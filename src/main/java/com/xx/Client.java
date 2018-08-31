/**
 * 
 */
package com.xx;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xx.core.decoder.MessageDecoder;
import com.xx.core.dto.LinkCheckMessage;
import com.xx.core.dto.Message;
import com.xx.core.dto.ObjectMessage;
import com.xx.core.dto.RegisterMessage;
import com.xx.core.encoder.MessageEncoder;
import com.xx.device.SyncFuture;
import com.xx.exception.ClientException;
import com.xx.handler.ClientHandler;
import com.xx.handler.LinkCheckHandler;
import com.xx.handler.RegisterHandler;
import com.xx.util.CommonUtil;
import com.xx.util.Crc8Util;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author lee
 *
 */
public class Client {

	private static Logger log = LoggerFactory.getLogger(Client.class);

	public static final AtomicInteger SEQ = new AtomicInteger(0);

	public static final int WAIT_SECONDS = 6;

	public enum ClientState {
		INITIAL(0, "初始化"), RUNNING(1, "运行中"), STOPPED(2, "已停止");
		private int code;

		private String desc;

		private ClientState(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}

	}

	private String host;

	private int port;

	private String clientId;

	/**
	 * 厂家编码
	 */
	private Integer productNo;
	/**
	 * 厂家密码
	 */
	private Integer productPwd;

	/**
	 * 生产日期-年
	 */
	private Integer year;

	/**
	 * 生产日期-月
	 */
	private Integer month;

	/**
	 * 测站id
	 */
	private Integer station;

	/**
	 * 连接检测间隔
	 */
	private Integer interval;

	/**
	 * 设备序列号
	 */
	private String serial;

	private ClientState state = ClientState.INITIAL;

	private SocketChannel socketChannel;

	private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

	/**
	 * 消息future
	 */
	private SyncFuture<Message> responseFuture = new SyncFuture<>();

	/**
	 * 客户端连接future
	 */
	private SyncFuture<Boolean> connectFuture = new SyncFuture<>();

	public Client(String host, int port, String clientId, Integer productNo, Integer productPwd, Integer year,
			Integer month, Integer station, Integer interval, String serial) throws ClientException {
		super();
		this.host = host;
		this.port = port;
		this.clientId = clientId == null ? CommonUtil.getLocalMac() + "#" + SEQ.getAndIncrement() : clientId;
		this.productNo = productNo;
		this.productPwd = productPwd;
		this.year = year;
		this.month = month;
		this.station = station;
		this.interval = interval;
		this.serial = serial;
	}

	public boolean connect() throws InterruptedException, ExecutionException {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Bootstrap bootstrap = new Bootstrap();

				// 心跳数据
				final LinkCheckMessage msg = new LinkCheckMessage();
				/*
				 * msg.setDirect(1); msg.setDiv(0); msg.setFcb(3); msg.setFunctionCode(1);
				 */
				msg.setProductNo(productNo);
				msg.setProductPwd(productPwd);
				msg.setMonth(month);
				msg.setYear(year);
				msg.setStation(station);

				// 设备注册信息
				final RegisterMessage message = new RegisterMessage();
				message.setSerial(serial);
				// message.setDirect(1);
				// message.setDiv(0);
				// message.setFcb(3);
				// message.setFunctionCode(1);
				message.setProductNo(productNo);
				message.setProductPwd(productPwd);
				message.setMonth(month);
				message.setYear(year);
				message.setStation(station);
				bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
						.option(ChannelOption.TCP_NODELAY, true).remoteAddress(host, port)
						.handler(new ChannelInitializer<SocketChannel>() {
							@Override
							public void initChannel(SocketChannel ch) throws Exception {
								ChannelPipeline p = ch.pipeline();
								p.addLast(new MessageDecoder());
								p.addLast(new MessageEncoder());
								p.addLast(new LinkCheckHandler(clientId, interval, msg));
								p.addLast(new RegisterHandler(clientId, message));
								p.addLast(new ClientHandler(responseFuture, clientId));
							}
						});

				// 进行连接
				ChannelFuture future;
				try {
					future = bootstrap.connect(host, port).sync();
					// 判断是否连接成功
					if (future.isSuccess()) {
						// 得到管道，便于通信
						socketChannel = (SocketChannel) future.channel();
						log.info(String.format("客户端[%s]开启成功...", clientId));
						state = ClientState.RUNNING;
						// 释放连接等待
						connectFuture.setResponse(true);
						// 等待客户端链路关闭，就是由于这里会将线程阻塞，导致无法发送信息，所以我这里开了线程
						socketChannel.closeFuture().sync();
					} else {
						log.info(String.format("客户端[%s]开启失败...", clientId));
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					// 优雅地退出，释放相关资源
					// eventLoopGroup.shutdownGracefully();
					// state = ClientState.STOPPED;
					reConnectServer();
				}

			}
		});
		thread.setName(clientId + "#client");
		thread.setDaemon(true);
		thread.start();
		return connectFuture.get();
	}

	/**
	 * 断线重连
	 */
	private void reConnectServer() {
		try {
			Thread.sleep(5000);
			log.info("客户端[{}]断开重连", clientId);
			connect();
		} catch (Exception e) {
			log.error("重连失败",e);
		}
	}

	public void shutdown() throws ClientException {
		if (isRunning()) {
			eventLoopGroup.shutdownGracefully();
			state = ClientState.STOPPED;
		} else {
			throw new ClientException("客户端已关闭");
		}
	}

	public boolean isRunning() {
		return ClientState.RUNNING.getCode() == state.getCode();
	}

	public Message sendMessage(ObjectMessage message, int seconds) throws ClientException {
		if (!isRunning()) {
			throw new ClientException("客户端未启动");
		}
		for (Message msg : message.toMessage()) {
			log.info(String.format("客户端[%s]发送数据：%s", clientId, Crc8Util.formatHexString(msg.toHexString())));
			socketChannel.writeAndFlush(msg);
		}
		Message msg = null;
		try {
			msg = responseFuture.get(seconds, TimeUnit.SECONDS);
		} catch (Exception e) {
			throw new ClientException(e);
		}
		return msg;
	}

	public Message sendMessage(ObjectMessage message) throws ClientException {
		message.setProductNo(productNo);
		message.setProductPwd(productPwd);
		message.setYear(year);
		message.setMonth(month);
		message.setStation(station);
		return sendMessage(message, WAIT_SECONDS);
	}

	public static void main(String[] args) {

	}

}

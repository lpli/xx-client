/**
 * 
 */
package com.xx;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.xx.core.decoder.MessageDecoder;
import com.xx.core.dto.Message;
import com.xx.core.dto.ObjectMessage;
import com.xx.core.encoder.MessageEncoder;
import com.xx.device.SyncFuture;
import com.xx.exception.ClientException;
import com.xx.handler.ClientHandler;
import com.xx.util.CommonUtil;
import com.xx.util.Crc8Util;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author lee
 *
 */
public class Client {

	private static Log log = LogFactory.getLog(Client.class);

	public static final AtomicInteger SEQ = new AtomicInteger(0);

	public static final int WAIT_SECONDS = 6;

	public enum ClientState {
		INITIAL(0,"初始化"),RUNNING(1, "运行中"), STOPPED(2, "已停止");
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

	private ClientState state = ClientState.INITIAL;

	private SocketChannel socketChannel;

	private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

	private SyncFuture<Message> responseFuture = new SyncFuture<>();

	public Client(String host, int port) throws ClientException {
		super();
		this.clientId = CommonUtil.getLocalMac() + "#" + SEQ.getAndIncrement();
		this.host = host;
		this.port = port;
		start();
	}

	private void start() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Bootstrap bootstrap = new Bootstrap();
				bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
						// .option(ChannelOption.SO_KEEPALIVE, true)
						.remoteAddress(host, port).handler(new ChannelInitializer<SocketChannel>() {
							@Override
							public void initChannel(SocketChannel ch) throws Exception {
								ChannelPipeline p = ch.pipeline();
								p.addLast(new MessageDecoder());
								p.addLast(new MessageEncoder());
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
						// 等待客户端链路关闭，就是由于这里会将线程阻塞，导致无法发送信息，所以我这里开了线程
						socketChannel.closeFuture().sync();

					} else {
						log.info( String.format("客户端[%s]开启失败...", clientId));
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					// 优雅地退出，释放相关资源
					eventLoopGroup.shutdownGracefully();
					state = ClientState.STOPPED;
				}

			}
		});
		thread.start();
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
		return sendMessage(message, WAIT_SECONDS);
	}

	public static void main(String[] args) {

	}

}

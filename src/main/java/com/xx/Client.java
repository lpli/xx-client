/**
 * 
 */
package com.xx;

import com.xx.core.decoder.MessageDecoder;
import com.xx.core.dto.Message;
import com.xx.core.dto.ObjectMessage;
import com.xx.core.encoder.MessageEncoder;
import com.xx.handler.ClientHandler;

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

	private String host;

	private int port;

	private SocketChannel socketChannel;

	public Client(String host, int port) {
		super();
		this.host = host;
		this.port = port;
		start();
	}

	private void start() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

				Bootstrap bootstrap = new Bootstrap();
				bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
						.remoteAddress(host, port).handler(new ChannelInitializer<SocketChannel>() {
							@Override
							public void initChannel(SocketChannel ch) throws Exception {
								ChannelPipeline p = ch.pipeline();
								p.addLast(new MessageDecoder());
								p.addLast(new MessageEncoder());
								p.addLast(new ClientHandler());
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
						System.out.println("客户端开启成功...");
						// 等待客户端链路关闭，就是由于这里会将线程阻塞，导致无法发送信息，所以我这里开了线程
						socketChannel.closeFuture().sync();

					} else {
						System.out.println("客户端开启失败...");
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					// 优雅地退出，释放相关资源
					eventLoopGroup.shutdownGracefully();
				}

			}
		});
		thread.start();
	}

	public void sendMessage(ObjectMessage message) {
		if (socketChannel != null) {
			for (Message msg : message.toMessage()) {
				System.out.println("客户端发送数据：" );
				Crc8Util.printHexString(msg.toHexString());
				socketChannel.writeAndFlush(message);
			}
		}
	}

	public static void main(String[] args) {

	}

}

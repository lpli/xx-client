/**
 * 
 */
package com.xx;

import com.xx.core.decoder.MessageDecoder;
import com.xx.core.encoder.MessageEncoder;
import com.xx.handler.ClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
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
	public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
              Bootstrap b = new Bootstrap();
              b.group(group)
               .channel(NioSocketChannel.class)
               .handler(new ChannelInitializer<SocketChannel>() {
                   @Override
                   public void initChannel(SocketChannel ch) throws Exception {
                       ChannelPipeline p = ch.pipeline();
                       p.addLast(new MessageDecoder());
                       p.addLast(new MessageEncoder());
                       p.addLast(new ClientHandler());
                   }
               });

              // Start the client.
              ChannelFuture f = b.connect("127.0.0.1", 8867).sync();

              // Wait until the connection is closed.
              f.channel().closeFuture().sync();

          } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
              // Shut down the event loop to terminate all threads.
              group.shutdownGracefully();
          }

    }

}

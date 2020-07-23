package com.yu.netty.base;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
public class NettyClient {

    private final String host = "127.0.0.1";
    private final Integer port = 9557;

    private final NioEventLoopGroup event;

    public NettyClient (){
        this.event = new NioEventLoopGroup();
    }

    @Test
    public void start() throws InterruptedException {
        try{
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(event)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(host, port));

            //等待通道关闭(断线)
            channelFuture.channel().closeFuture().sync();
        } finally {
            event.shutdownGracefully();
        }
    }
}

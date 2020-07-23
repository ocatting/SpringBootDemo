package com.yu.netty.base;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
public class NettyServer {

    private final Integer port = 9557;

    private final EventLoopGroup eventLoopGroupSelector;
    private final EventLoopGroup eventLoopGroupBoss;

    public NettyServer() {
        /**
         * 可以选择采用linux epoll 模型
         */
        if(useEpoll()){
            this.eventLoopGroupBoss = new EpollEventLoopGroup(1);
            this.eventLoopGroupSelector = new EpollEventLoopGroup();
        }else {
            this.eventLoopGroupBoss = new NioEventLoopGroup(1);
            this.eventLoopGroupSelector = new NioEventLoopGroup();
        }
    }

    /**
     * 服务端启动
     * @throws InterruptedException
     */
    @Test
    public void start() throws InterruptedException {
        try{
            // 主要启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(eventLoopGroupBoss,eventLoopGroupSelector)
                    .channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(port);

            channelFuture.channel().closeFuture().sync();

        } finally {
            eventLoopGroupBoss.shutdownGracefully();
            eventLoopGroupSelector.shutdownGracefully();
        }
    }

    public static boolean useEpoll(){
        String OS_NAME = System.getProperty("os.name");
        if (OS_NAME != null && OS_NAME.toLowerCase().contains("linux")) {
            return true;
        }
        return false;
    }
}

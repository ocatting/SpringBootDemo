package com.yu.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @Description: Netty实现 聊天室 功能 ； 客户端
 * @Author Yan XinYu
 **/
@Slf4j
public class NettyChatClient {

    private final String host = "127.0.0.1";
    private final Integer port = 9557;

    private final NioEventLoopGroup event;

    public NettyChatClient (){
        this.event = new NioEventLoopGroup();
    }

    /**
     * @Test 启动 Scanner 类无效 采用 main 方法启动
     * @throws InterruptedException
     */
    public void start() throws InterruptedException {
        try{
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(event)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // Netty实现的 String 编解码工具，顺序？
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new NettyChatClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(host, port));
            Channel channel = channelFuture.channel();

            log.info("NettyChatClient：starting");
            /**
             * 控制台输入信息
             */
            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNextLine()){
                String msg = scanner.nextLine();
                if(msg.toLowerCase().equals("quit")){
                    break;
                }
                channel.writeAndFlush(msg);
            }

            //等待通道关闭(断线)
            channel.closeFuture().sync();
        } finally {
            event.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        NettyChatClient nettyChatClient = new NettyChatClient();
        nettyChatClient.start();
    }

}

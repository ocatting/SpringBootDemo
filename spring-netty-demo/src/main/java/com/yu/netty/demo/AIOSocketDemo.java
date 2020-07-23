package com.yu.netty.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: AIO 线程模型 异步非阻塞
 * @Author Yan XinYu
 **/
@Slf4j
public class AIOSocketDemo {

    public final String host = "127.0.0.1";
    public final Integer port = 9500;


    /**
     * AIO 单线程处理 模式
     */
    @Test
    public void aio_server() throws IOException {
        final AsynchronousServerSocketChannel aServerSocketChannel = AsynchronousServerSocketChannel.open();
        aServerSocketChannel.bind(new InetSocketAddress(host,port));

        //处理逻辑
        aServerSocketChannel.accept(null,

                new CompletionHandler<AsynchronousSocketChannel, Object>() {
                    @Override
                    public void completed(AsynchronousSocketChannel socketChannel, Object attachment) {
                        aServerSocketChannel.accept(attachment, this);
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer, buffer,

                                new CompletionHandler<Integer, ByteBuffer>() {
                                    @Override
                                    public void completed(Integer result, ByteBuffer attachment) {
                                        buffer.flip();
                                        System.out.println(new String(buffer.array(), 0, result));
                                        socketChannel.write(ByteBuffer.wrap("HelloClient".getBytes()));
                                    }

                                    @Override
                                    public void failed(Throwable exc, ByteBuffer attachment) {
                                        exc.printStackTrace();
                                    }
                                }
                        );

                    }

                    @Override
                    public void failed(Throwable exc, Object attachment) {
                        exc.printStackTrace();
                    }
                }
                );
    }

    /**
     * AIO 客户端
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void aio_client() throws IOException, ExecutionException, InterruptedException {
        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
        socketChannel.connect(new InetSocketAddress(host, 9000));
        socketChannel.write(ByteBuffer.wrap("HelloServer".getBytes()));
        ByteBuffer buffer = ByteBuffer.allocate(512);
        Integer len = socketChannel.read(buffer).get();
        if (len != -1) {
            System.out.println("客户端收到信息：" + new String(buffer.array(), 0, len));
        }
    }

    /**
     * AIO 多线程处理 模式
     */
    @Test
    public void aio_server_multiThread() throws IOException {

        ExecutorService executorService = Executors.newCachedThreadPool();
        //initialSize代表使用几个线程池处理
        AsynchronousChannelGroup threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 2);

        final AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open(threadGroup)
                .bind(new InetSocketAddress(9000));


        //此处与单线程 处理逻辑 一样

        serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel socketChannel, Object attachment) {
                try {
                    serverChannel.accept(attachment, this);
                    System.out.println(socketChannel.getRemoteAddress());
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    socketChannel.read(buffer, buffer,
                            new CompletionHandler<Integer, ByteBuffer>() {

                                @Override
                                public void completed(Integer result, ByteBuffer attachment) {
                                    attachment.flip();
                                    log.info(new String(attachment.array(), 0, result));
                                    socketChannel.write(ByteBuffer.wrap("HelloClient".getBytes()));
                                }

                                @Override
                                public void failed(Throwable exc, ByteBuffer attachment) {
                                    exc.printStackTrace();
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });
    }

}

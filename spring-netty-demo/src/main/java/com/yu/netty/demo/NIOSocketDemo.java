package com.yu.netty.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.sound.sampled.Line;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Description: NIO 的例子
 *
 * NIO SelectionKey 四种事件：
 *      SelectionKey.OP_ACCEPT —— 接收连接继续事件，表示服务器监听到了客户连接，服务器可以接收这个连接了
 *      SelectionKey.OP_CONNECT —— 连接就绪事件，表示客户与服务器的连接已经建立成功
 *      SelectionKey.OP_READ —— 读就绪事件，表示通道中已经有了可读的数据，可以执行读操作了（通道目前有数据，可以进行读操作了）
 *      SelectionKey.OP_WRITE —— 写就绪事件，表示已经可以向通道写数据了（通道目前可以用于写操作）
 *
 * @Author Yan XinYu
 **/
@Slf4j
public class NIOSocketDemo {

    /**
     * 处理客户端事件
     */
    public void handler_server(SelectionKey key) throws IOException {
        // 客户端请求与服务端接收连接
        if(key.isAcceptable()){
            log.info("客户端请求与服务端建立连接...");
            /**
             * ServerSocketChannel 设定 该客户端可以请求数据
             */
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            ssc.configureBlocking(false);
            ssc.register(key.selector(), SelectionKey.OP_READ);
            return;
        }



        //客户端请求服务器读取发送数据
        if(key.isReadable()){
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int len = sc.read(buffer);
            if (len != -1) {
                log.info("读取到发送的数据：" + new String(buffer.array(), 0, len));
            }
            ByteBuffer bufferToWrite = ByteBuffer.wrap("HelloClient".getBytes());
            sc.write(bufferToWrite);
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            return;
        }
        //客户端通道已准备就绪服务端可以写入数据
        if(key.isWritable()){
            log.info("客户端已准备就绪，服务端可写入数据");

            key.interestOps(SelectionKey.OP_READ);
            return;
        }
    }

    /**
     * 处理服务端时间
     * @param key
     * @throws IOException
     */
    public void handler_client(SelectionKey key) throws IOException {
        //客户端与服务端连接请求完成
        if(key.isConnectable()){
            log.info("客户端已与服务端建立连接...");
            SocketChannel channel = (SocketChannel) key.channel();
            // 如果正在连接，则完成连接
            if (channel.isConnectionPending()) {
                channel.finishConnect();
            }
            // 设置成非阻塞
            channel.configureBlocking(false);
            //在这里可以给服务端发送信息哦
            ByteBuffer buffer = ByteBuffer.wrap("HelloServer".getBytes());
            channel.write(buffer);
            //在和服务端连接成功之后，为了可以接收到服务端的信息，需要给通道设置读的权限。
            channel.register(key.selector(), SelectionKey.OP_READ);
            return;
        }

        if(key.isReadable()){
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int len = channel.read(buffer);
            if (len != -1) {
                log.info("客户端收到信息：" + new String(buffer.array(), 0, len));
            }
            return;
        }
    }

    public final String host = "127.0.0.1";
    public final Integer port = 9500;

    @Test
    public void nio_server() throws IOException {

        ServerSocketChannel open = ServerSocketChannel.open();
        //设置非阻塞
        open.configureBlocking(false);
        open.socket().bind(new InetSocketAddress(port));

        //多路复用器
        Selector selector = Selector.open();

        //注册绑定多路复用器
        open.register(selector,SelectionKey.OP_ACCEPT);

        while (true){
            /**
             * 多路复用器等待客户端事件触发，比如：建立连接，写入数据。
             * 此方法里面会采用os的 select,poll,epoll机制 轮询数据直到有client事件发生
             * 该方法未找到事件执行时会一直阻塞。
             */
            selector.select();

            log.info("client 有事件被触发");
            //获取监听到的所有事件并被 串行 执行。
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                /**
                 * 此处可以采用线程池执行
                 */
                handler_server(key);
            }
            iterator.remove();//防止重复执行，其实我觉得没必要
        }
    }

    /**
     * NIO 客户端
     * @throws IOException
     */
    @Test
    public void nio_client() throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        Selector selector = Selector.open();
        channel.connect(new InetSocketAddress(host, port));
        channel.register(selector, SelectionKey.OP_CONNECT);
    }



}

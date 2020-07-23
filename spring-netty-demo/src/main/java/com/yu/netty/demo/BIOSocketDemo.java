package com.yu.netty.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
public class BIOSocketDemo {

    public void handler_server(Socket socket) throws IOException {
        byte[] bytes = new byte[1024];
        //接收客户端信息，将会等待客户端写入数据，否则该方法会一直阻塞等待。
        int read = socket.getInputStream().read(bytes);
        if(read != -1){
            log.info("接收到客户端信息:{}",new String(bytes,0,read));
        }
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("nihao,my is socketClient".getBytes(StandardCharsets.UTF_8));
        outputStream.flush();//立刻刷新回待发送内存或者磁盘中
    }

    public final String host = "127.0.0.1";
    public final Integer port = 9500;

    @Test
    public void socket_server() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true){
            log.info("connect...");
            Socket socket = serverSocket.accept();
            handler_server(socket);
            //多线程处理多个客户端，为每个客户端建立一个线程
//            new Thread(() -> {
//                try {
//                    handler_server(socket);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
        }
    }

    @Test
    public void socket_client() throws IOException {
        //建立连接
        Socket socket = new Socket(host, port);

        //向服务端发送数据
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("accept:client".getBytes(StandardCharsets.UTF_8));
        outputStream.flush();

        //接收服务端数据
        byte[] bytes = new byte[1024];
        InputStream inputStream = socket.getInputStream();
        //阻塞等待服务端发送数据
        inputStream.read(bytes);
        log.info("accept:server:{}",new String(bytes));
        inputStream.close();
    }



}

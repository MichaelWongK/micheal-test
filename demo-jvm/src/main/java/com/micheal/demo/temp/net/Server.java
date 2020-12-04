package com.micheal.demo.temp.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/2 17:24
 * @Description
 */
public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("服务器启动");
        // BIO 阻塞io
        Socket clientServer = serverSocket.accept();
        System.out.println("客户端连接成功");

        InputStream inputStream = clientServer.getInputStream();
        OutputStream outputStream = clientServer.getOutputStream();

        new BufferedReader(new InputStreamReader(inputStream));

    }
}

package com.micheal.demo.temp.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/2 15:54
 * @Description
 *   http://localhost:8080/xxx==http://127.0.0.1:8080/xxx[ip有很多种比如127.0.0.1这个叫做本地回环IP，
 *       255.255.255.255广播地址] localhost:8080:套接字
 *       键入http://www.baidu.com->DNS解析->通过TCP协议封装数据->把TCP的报文封装成IP数据报->通过ARP找到下一个要发送的机器的
 *       MAC地址->封装成帧发送 C/S架构： 其实就是C负责发送请求，S负责响应请求 B/S架构： 浏览器发送请求，S响应请求
 *		0xFFFF:一位16进制由四位二进制表示。。。。。
 *		F：1111
 *		4*4=16;
 *		2^16=65536;
 *		=>端口号的范围：0<port<65536
 */
public class NetDemo {

    public static void createServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket accept = serverSocket.accept();
        InputStream inputStream = accept.getInputStream();
        int i = 0;
        while ((i = inputStream.read()) != -1) {
            System.out.print((char) i);
        }
        System.out.println(accept.getKeepAlive());
        OutputStream outputStream = accept.getOutputStream();
        outputStream.write("Hello".getBytes());
        outputStream.close();
        accept.close();
    }

    public static void main(String[] args) throws IOException {
        createServer();
    }
}

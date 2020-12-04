package com.micheal.demo.temp.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/2 17:14
 * @Description
 */
public class ClientDemo {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8080);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("hello server".getBytes());
//        outputStream.close();
        InputStream inputStream = socket.getInputStream();
        int i = 0;
        while ((i = inputStream.read()) != -1) {
            System.out.print((char) i);
        }
    }
}

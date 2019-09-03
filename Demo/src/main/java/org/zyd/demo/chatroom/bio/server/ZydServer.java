package org.zyd.demo.chatroom.bio.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 聊天室服务端
 */
public class ZydServer {

    // 服务端口
    static final int SERVER_PROT = 16888;

    // 客户端列表
    public static List<Socket> clientList = new ArrayList<Socket>();

    public static void main(String[] args) throws IOException {
        //创建一个ServerSocket，用于监听客户端Socket的连接请求
        ServerSocket server = new ServerSocket(SERVER_PROT);
        System.out.println("聊天室服务端启动，端口：" + SERVER_PROT);
        while (true) {
            // 接收客户端连接
            Socket socket = server.accept();
            clientList.add(socket);
            // 每连接一个客户端，启动一个ServerThread线程为该客户端服务
            new Thread(new ZydServerHandler(socket)).start();
        }
    }

}

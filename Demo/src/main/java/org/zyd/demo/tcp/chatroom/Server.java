package org.zyd.demo.tcp.chatroom;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    /**
     * 客户端集合
     */
    private static List<Socket> clients = new ArrayList<Socket>();

    private void start() {
        try {
            // 开启服务，设置指定端口
            ServerSocket server = new ServerSocket(5555);
            System.out.println("服务开启，等待客户端连接中...");
            // 循环监听
            while (true) {
                // 等待客户端进行连接
                Socket client = server.accept();
                // 将客户端添加到集合
                clients.add(client);
                System.out.println("客户端[" + client.getRemoteSocketAddress() + "]连接成功，当前在线用户" + clients.size() + "个");
                // 每一个客户端开启一个线程处理消息
                new MessageListener(client).start();
            }
        } catch (IOException e) {
            // log
        }
    }

    /**
     * 消息处理线程，负责转发消息到聊天室里的人
     */
    class MessageListener extends Thread {

        // 将每个连接上的客户端传递进来，收消息和发消息
        private Socket client;

        // 将这几个变量抽出来公用，避免频繁new对象
        private OutputStream os;
        private PrintWriter pw;
        private InputStream is;
        private InputStreamReader isr;
        private BufferedReader br;

        public MessageListener(Socket socket) {
            this.client = socket;
        }

        @Override
        public void run() {
            try {
                // 每个用户连接上了，就发送一条系统消息（类似于广播）
                sendMsg(0, "[系统消息]：欢迎" + client.getRemoteSocketAddress() + "来到聊天室，当前共有" + clients.size() + "人在聊天");
                // 循环监听消息
                while (true) {
                    sendMsg(1, "[" + client.getRemoteSocketAddress() + "]：" + receiveMsg());
                }
            } catch (IOException e) {
                // log
            }
        }

        /**
         * 发送消息
         *
         * @param type 消息类型（0、系统消息；1、用户消息）
         * @param msg  消息内容
         * @throws IOException
         */
        private void sendMsg(int type, String msg) throws IOException {
            if (type != 0) {
                System.out.println("处理消息：" + msg);
            }
            for (Socket socket : clients) {
                if (type != 0 && socket == client) {
                    continue;
                }
                os = socket.getOutputStream();
                pw = new PrintWriter(os);
                pw.println(msg);// 这里需要特别注意，对方用readLine获取消息，就必须用print而不能用write，否则会导致消息获取不了
                pw.flush();
            }
        }

        /**
         * 接收消息
         *
         * @return 消息内容
         * @throws IOException
         */
        private String receiveMsg() throws IOException {
            is = client.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            return br.readLine();
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}

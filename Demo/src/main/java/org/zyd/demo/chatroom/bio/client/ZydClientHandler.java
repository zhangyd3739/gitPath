package org.zyd.demo.chatroom.bio.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ZydClientHandler implements Runnable {

    private Socket socket = null;

    public ZydClientHandler(Socket socket) throws IOException {
        this.socket = socket;
    }

    public void run() {
        try {
            String content = null;
            // 获取服务端返回的消息
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((content = bufferedReader.readLine()) != null) {
                System.out.println("聊天记录：" + content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

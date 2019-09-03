package org.zyd.demo.chatroom.bio.server;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ZydServerHandler implements Runnable {

    private Socket socket = null;

    public ZydServerHandler(Socket socket) throws IOException {
        this.socket = socket;
    }

    public void run() {
        try {
            String content = null;
            // 循环从Socket中读取客户端发送过来的数据
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((content = bufferedReader.readLine()) != null) {
                // 将读到的内容向每个Socket发送一次
                for (Socket clientSocket : ZydServer.clientList) {
                    // 服务端处理消息
                	String result = this.packMessage(content);
                    PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());
                    printWriter.println(result);
                    printWriter.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 包装消息：时间+内容
     *
     * @param msg
     * @return
     */
    private String packMessage(String msg) {
        StringBuffer result = new StringBuffer();
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");    //设置日期格式
        result.append(df.format(new Date()));
        result.append("--------");
        result.append(msg);
        return result.toString();
    }
}

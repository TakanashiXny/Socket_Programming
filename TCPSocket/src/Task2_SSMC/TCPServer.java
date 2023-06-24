package Task2_SSMC;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        // 1. 创建⼀个服务器端Socket，即ServerSocket，监听指定端⼝
        serverSocket = new ServerSocket(port);
        // 2. 调⽤accept()⽅法开始监听，阻塞等待客户端的连接
        for (;;) {
            System.out.println("阻塞等待客户端连接中...");
            // TODO
            Socket clientSocket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clientHandler.start();
        }
    }

    public void stop() {
        // 关闭相关资源
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 9091;
        TCPServer server = new TCPServer();
        try {
            server.start(port);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}

package Task1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private ServerSocket serverSocket;


    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("阻塞等待客户端连接中...");

        for (;;) {
            Socket clientSocket = serverSocket.accept();
            ClientHandler ch = new ClientHandler(clientSocket);
            ch.start();
        }
    }

    public void stop() throws IOException {
        try {

            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
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

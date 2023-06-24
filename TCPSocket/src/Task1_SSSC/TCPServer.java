package Task1_SSSC;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class TCPServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) throws IOException {
        // 1. 创建⼀个服务器端Socket，即ServerSocket，监听指定端⼝
        serverSocket = new ServerSocket(port);
        // 2. 调⽤accept()⽅法开始监听，阻塞等待客户端的连接
        System.out.println("阻塞等待客户端连接中...");
        clientSocket = serverSocket.accept();
        // 3. 获取Socket的字节输出流
        out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(),
                StandardCharsets.UTF_8), true);
        // 4. 获取Socket的字节输⼊流，并准备读取客户端发送的信息
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
        // 5. 阻塞读取客户端发送的信息
        String str;
        while ((str = in.readLine()) != null) {
            str = str.toUpperCase(Locale.ROOT);
            out.println("服务端已收到消息" + str);
        }
    }

    public void stop() {
        // 关闭相关资源
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
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

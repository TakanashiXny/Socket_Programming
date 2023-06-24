package Task2;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPClient {
    private Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        // 1. 创建客户端Socket，指定服务器地址，端⼝
        clientSocket = new Socket(ip, port);
        // 2. 获取输⼊输出流
        out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(),
                StandardCharsets.UTF_8), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
    }


    public void stopConnection() {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 9091;
        TCPClient client = new TCPClient();
        try {
            client.startConnection("127.0.0.1", port);

            try {
                String str;
                while ((str = in.readLine()) != null) {
                    System.out.println(str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.stopConnection();
        }
    }
}

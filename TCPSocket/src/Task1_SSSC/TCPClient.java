package Task1_SSSC;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TCPClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        // 1. 创建客户端Socket，指定服务器地址，端⼝
        clientSocket = new Socket(ip, port);
        // 2. 获取输⼊输出流
        out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(),
                StandardCharsets.UTF_8), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
    }

    public String sendMessage(String msg) throws IOException {
        // 3. 向服务端发送消息
        out.println(msg);
        // 4. 接收服务端回写信息
        return in.readLine();
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

            // TODO
            System.out.println("请输入message: ");
            Scanner sc = new Scanner(System.in);

            while (sc.hasNext()) {
                String message = sc.nextLine();
                String response = client.sendMessage(message);
                System.out.println(response);
                System.out.println("请输入message: ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.stopConnection();
        }
    }
}

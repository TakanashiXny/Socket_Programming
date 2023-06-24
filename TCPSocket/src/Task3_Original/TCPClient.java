package Task3_Original;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TCPClient {
    private Socket clientSocket;
    private OutputStream out;
    private static int BYTE_LENGTH = 64;
    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = clientSocket.getOutputStream();
    }
    public void sendMessage(String msg) throws IOException {
        // 重复发送⼗次
        for(int i=0;i<10;i++){
            out.write(msg.getBytes());
        }
    }
    public void stopConnection() {
        // 关闭相关资源
        try {
            if(out!=null) out.close();
            if(clientSocket!=null) clientSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 9091;
        TCPClient client = new TCPClient();
        try {
            client.startConnection("127.0.0.1", port);
            StringBuilder message = new StringBuilder("NETWORK PRINCIPLE");
            client.sendMessage(message.toString());
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            client.stopConnection();
        }
    }
}

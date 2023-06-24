package Task3_M1;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private static int BYTE_LENGTH = 64;
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("阻塞等待客户端连接中...");
        clientSocket = serverSocket.accept();
        InputStream is = clientSocket.getInputStream();
        for(;;) {
            byte[] bytes = new byte[BYTE_LENGTH];
            // 读取客户端发送的信息
            int cnt = is.read(bytes, 0, BYTE_LENGTH);
            if(cnt>0)
                System.out.println("服务端已收到消息: " + new String(bytes).trim());
        }
    }

    public void stop(){
        // 关闭相关资源
        try {
            if(clientSocket!=null) clientSocket.close();
            if(serverSocket!=null) serverSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 9091;
        TCPServer server=new TCPServer();
        try {
            server.start(port);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            server.stop();
        }
    }

}

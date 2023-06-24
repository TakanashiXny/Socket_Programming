package Task3_M3;

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
        SocketPacket socketPacket = new SocketPacket();
        try  {
            for(;;) {
                int bodySize = 0;
                try {
                    bodySize = socketPacket.getHeader(is);
                } catch (NumberFormatException e) {
                    break;
                }

                byte[] bodyByte = new byte[bodySize];

                int readCount = 0;
                int bodyIndex = 0;

                while (bodyIndex <= (bodySize - 1) && (readCount = is.read(bodyByte, bodyIndex, bodySize)) != -1) {
                    bodyIndex += readCount;
                }
                System.out.println("服务端已收到消息: " + new String(bodyByte));
            }
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
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

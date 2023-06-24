package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private static ServerSocket serverSocket;
    private static final int DEFAULT_PORT = 8081;

    public void start() throws IOException, InterruptedException {
        serverSocket = new ServerSocket(DEFAULT_PORT);
        System.out.println("服务器正在监听端口" + serverSocket.getLocalPort());

        for (;;) {
            final Socket socket = serverSocket.accept();
            Handler handler = new Handler(socket);
            handler.start();
        }
    }

    public void stop() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        try {
            server.start();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}

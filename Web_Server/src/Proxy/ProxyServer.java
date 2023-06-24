package Proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProxyServer {
    private ServerSocket proxyServerSocket;
    private static final int DEFAULT_PORT = 8083;

    public void start() throws IOException, InterruptedException {
        proxyServerSocket = new ServerSocket(DEFAULT_PORT);
        System.out.println("代理服务器正在监听端口" + DEFAULT_PORT);

        for (;;) {
            Socket socket = proxyServerSocket.accept();
            ProxyHandler handler = new ProxyHandler(socket);
            handler.start();
        }
    }

    public void stop() {
        try {
            if (proxyServerSocket != null) {
                proxyServerSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ProxyServer server = new ProxyServer();
        try {
            server.start();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}

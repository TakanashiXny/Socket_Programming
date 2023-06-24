package Task2;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class TCPServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    Collection<Socket> sockets = new ArrayList<>();

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("阻塞等待客户端连接中...");

        for (;;) {

            clientSocket = serverSocket.accept();
            Iterator<Socket> it = sockets.iterator();
            while(it.hasNext()){
                boolean flag = true;
                Socket s = it.next();
                try {
                    s.getOutputStream().write(0);
                } catch (IOException e) {
                    flag = false;
                }
                if (!flag) {
                    it.remove();
                }
            }

            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(),
                    StandardCharsets.UTF_8), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),
                    StandardCharsets.UTF_8));
            sockets.add(clientSocket);

            Collection<SocketAddress> socketAddr = new ArrayList<>();
            for (Socket eachSocket : sockets) {
                socketAddr.add(eachSocket.getRemoteSocketAddress());
            }
            for (Socket eachSocket : sockets) {
                try {
                    out = new PrintWriter(new OutputStreamWriter(eachSocket.getOutputStream(),
                            StandardCharsets.UTF_8), true);
                    out.println(socketAddr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() throws IOException {
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

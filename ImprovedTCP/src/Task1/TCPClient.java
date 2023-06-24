package Task1;

import java.io.*;
import java.net.Socket;
import static java.lang.Thread.sleep;

public class TCPClient {
    private Socket clientSocket;

    public void startConnection(String id, int port) throws IOException, InterruptedException {
        clientSocket = new Socket(id, port);
        ServerHandler sh = new ServerHandler(clientSocket);
        sh.start();
        sh.join();
    }

    public void stopConnection() {
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 9091;
        TCPClient client = new TCPClient();
        try {
            client.startConnection("127.0.0.1", port);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sleep(10000000);
            client.stopConnection();
        }
    }
}

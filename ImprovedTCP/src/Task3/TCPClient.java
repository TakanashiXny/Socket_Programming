package Task3;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TCPClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private void startConnection(String id, int port) throws IOException, InterruptedException {
        socket = new Socket(id, port);
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

        Thread sendMessage = new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (sc.hasNext()) {
                String msg = sc.nextLine();
                out.println(msg);
            }
        });

        sendMessage.start();
        sendMessage.join();
    }

    private void stopConnection() throws IOException {
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
        if (socket != null) {
            socket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 9091;
        TCPClient client = new TCPClient();
        try {
            client.startConnection("127.0.0.1", port);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            client.stopConnection();
        }
    }
}

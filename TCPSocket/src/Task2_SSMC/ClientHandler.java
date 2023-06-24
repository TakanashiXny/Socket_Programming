package Task2_SSMC;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

class ClientHandler extends Thread {
    private Socket socket;
    ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();
        // TODO
        try {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),
                    StandardCharsets.UTF_8), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                    StandardCharsets.UTF_8));
            String str;
            while ((str = in.readLine()) != null) {
                out.println("服务端已收到消息" + str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

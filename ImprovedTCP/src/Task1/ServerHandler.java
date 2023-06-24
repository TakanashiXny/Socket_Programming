package Task1;

import java.io.IOException;
import java.net.Socket;

public class ServerHandler extends Thread {
    private Socket socket;
    private ServerReadHandler serverReadHandler;
    private ServerWriteHandler serverWriteHandler;

    public ServerHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.serverReadHandler = new ServerReadHandler(socket.getInputStream());
        this.serverWriteHandler = new ServerWriteHandler(socket.getOutputStream());
    }

    @Override
    public void run() {
        super.run();
        serverReadHandler.start();
        serverWriteHandler.start();
    }
}

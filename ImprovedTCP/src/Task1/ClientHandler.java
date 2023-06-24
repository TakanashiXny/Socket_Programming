package Task1;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread{
    private Socket socket;
    private ClientReadHandler clientReadHandler;
    private ClientWriteHandler clientWriteHandler;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.clientReadHandler = new ClientReadHandler(socket.getInputStream());
        this.clientWriteHandler = new ClientWriteHandler(socket.getOutputStream());
    }

    @Override
    public void run() {
        super.run();
        clientReadHandler.start();
        clientWriteHandler.start();
    }
}

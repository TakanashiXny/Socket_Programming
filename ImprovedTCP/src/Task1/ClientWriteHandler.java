package Task1;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientWriteHandler extends Thread{
    private final PrintWriter printWriter;
    private final Scanner sc;

    public ClientWriteHandler(OutputStream outputStream) {
        printWriter = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);
        this.sc = new Scanner(System.in);
    }

    void send(String str) {
        this.printWriter.println(str);
    }

    @Override
    public void run() {
        while (sc.hasNext()) {
            String str = sc.next();
            send(str);
        }
    }
}

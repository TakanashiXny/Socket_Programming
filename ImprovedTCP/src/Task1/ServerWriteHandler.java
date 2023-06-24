package Task1;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ServerWriteHandler extends Thread {
    private final PrintWriter printWriter;
    private final Scanner sc;

    ServerWriteHandler(OutputStream outputStream) {
        printWriter = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);
        sc = new Scanner(System.in);
    }

    @Override
    public void run() {
        while (sc.hasNext()) {
            String str = sc.next();
            printWriter.println(str);
        }
    }
}

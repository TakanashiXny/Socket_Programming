package Task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ServerReadHandler extends Thread {
    private final BufferedReader bufferedReader;

    ServerReadHandler(InputStream inputStream) {
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    @Override
    public void run() {
        try {
            while (true) {
                String str = bufferedReader.readLine();
                if (str == null) {
                    System.out.println("读到的数据为空");
                    break;
                } else {
                    System.out.println("读到的数据为：" + str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

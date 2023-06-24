package bonus;

import java.io.*;
import java.net.*;
import java.util.Random;

public class TCPFileClient {
    public static void main(String[] args) throws IOException {
        // ⽣成并写⼊发送⽂件
        try (FileWriter fileWriter = new FileWriter("checksum.txt")) {
            Random r = new Random(2023);
            // 尝试 1e3 and 1e8
            for (int i = 0; i < 1e8; i++) {
                fileWriter.write(r.nextInt());
            }
        }

        File file = new File("checksum.txt");
        System.out.println("发送文件生成完毕");
        System.out.println("发送文件的md5为: " + MD5Util.getMD5(file));

        FileInputStream fis = new FileInputStream(file);
        Socket socket = new Socket("127.0.0.1", 2222);
        OutputStream out = socket.getOutputStream();
        byte[] bytes = new byte[1024];

        // 不断从⽂件读取字节并将其组装成数据报发送
        int len;
        for (;;) {
            len = fis.read(bytes);
            if (len == -1) {
                break;
            }
            out.write(bytes, 0, len);
        }

        // 空数组作为发送终⽌符
        byte[] a = new byte[0];
        out.write(a);

        fis.close();
        out.close();
        socket.close();
        System.out.println("向" + socket.getRemoteSocketAddress().toString() + "发送⽂件完毕！端⼝号为:" + socket.getPort());
    }
}

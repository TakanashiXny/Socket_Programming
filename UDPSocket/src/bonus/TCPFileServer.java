package bonus;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class TCPFileServer {
    public static void main(String[] args) throws IOException {
        File file = new File("checksum_recv.txt");
        FileOutputStream output = new FileOutputStream(file);

        byte[] data = new byte[1024];
        ServerSocket serverSocket = new ServerSocket(2222);
        Socket socket = serverSocket.accept();

        InputStream in = socket.getInputStream();

        // TODO 实现不断接收数据报并将其通过⽂件输出流写⼊⽂件, 以数据报⻓度为零作为终⽌条件
        for (;;) {
            int len = in.read(data);
            if (len == -1) {
                break;
            }
            output.write(data, 0, len);
        }

        output.close();
        in.close();
        socket.close();
        serverSocket.close();
        file = new File("checksum_recv.txt");
        System.out.println("接收⽂件的md5为: " + MD5Util.getMD5(file));
    }
}

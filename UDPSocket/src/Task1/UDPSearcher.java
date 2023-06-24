package Task1;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class UDPSearcher {
    public static void main(String[] args) throws IOException {
        String sendData = "用户名admin, 密码123";
        byte[] sendBytes = sendData.getBytes(StandardCharsets.UTF_8);
        // 2. 创建发送者端的DatagramSocket对象
        DatagramSocket datagramSocket = new DatagramSocket(9092);
        // 3. 创建数据报，包含要发送的数据
        DatagramPacket sendPacket = new DatagramPacket(sendBytes, 0, sendBytes.length,
                InetAddress.getLocalHost(), 9091);

        // 4. 向接受者端发送数据报
        datagramSocket.send(sendPacket);
        System.out.println("数据发送完毕...");

        // Task 1 TODO: 准备接收Provider的回送消息; 查看接受信息并打印
        byte[] buf = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(buf, 0, buf.length);
        datagramSocket.receive(receivePacket);
        int len = receivePacket.getLength();
        String data = new String(receivePacket.getData(), 0, len);
        System.out.println("接收到回写信息: "+ data);

        // 5. 关闭datagramSocket
        datagramSocket.close();
    }
}

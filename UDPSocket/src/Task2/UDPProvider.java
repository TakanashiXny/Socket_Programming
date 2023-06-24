package Task2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class UDPProvider {
    public static void main(String[] args) throws IOException {
        // 1. 创建接受者端的DatagramSocket，并指定端⼝
        DatagramSocket datagramSocket = new DatagramSocket(9091);
        // 2. 创建数据报，⽤于接受客户端发来的数据
        byte[] buf = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(buf, 0, buf.length);

        System.out.println("阻塞等待发送者的消息...");
        datagramSocket.receive(receivePacket);

        String receiveMsg = new String(receivePacket.getData()).trim();
        int port = MessageUtil.parsePort(receiveMsg);

        String tag = UUID.randomUUID().toString();
        String sendMsg = MessageUtil.buildWithTag(tag);

        DatagramPacket sendPacket = new DatagramPacket(sendMsg.getBytes(StandardCharsets.UTF_8), 0,
                sendMsg.length(), InetAddress.getLocalHost(), port);

        datagramSocket.send(sendPacket);
        System.out.println("数据发送完毕...");

        // 5. 关闭datagramSocket
        datagramSocket.close();
    }
}

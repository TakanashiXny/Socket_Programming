package Task2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UDPSearcher {
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(30000);

        String builtPort = MessageUtil.buildWithPort(30000);
        DatagramPacket sendPacket = new DatagramPacket(builtPort.getBytes(StandardCharsets.UTF_8), 0,
                builtPort.length(), InetAddress.getLocalHost(), 9091);

        datagramSocket.send(sendPacket);
        System.out.println("数据发送完毕...");

        byte[] buf = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(buf, 0, buf.length);
        datagramSocket.receive(receivePacket);

        String receiveMsg = new String(receivePacket.getData()).trim();
        String tag = MessageUtil.parseTag(receiveMsg);

        System.out.println(tag);

        datagramSocket.close();
    }
}

package Task4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class NIOClient {
    private static int BYTE_LENGTH = 64;
    private Selector selector;

    public static void main(String[] args) {
        try {
            new NIOClient().StartClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void StartClient() throws IOException {
        SocketChannel channel = SocketChannel.open();

        channel.configureBlocking(false);

        channel.connect(new InetSocketAddress("127.0.0.1", 9091));

        this.selector = Selector.open();

        channel.register(this.selector, SelectionKey.OP_CONNECT);
        System.out.println("客户端连接成功");

        for (;;) {
            this.selector.select();
            Iterator iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();

                iterator.remove();

                if (key.isConnectable()) {
                    this.connect(key);
                } else if (key.isReadable()) {
                    this.read(key);
                } else if (key.isWritable()) {

                }
            }
        }
    }

    private void connect(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        if (channel.isConnectionPending()) {
            channel.finishConnect();
            ByteBuffer buffer = ByteBuffer.allocate(BYTE_LENGTH);
            buffer.put("Hello Server".getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            channel.write(buffer);
        }
        channel.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(BYTE_LENGTH);
        int numRead = channel.read(buffer);

        if (numRead > 0) {
            buffer.flip();
            System.out.println("客户端已收到消息：" + new String(buffer.array(), 0, numRead));
            channel.register(selector, SelectionKey.OP_WRITE);
        }
    }

}

package NIOServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOHttpServer {
    private static int BYTE_LENGTH = 8092;
    private static int DEFAULT_PORT = 8081;
    private String path;
    private Selector selector;

    public static void main(String[] args) {
        try {
            new NIOHttpServer().startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startServer() throws IOException {
        this.selector = Selector.open();
        // ServerSocketChannel与serverSocket类似
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(DEFAULT_PORT));
        // 设置⽆阻塞
        serverSocket.configureBlocking(false);
        // 将channel注册到selector
        serverSocket.register(this.selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务端已启动");
        for (;;) {
            // 操作系统提供的⾮阻塞I/O
            int readyCount = selector.select();
            if (readyCount == 0) {
                continue;
            }
            // 处理准备完成的fd
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                iterator.remove();
                if (!key.isValid()) {
                    continue;
                }
                if (key.isAcceptable()) {
                    this.accept(key);
                } else if (key.isReadable()) {
                    this.read(key);
                    // 将SelectionKey切换为写模式
                    key.interestOps(SelectionKey.OP_WRITE);
                } else if (key.isWritable()) {
                    this.write(key);
                    // 处理完响应操作，才能关掉客户端的SocketChannel
                    key.channel().close();
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        // 监听读事件
        channel.register(this.selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(BYTE_LENGTH);
        int numRead = -1;
        numRead = channel.read(buffer);
        if (numRead == -1) {
            channel.close();
            key.cancel();
            return;
        }
        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);
        String data1 = new String(data);
        String[] tmp = data1.split(" ");
        String currentPath = System.getProperty("user.dir");
        path = currentPath + "/src/NIOServer/content" + tmp[1];
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        channel.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(BYTE_LENGTH);
        String header = "";

        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            byte[] fileContent = new byte[(int) file.length()];
            fis.read(fileContent);
            fis.close();
            String content = new String(fileContent);
            header = make_response(content, true);
        } catch (FileNotFoundException e) {
            header = make_response("404 not found", false);
        }

        buffer.put(header.getBytes());
        buffer.flip(); // 将写模式转化为读模式
        channel.write(buffer);
    }

    private String make_response(String content, boolean found) {
        String HttpStatus = found ? "HTTP/1.1 200 OK\r\n" : "HTTP/1.1 404 NOT FOUND\r\n";
        String contentLength = "Content-Length: " + content.getBytes().length + "\r\n";
        String contentType = "Content-Type: text/html\r\n";
        return HttpStatus + contentType + contentLength + "\r\n" + content;
    }
}

package Proxy;

import java.io.*;
import java.net.Socket;


public class ProxyHandler extends Thread {
    private final InputStream clientReader;
    private InputStream serverReader;
    private final OutputStream clientWriter;
    private OutputStream serverWriter;
    private final int BUFFERSIZE = 8092;

    public ProxyHandler(Socket socket) throws IOException {
        serverWriter = null;
        serverReader = null;
        this.clientReader = socket.getInputStream();
        this.clientWriter = socket.getOutputStream();
    }

    @Override
    public void run() {
        try {
            StringBuilder header = new StringBuilder();

            String line;
            String host = null;
            String type;
            int port = 80; // HTTP默认端口
            BufferedReader ReadFromClient = new BufferedReader(new InputStreamReader(clientReader));

            while ((line = ReadFromClient.readLine()) != null) {
                System.out.println(line);
                header.append(line).append("\r\n");
                if (line.length() == 0) {
                    break;
                } else {
                    String[] tmp = line.split(" ");
                    if (tmp[0].contains("Host")) {
                        host = tmp[1];
                    }
                }
            }
            System.out.println("header: " + header);
            type = header.substring(0, header.indexOf(" "));
            String[] hostTemp = host.split(":");
            host = hostTemp[0];
            if (hostTemp.length > 1) {
                port = Integer.parseInt(hostTemp[1]);
            }

            Socket server = null;
            server = new Socket(host, port);

            serverReader = server.getInputStream();
            serverWriter = server.getOutputStream();

            serverWriter.write(header.toString().getBytes());
            serverWriter.flush();


            byte[] data = new byte[BUFFERSIZE];
            int len = 0;
            while ((len = serverReader.read(data)) > 0) {
                clientWriter.write(data, 0, len);
                clientWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverReader != null) {
                try {
                    serverReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (serverWriter != null) {
                try {
                    serverWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (clientWriter != null) {
                try {
                    clientWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (clientReader != null) {
                try {
                    clientReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
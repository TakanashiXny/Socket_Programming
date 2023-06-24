package Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Handler extends Thread {
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private String request;
    private String requestType;

    public Handler(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
    }

    private void read_request() throws IOException {
        String tmp;
        while ((tmp = bufferedReader.readLine()) != null && !tmp.isEmpty()) {
            System.out.println(tmp);
            if (tmp.split(" ")[1].equals("/shutdown")) {
                request = "shutdown";
                break;
            }
            if (tmp.contains("GET")) {
                requestType = "GET";
                request = tmp.split(" ")[1];
            }
            if (tmp.contains("POST")) {
                requestType = "POST";
                request = tmp.split(" ")[1];
            }
        }

    }

    private void file_request(String path) throws IOException {
        try {
            path = path.replace("http://localhost:8081", "");
            System.out.println(path);
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            byte[] fileContent = new byte[(int) file.length()];
            fis.read(fileContent);
            fis.close();
            String content = new String(fileContent);
            printWriter.println(make_response(content, true));
            printWriter.flush();
        } catch (FileNotFoundException e) {
            printWriter.println(make_response("404 not found", false));
        }
        if (printWriter != null) {
            printWriter.close();
        }
        if (bufferedReader != null) {
            bufferedReader.close();
        }
        if (socket != null) {
            socket.close();
        }
    }

    private String make_response(String content, boolean found) {
        String HttpStatus = found ? "HTTP/1.1 200 OK\r\n" : "HTTP/1.1 404 NOT FOUND\r\n";
        String contentLength = "Content-Length: " + content.getBytes().length + "\r\n";
        String contentType = "Content-Type: text/html\r\n";
        return HttpStatus + contentType + contentLength + "\r\n" + content;
    }

    @Override
    public void run() {
        try {

            read_request();

            if (request != null) {
                String requirement = request;
                if (requirement.equals("shutdown")) {
                    String a = "The server is going to be shut down!";
                    String HTTPStatus = "HTTP/1.1 200 OK\r\n";
                    String contentType = "Content-Type: text/html\r\n";
                    String contentLength = "Content-Length: " + a.getBytes().length + "\r\n";
                    printWriter.println(HTTPStatus + contentType + contentLength + "\r\n" + a);
                    System.exit(0);
                } else {
                    String path = System.getProperty("user.dir");
                    file_request(path + "/src/Server/content" + requirement);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

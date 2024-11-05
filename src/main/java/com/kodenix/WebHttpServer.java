package com.kodenix;

import java.net.*;
import java.io.*;

public class WebHttpServer {

    private ServerSocket serverSocket;

    public WebHttpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        System.out.println("Server started on port " + serverSocket.getLocalPort());
        while (true) {
            Socket clientSocket = createOpenClientConnectionSocket();
            handleConnectionInNewThread(clientSocket);
        }
    }

    private Socket createOpenClientConnectionSocket() throws IOException {
        return serverSocket.accept();
    }

    private void handleConnectionInNewThread(Socket clientSocket) {
        new Thread(() -> handleClient(clientSocket)).start();
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream())) {

            String line;
            while (!(line = in.readLine()).isEmpty()) {
                System.out.println(line);
            }

            out.println("HTTP/1.1 200 OK");
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        WebHttpServer server = new WebHttpServer(8080);
        server.start();
    }
}

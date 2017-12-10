package com.durafficpark;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class SomeRequest {
    public String text;
}
class SomeResponse {
    public String text;
}
public class Main {
    public static void main(String args[]) throws IOException {
        int PORT = 56428;
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();

        }
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // new thread for a client
            new SimulatorThread(socket).start();
        }
    }

    }

package com.durafficpark;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

public class SimulatorThread extends Thread {
    protected Socket socket;

    public SimulatorThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        System.out.println("Running");
        InputStream inp = null;
        BufferedReader brinp = null;
        DataOutputStream out = null;
        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        String input;
        while (true) {
            try {
                System.out.println("Now process inputs");
                input = brinp.readLine();
                System.out.println(input);
                // todo delete this, as I think this is making it worse
                if ((input == null)) {
                    socket.close();
                    return;
                } else {
                    String output = "stringtooutput";
                    String value = new String(output.getBytes("UTF-8"));
                    out.writeBytes(value);
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
 }
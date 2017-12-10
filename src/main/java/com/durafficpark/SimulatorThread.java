package com.durafficpark;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
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
        PrintWriter out = null;
        BufferedWriter bwrite = null;
        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        String input;
        while (true) {
            try {
                System.out.println("Now process inputs");
                input = brinp.readLine();
                System.out.println(input);
                HashMap<String, String> inputs = new Gson().fromJson(
                        input,
                        new TypeToken<HashMap<String, String>>(){}.getType()
                );
                System.out.println(input);
                // todo delete this, as I think this is making it worse
                    HashMap<String, String> output = new HashMap<>();
                    output.put("client", inputs.get("client"));

                    String outputStr = new Gson().toJson(output);

                    String value = new String(outputStr.getBytes("UTF-8"));
                    System.out.println("Output:" + value);
                    out.write(value);
                    out.flush();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
 }
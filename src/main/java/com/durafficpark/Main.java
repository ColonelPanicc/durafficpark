package com.durafficpark;

import com.corundumstudio.socketio.*;

public class Main {
    public static void main(String args[]) {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9000);

        SocketIOServer server = new SocketIOServer(config);

        server.addEventListener("sim-start", ProcessCommandObject.class,
                (client, command, ackRequest) -> {
                    String jsonSettings = command.getJSONInput();
                    System.out.println(jsonSettings);
                    System.out.println("Java Server Start Simulation");
                    client.sendEvent("test-return", jsonSettings);
                });

        try {
            server.start();
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}

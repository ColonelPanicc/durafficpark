package com.durafficpark;

import com.corundumstudio.socketio.*;

public class Main {

    public static void main(String args[]) {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(4000);

        SocketIOServer server = new SocketIOServer(config);

        server.addEventListener("start-sim", ProcessCommandObject.class,
                (client, command, ackRequest) -> {
                    String jsonSettings = command.getJSONInput();
                    System.out.println("Start simulation");
                });


        server.startAsync();

        try {
            server.startAsync();
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}

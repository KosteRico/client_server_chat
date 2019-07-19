package com.kosterico.server;

import com.kosterico.network.Connection;
import com.kosterico.network.ConnectionListener;
import com.kosterico.messages.AlertMessage;
import com.kosterico.messages.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server implements ConnectionListener {

    public static void main(String[] args) {
        new Server();
    }

    private final List<Connection> connections = new ArrayList<>();

    private Server() {
        System.out.println("Server is running ...");
        try (ServerSocket serverSocket = new ServerSocket(8000)) {

            while (true) {
                try {
                    new Connection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCP Connection exception: " + e);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(Connection connection) {
        connections.add(connection);
        sendAllConnections(new AlertMessage("Client connected: " + connection));
    }

    @Override
    public synchronized void onReceiveString(Connection connection, Message msg) {
        sendAllConnections(msg);
    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        connections.remove(connection);
        sendAllConnections(new AlertMessage("Client disconnected: " + connection));
    }

    @Override
    public synchronized void onException(Connection connection, Exception e) {
        System.out.println("TCP Connection exception: " + e);
    }

    private void sendAllConnections(Message msg) {
        System.out.println(msg.toString());
        for (Connection connection : connections) connection.sendMessage(msg);
    }

}

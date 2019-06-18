package com.kosterico.server;

import com.kosterico.network.TCPConnection;
import com.kosterico.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer implements TCPConnectionListener {

    public static void main(String[] args) {
        new ChatServer();
    }

    private final List<TCPConnection> connections = new ArrayList<>();

    private ChatServer() {
        System.out.println("Server is running ...");
        try (ServerSocket serverSocket = new ServerSocket(8000)) {

            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCP Connection exception: " + e);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection connection) {
        connections.add(connection);
        sendAllConnections("Client connected: " + connection.toString());
    }

    @Override
    public synchronized void onReceiveString(TCPConnection connection, String msg) {
        sendAllConnections(msg);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection connection) {
        connections.remove(connection);
        sendAllConnections("Client disconnected: " + connection.toString());
    }

    @Override
    public synchronized void onException(TCPConnection connection, Exception e) {
        System.out.println("TCP Connection exception: " + e);
    }

    private void sendAllConnections(String msg) {
        System.out.println(msg);
        for (TCPConnection connection : connections) connection.sendMessage(msg);
    }

}

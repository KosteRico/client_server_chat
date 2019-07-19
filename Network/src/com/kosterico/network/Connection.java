package com.kosterico.network;

import com.kosterico.messages.Message;

import java.io.*;
import java.net.Socket;

public class Connection {

    private final Socket socket;
    private final Thread thread;
    private final ObjectOutputStream objectWriter;
    private final ObjectInputStream objectReader;

    private final ConnectionListener eventListener;

    public Connection(ConnectionListener listener, String id, int port) throws IOException {
        this(listener, new Socket(id, port));
    }

    public Connection(ConnectionListener listener, Socket socket) throws IOException {
        this.socket = socket;
        eventListener = listener;

        objectWriter = new ObjectOutputStream(socket.getOutputStream());
        objectReader = new ObjectInputStream(socket.getInputStream());

        thread = new Thread(() -> {
            try {
                eventListener.onConnectionReady(this);
                while (!Thread.currentThread().isInterrupted()) {
                    listener.onReceiveString(Connection.this, (Message) objectReader.readObject());
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                eventListener.onDisconnect(this);
            }
        });
        thread.start();
    }

    public synchronized void sendMessage(Message msg) {
        try {
            objectWriter.writeObject(msg);
            objectWriter.flush();
        } catch (IOException e) {
            eventListener.onException(this, e);
            disconnect();
        }
    }

    private synchronized void disconnect() {
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(this, e);
        }
    }

    @Override
    public String toString() {
        return "TCP Connection: " + socket.getInetAddress() + ": " + socket.getPort();
    }
}

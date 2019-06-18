package com.kosterico.network;

import java.io.*;
import java.net.Socket;

public class TCPConnection {

    private final Socket socket;
    private final Thread thread;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    private final TCPConnectionListener eventListener;

    public TCPConnection(TCPConnectionListener listener, String id, int port) throws IOException {
        this(listener, new Socket(id, port));
    }

    public TCPConnection(TCPConnectionListener listener, Socket socket) throws IOException {
        this.socket = socket;
        eventListener = listener;

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        thread = new Thread(() -> {
            try {
                eventListener.onConnectionReady(this);
                while (!Thread.currentThread().isInterrupted()) {
                    listener.onReceiveString(TCPConnection.this, reader.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                eventListener.onDisconnect(this);
            }
        });
        thread.start();
    }

    public synchronized void sendMessage(String msg) {
        try {
            writer.write(msg + "\r\n");
            writer.flush();
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

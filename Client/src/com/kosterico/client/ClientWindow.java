package com.kosterico.client;

import com.kosterico.network.TCPConnection;
import com.kosterico.network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {

    private static final String IP = "localhost";
    private static final int PORT = 8000;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final String NICKNAME_DEFAULT = "**Nickname**";

    private final JTextArea log = new JTextArea();
    private final JTextField fieldNickname = new JTextField(NICKNAME_DEFAULT);
    private final JTextField fieldInput = new JTextField();

    private TCPConnection connection;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientWindow::new);
    }

    private ClientWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        log.setEditable(true);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);

        fieldInput.addActionListener(this);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldNickname, BorderLayout.NORTH);

        setVisible(true);

        try {
            connection = new TCPConnection(this, IP, PORT);
        } catch (IOException e) {
            printMessage("Connection exception: " + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
        if (msg.length() == 0) return;
        fieldInput.setText(null);
        connection.sendMessage(fieldNickname.getText() + ": " + msg);
    }

    @Override
    public void onConnectionReady(TCPConnection connection) {
        printMessage("Connection ready ...");
    }

    @Override
    public void onReceiveString(TCPConnection connection, String msg) {
        printMessage(msg);
    }

    @Override
    public void onDisconnect(TCPConnection connection) {
        printMessage("Connection close");
    }

    @Override
    public void onException(TCPConnection connection, Exception e) {
        printMessage("Connection exception: " + e);
    }

    private synchronized void printMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            log.append(msg + "\n");
            log.setCaretPosition(log.getDocument().getLength());
        });
    }

}

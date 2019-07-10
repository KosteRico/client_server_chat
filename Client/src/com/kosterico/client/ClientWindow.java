package com.kosterico.client;

import com.kosterico.network.Connection;
import com.kosterico.network.ConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener, ConnectionListener {

    private static final String IP = "localhost";
    private static final int PORT = 8000;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final String NICKNAME_DEFAULT = "**Nickname**";

    private final JTextArea log = new JTextArea();
    private final JTextField fieldNickname = new JTextField(NICKNAME_DEFAULT);
    private final JTextField fieldInput = new JTextField();

    private Connection connection;

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
            connection = new Connection(this, IP, PORT);
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
    public void onConnectionReady(Connection connection) {
        printMessage("Connection ready ...");
    }

    @Override
    public void onReceiveString(Connection connection, String msg) {
        printMessage(msg);
    }

    @Override
    public void onDisconnect(Connection connection) {
        printMessage("Connection close");
    }

    @Override
    public void onException(Connection connection, Exception e) {
        printMessage("Connection exception: " + e);
    }

    private synchronized void printMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            log.append(msg + "\n");
            log.setCaretPosition(log.getDocument().getLength());
        });
    }

}

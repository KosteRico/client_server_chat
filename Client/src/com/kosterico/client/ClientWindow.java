package com.kosterico.client;

import com.kosterico.audio.Recorder;
import com.kosterico.messages.VoiceMessage;
import com.kosterico.network.Connection;
import com.kosterico.network.ConnectionListener;
import com.kosterico.messages.AlertMessage;
import com.kosterico.text_area.ChatTextArea;
import com.kosterico.messages.Message;
import com.kosterico.messages.TextMessage;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ConnectionListener {

    private static final String IP = "localhost";
    private static final int PORT = 8000;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final String NICKNAME_DEFAULT = "**Nickname**";

    private final ChatTextArea chatField = new ChatTextArea();
    private final JTextField fieldNickname = new JTextField(NICKNAME_DEFAULT);
    private final JTextField fieldInput = new JTextField();
    private final JButton voiceMessageButton = new JButton();

    private static final String VOICE_MESSAGE_EMOJI = "\uD83D\uDDE3️";

    private Connection connection;

    private boolean isVoiceMessageButtonPressed;
    private Recorder recorder;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientWindow::new);
    }

    private ClientWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        chatField.setEditable(true);

        add(chatField, BorderLayout.CENTER);

        JPanel fieldInputPanel = new JPanel();
        fieldInputPanel.setLayout(new BoxLayout(fieldInputPanel, BoxLayout.X_AXIS));
        fieldInputPanel.add(fieldInput);
        fieldInputPanel.add(voiceMessageButton);
        try {
            ActionListener textInputListener = e -> {
                String msg = fieldInput.getText();
                if (msg.length() == 0) return;
                fieldInput.setText(null);
                TextMessage message = new TextMessage(msg, fieldNickname.getText());
                message.encrypt();
                System.out.println("Encrypted message: " + message.toString());
                connection.sendMessage(message);
            };

            ActionListener voiceMessageListener = e -> {
                try {
                    if (!isVoiceMessageButtonPressed) {
                        recorder = new Recorder();
                        new Thread(() -> {
                            try {
                                recorder.start();
                            } catch (LineUnavailableException | IOException ex) {
                                ex.printStackTrace();
                            }
                        }).start();

                    } else {
                        recorder.stop();
                        connection.sendMessage(new VoiceMessage(recorder.getPlayer(), fieldNickname.getText()));
                    }

                    isVoiceMessageButtonPressed = !isVoiceMessageButtonPressed;
                } catch (IOException ex) {
                    printMessage(new AlertMessage(ex.toString()));
                }

            };

            fieldInput.addActionListener(textInputListener);
            voiceMessageButton.setText(VOICE_MESSAGE_EMOJI);
            voiceMessageButton.setPreferredSize(new Dimension(50, fieldInput.getHeight()));
            voiceMessageButton.addActionListener(voiceMessageListener);
            add(fieldInputPanel, BorderLayout.SOUTH);

            add(fieldNickname, BorderLayout.NORTH);

            setVisible(true);

            connection = new Connection(this, IP, PORT);
        } catch (IOException e) {
            printMessage(new AlertMessage("Connection exception: " + e));
        }
    }

    @Override
    public void onConnectionReady(Connection connection) {
        printMessage(new AlertMessage("Connection ready ..."));
    }

    @Override
    public void onReceiveString(Connection connection, Message msg) {
        printMessage(msg);
    }

    @Override
    public void onDisconnect(Connection connection) {
        printMessage(new AlertMessage("Connection close"));
    }

    @Override
    public void onException(Connection connection, Exception e) {
        printMessage(new AlertMessage("Connection exception: " + e));
    }

    private synchronized void printMessage(Message msg) {
        SwingUtilities.invokeLater(() -> msg.sendClient(chatField));
    }

}

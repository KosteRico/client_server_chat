package com.kosterico.messages;

import com.kosterico.text_area.ChatTextArea;
import com.kosterico.audio.Player;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VoiceMessage extends UserMessage implements ActionListener {

    private final Player player;
    private JButton playButton;
    private static final String PLAY_SIGN = "\u25B6";

    public VoiceMessage(Player player, String user) {
        super(user);
        this.player = player;

        playButton = new JButton(PLAY_SIGN);
        playButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.play();
    }

    @Override
    public void sendClient(ChatTextArea textArea) {
        try {
            StyledDocument document = textArea.getStyledDocument();
            document.insertString(document.getLength(), user + ": Voice message ", null);
            textArea.setCaretPosition(textArea.getDocument().getLength());

            playButton.setPreferredSize(new Dimension(playButton.getWidth(), textArea.getFont().getSize() + 2));

            textArea.insertComponent(playButton);
            document.insertString(document.getLength(), "\n", null);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return user + ": Voice message (" + player + ")";
    }

}

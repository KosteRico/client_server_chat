package com.kosterico.text_area;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class ChatTextArea extends JTextPane {
    public ChatTextArea() {
        super();
    }

    public void appendText(String string) {
        try {
            StyledDocument document = getStyledDocument();
            document.insertString(document.getLength(), string, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}

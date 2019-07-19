package com.kosterico.messages;

import com.kosterico.text_area.ChatTextArea;

public class AlertMessage extends Message {

    private String message;

    public AlertMessage(String str) {
        message = str;
    }

    @Override
    public void sendClient(ChatTextArea textArea) {
        textArea.appendText(message + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    @Override
    public String toString() {
        return message;
    }
}

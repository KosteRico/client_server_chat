package com.kosterico.messages;

import com.kosterico.text_area.ChatTextArea;

public class TextMessage extends UserMessage {

    private String text;

    public TextMessage(String message, String user) {
        super(user);
        text = message;
    }

    @Override
    public void sendClient(ChatTextArea textArea) {
        textArea.appendText(toString() + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    @Override
    public String toString() {
        return user + ": " + text;
    }

}

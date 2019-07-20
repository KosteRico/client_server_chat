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
        decrypt();
        System.out.println("Decrypted message: " + toString());
        textArea.appendText(toString() + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    @Override
    public String toString() {
        return user + ": " + text;
    }

    public void encrypt() {
        int i = 1;
        char[] chars = text.toCharArray();

        for (int j = 0; j < chars.length / 2; j++) {
            chars[j] += i;
            i++;
        }

        for (int j = chars.length / 2; j < chars.length; j++) {
            chars[j] += i;
            i--;
        }

        text = String.copyValueOf(chars);
    }

    private void decrypt() {
        int i = 1;
        char[] chars = text.toCharArray();

        for (int j = 0; j < chars.length / 2; j++) {
            chars[j] -= i;
            i++;
        }

        for (int j = chars.length / 2; j < chars.length; j++) {
            chars[j] -= i;
            i--;
        }

        text = String.copyValueOf(chars);
    }

}

package com.kosterico.messages;

import com.kosterico.text_area.ChatTextArea;

import java.io.Serializable;

public abstract class Message implements Serializable {
    public abstract void sendClient(ChatTextArea textArea);

    public abstract String toString();

}
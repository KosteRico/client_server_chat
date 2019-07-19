package com.kosterico.messages;

public abstract class UserMessage extends Message {
    protected String user;

    public UserMessage(String user) {
        this.user = user;
    }

}

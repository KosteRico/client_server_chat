package com.kosterico.network;

import com.kosterico.messages.Message;

public interface ConnectionListener {
    void onConnectionReady(Connection connection);

    void onReceiveString(Connection connection, Message msg);

    void onDisconnect(Connection connection);

    void onException(Connection connection, Exception e);
}

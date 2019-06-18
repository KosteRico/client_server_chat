package com.kosterico.network;

public interface TCPConnectionListener {
    void onConnectionReady(TCPConnection connection);

    void onReceiveString(TCPConnection connection, String msg);

    void onDisconnect(TCPConnection connection);

    void onException(TCPConnection connection, Exception e);
}

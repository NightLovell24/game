package com.archers.multiplayer;

public interface MessageDispatcher {
    void dispatch(String message, String ip);
}

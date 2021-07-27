package com.github.jnidzwetzki.bitfinex.v2.exception;

public class SessionLostException extends RuntimeException {

    public SessionLostException() {
    }

    public SessionLostException(String message) {
        super(message);
    }
}
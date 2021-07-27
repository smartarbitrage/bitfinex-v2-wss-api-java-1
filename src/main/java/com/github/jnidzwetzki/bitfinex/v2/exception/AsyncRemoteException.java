package com.github.jnidzwetzki.bitfinex.v2.exception;

public class AsyncRemoteException extends RuntimeException {

    public AsyncRemoteException() {
    }

    public AsyncRemoteException(String message) {
        super(message);
    }
}


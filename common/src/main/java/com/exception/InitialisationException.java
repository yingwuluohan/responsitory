package com.exception;

/**
 * Created by fn on 2017/2/27.
 */
public class InitialisationException extends Exception {
    private static final long serialVersionUID = 1L;

    public InitialisationException() {
    }

    public InitialisationException(String s) {
        super(s);
    }

    public InitialisationException(Throwable cause) {
        super(cause);
    }

    public InitialisationException(String s, Throwable cause) {
        super(s, cause);
    }
}

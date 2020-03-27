package com.simple.bizFlow.core.exception;

/**
 * @author joooohnli  2020-03-20 6:39 PM
 */
public class FlowException extends RuntimeException {
    public FlowException() {
    }

    public FlowException(Throwable cause) {
        super(cause);
    }

    public FlowException(String message) {
        super(message);
    }

    public FlowException(String message, Throwable cause) {
        super(message, cause);
    }
}

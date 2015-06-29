package org.finasoft.metal.core;

/**
 * Special wrapper for exception caught during message sending
 */
public class SendingException extends Throwable {
    public SendingException(String message) {
        super( message);
    }
}

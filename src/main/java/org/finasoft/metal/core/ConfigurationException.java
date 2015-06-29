package org.finasoft.metal.core;

/**
 * Exception wrapper for configuration errors
 */
public class ConfigurationException extends Exception {
    public ConfigurationException( String message) {
        super( message);
    }
}

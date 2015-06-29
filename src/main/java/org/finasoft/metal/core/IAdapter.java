package org.finasoft.metal.core;

public interface IAdapter {
    public String getName();
    public void start() throws ConfigurationException;
    public void stop();
}

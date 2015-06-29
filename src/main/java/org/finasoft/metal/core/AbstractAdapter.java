package org.finasoft.metal.core;

/**
 * This is the mother base class for all adapters
 */
public abstract class AbstractAdapter {
    protected String name;

    protected AbstractAdapter(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}

package org.finasoft.metal.core;

/**
 * This is the mother base class for all adapters
 */
public abstract class Adapter {
    protected String name;

    Adapter( String name) {
        this.name = name;
    }

    public String getName() { return name; }
}

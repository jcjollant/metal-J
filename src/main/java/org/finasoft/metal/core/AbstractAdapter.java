package org.finasoft.metal.core;

/**
 * This is the mother base class for all adapters
 */
public abstract class AbstractAdapter {
    protected String name;
    protected AdapterStatus status;

    /**
     * Adapter status set to STOPPED
     * @param name
     */
    protected AbstractAdapter(String name) {
        this.name = name;
        status = AdapterStatus.STOPPED;
    }

    public String getName() { return name; }

    public void changeStatus( AdapterStatus newStatus) {
        this.status = newStatus;
    }
}

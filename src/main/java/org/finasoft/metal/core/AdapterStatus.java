package org.finasoft.metal.core;

/**
 * These are all possible statuses for an adapter
 * STOPPED means the Adapter has been created but not yet started or got disconnected
 * STARTED means the Adapter has been started but is not yet logged, the connection is in progress
 * CONNECTED means the Adapter connected physically (network) but not yet logged in
 * READY means the Adapters is logically connected
 */
public enum AdapterStatus {
    STOPPED, STARTED, CONNECTED, READY;
}

package org.finasoft.metal.core.normalization.values;

/**
 */
public enum Side {
    BUY("1"), SELL("2");

    private String value;

    Side( String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

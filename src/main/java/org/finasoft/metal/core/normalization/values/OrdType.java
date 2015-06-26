package org.finasoft.metal.core.normalization.values;

/**
 * Enum representing order types
 */
public enum OrdType {
    // TODO complete list
    Market("1"), Limit( "2"), Stop_StopLoss("3"), StopLimit("4");

    private String value;

    private OrdType( String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

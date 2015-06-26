package org.finasoft.metal.core.normalization;

/**
 * This list of fields follows FIX specification
 */
public enum Field {
    ClOrdID(11), OrderQty(38),
    OrdType(40), Price(44),
    Side(54), Symbol(55), TransactTime(60);

    private int number;

    Field( int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}

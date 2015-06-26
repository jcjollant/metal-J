package org.finasoft.metal.core.normalization;

/**
 * This class stores possible way to encode an instrument
 */
public class Instrument {

    private String symbol;

    private Instrument() {}

    public static Instrument fromSymbol(String symbol) {
        Instrument output = new Instrument();
        output.symbol = symbol;

        return output;
    }

    public void setFields(PseudoFIXMessage pfm) {
        if( symbol != null) {
            pfm.setField(Field.Symbol, symbol);
        }
    }
}

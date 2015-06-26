package org.finasoft.metal.core.normalization.message;

import org.finasoft.metal.core.normalization.Field;
import org.finasoft.metal.core.normalization.Instrument;
import org.finasoft.metal.core.normalization.OrderQtyData;
import org.finasoft.metal.core.normalization.PseudoFIXMessage;
import org.finasoft.metal.core.normalization.values.Side;
import org.finasoft.metal.core.normalization.values.OrdType;

/**
 * Normalized representation of a new order
 */
public class NewOrderSingle extends PseudoFIXMessage {
    /**
     * Constructor enforces mandatory fields
     * (!) Makes the assumption that tag 55 is sufficient to represent instrument
     */
    public NewOrderSingle( String clOrdId, Instrument instrument, Side side, String nowUTC, OrderQtyData orderQtyData, OrdType ordType) {
        setField( Field.ClOrdID, clOrdId);
        if( instrument != null) instrument.setFields(this);
        setField(Field.Side, side.getValue());
        if( orderQtyData != null) orderQtyData.encodeFields( this);
        setField(Field.TransactTime, nowUTC);
        setField(Field.OrdType, ordType.getValue());
    }
}

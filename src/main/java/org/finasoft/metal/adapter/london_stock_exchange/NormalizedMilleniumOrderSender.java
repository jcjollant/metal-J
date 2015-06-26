package org.finasoft.metal.adapter.london_stock_exchange;

import org.finasoft.metal.core.IOrderSenderObserver;
import org.finasoft.metal.core.normalization.IOrderSender;
import org.finasoft.metal.core.normalization.message.NewOrderSingle;

public class NormalizedMilleniumOrderSender implements IOrderSender {
    @Override
    public void send(NewOrderSingle nos) {
        // Create a native order
    }

    @Override
    public void registerObserver(IOrderSenderObserver orderSenderObserver) {

    }
}

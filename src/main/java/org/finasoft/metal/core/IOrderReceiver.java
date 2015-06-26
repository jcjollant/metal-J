package org.finasoft.metal.core;

import org.finasoft.metal.core.normalization.message.NewOrderSingle;

public interface IOrderReceiver {
    public void onNewOrderSingle( NewOrderSingle nos);
}

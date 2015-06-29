package org.finasoft.metal.core.normalization;

import org.finasoft.metal.core.IAdapter;
import org.finasoft.metal.core.IOrderSenderObserver;
import org.finasoft.metal.core.SendingException;
import org.finasoft.metal.core.normalization.message.NewOrderSingle;

/**
 * Order Senders can send New Order Single and receive Execution Reports
 */
public interface IOrderSender extends IAdapter {
    void send( NewOrderSingle nos) throws SendingException;

    void setObserver(IOrderSenderObserver orderSenderObserver);
}

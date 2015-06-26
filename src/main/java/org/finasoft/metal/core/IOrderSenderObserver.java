package org.finasoft.metal.core;

import org.finasoft.metal.core.normalization.message.ExecutionReport;

public interface IOrderSenderObserver {
    public void onExecutionReport( ExecutionReport er);
}

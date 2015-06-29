package org.finasoft.metal.example.command_line_trading;

import org.finasoft.metal.core.AdapterStatus;
import org.finasoft.metal.core.IOrderSenderObserver;
import org.finasoft.metal.core.normalization.message.ExecutionReport;

/**
 * this class processes incoming execution report ... by just printing the output
 */
public class CommandLineObserver implements IOrderSenderObserver {
    @Override
    public void onExecutionReport(ExecutionReport er) {
        System.out.println( "Execution Report received : " + er.toString());
    }

    @Override
    public void onStatusChange(AdapterStatus adapterStatus) {
        System.out.println( "Status has be changed to " + adapterStatus);
    }
}

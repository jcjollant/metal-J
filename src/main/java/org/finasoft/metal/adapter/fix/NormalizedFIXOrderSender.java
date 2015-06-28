package org.finasoft.metal.adapter.fix;

import org.finasoft.metal.core.IOrderSenderObserver;
import org.finasoft.metal.core.normalization.IOrderSender;
import org.finasoft.metal.core.normalization.message.NewOrderSingle;

/**
 * Adapter to FIX
 * Using QuickFIX as os for now
 */
public class NormalizedFIXOrderSender implements IOrderSender {
    public NormalizedFIXOrderSender() {

    }

    public void send(NewOrderSingle nos) {

    }

    public void setObserver(IOrderSenderObserver orderSenderObserver) {

    }

    public void start() {
        // FooApplication is your class that implements the Application interface
        Application application = new FooApplication();

        SessionSettings settings = new SessionSettings(new FileInputStream(fileName));
        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();
        Acceptor acceptor = new SocketAcceptor
                (application, storeFactory, settings, logFactory, messageFactory);
        acceptor.start();
        // while( condition == true ) { do something; }
        acceptor.stop();
    }

    public void stop() {

    }
}

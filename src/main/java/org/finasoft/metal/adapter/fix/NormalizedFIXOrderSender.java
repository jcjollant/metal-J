package org.finasoft.metal.adapter.fix;

import org.finasoft.metal.core.AbstractAdapter;
import org.finasoft.metal.core.ConfigurationException;
import org.finasoft.metal.core.IOrderSenderObserver;
import org.finasoft.metal.core.normalization.IOrderSender;
import org.finasoft.metal.core.normalization.message.NewOrderSingle;
import quickfix.*;

/**
 * Adapter to FIX
 * Using QuickFIX as os for now
 */
public class NormalizedFIXOrderSender extends AbstractAdapter implements IOrderSender {

    private IOrderSenderObserver observer;

    private Initiator quickfixInitiator;
    private SessionSettings quickfixSessionSettings;
    private SessionID quickfixSessionID;

    public NormalizedFIXOrderSender() {
        super( "FIX-Adapter");

        observer = null;

        quickfixInitiator = null;

        long portNumber = 1000;
        String beginString = "FIX.4.4";
        String host = "localhost";
        String senderCompID = "SCI";
        String targetCompID = "TCI";

        quickfixSessionSettings = new SessionSettings();

        quickfixSessionSettings.setString( SessionSettings.BEGINSTRING, beginString);
        quickfixSessionSettings.setString( DefaultSessionFactory.SETTING_CONNECTION_TYPE, DefaultSessionFactory.INITIATOR_CONNECTION_TYPE);

        quickfixSessionSettings.setLong(Initiator.SETTING_SOCKET_CONNECT_PORT, portNumber);
        quickfixSessionSettings.setString(Initiator.SETTING_SOCKET_CONNECT_HOST, host);

        quickfixSessionSettings.setString( SessionSettings.SENDERCOMPID, senderCompID);
        quickfixSessionSettings.setString( SessionSettings.TARGETCOMPID, targetCompID);

        quickfixSessionSettings.setString( "DataDictionary", "fix/FIX44.xml");
        quickfixSessionSettings.setString( "FileStorePath", "fix/store");

        quickfixSessionID = new SessionID( beginString, senderCompID, targetCompID);
    }

    public void send(NewOrderSingle nos) {

    }

    public void setObserver(IOrderSenderObserver orderSenderObserver) {
        observer = orderSenderObserver;
    }

    /**
     * Create a new quickFIX application and start its initiator
     * @throws ConfigurationException
     */
    public void start() throws ConfigurationException {
        Application application = new QuickFIXApplication();

        MessageStoreFactory storeFactory = new FileStoreFactory(quickfixSessionSettings);
//        LogFactory logFactory = new FileLogFactory(quickfixSessionSettings);
        MessageFactory messageFactory = new DefaultMessageFactory();
        try {
            quickfixInitiator = new SocketInitiator( application, storeFactory, quickfixSessionSettings, messageFactory);
            quickfixInitiator.start();
        } catch (ConfigError e) {
            throw new ConfigurationException( e.getMessage());
        }
    }

    public void stop() {
        if( quickfixInitiator != null) {
            quickfixInitiator.stop();
        }
    }

    private class QuickFIXApplication implements Application {
        @Override
        public void onCreate(SessionID sessionID) {

        }

        @Override
        public void onLogon(SessionID sessionID) {

        }

        @Override
        public void onLogout(SessionID sessionID) {

        }

        @Override
        public void toAdmin(Message message, SessionID sessionID) {

        }

        @Override
        public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {

        }

        @Override
        public void toApp(Message message, SessionID sessionID) throws DoNotSend {

        }

        @Override
        public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {

        }
    }
}

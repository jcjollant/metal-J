package org.finasoft.metal.adapter.fix;

import org.finasoft.metal.core.*;
import org.finasoft.metal.core.normalization.Field;
import org.finasoft.metal.core.normalization.IOrderSender;
import org.finasoft.metal.core.normalization.message.ExecutionReport;
import org.finasoft.metal.core.normalization.message.NewOrderSingle;
import quickfix.*;
import quickfix.field.BeginString;
import quickfix.field.MsgType;
import quickfix.field.SenderCompID;
import quickfix.field.TargetCompID;

import java.util.Iterator;

/**
 * Adapter to FIX
 * Using QuickFIX as os for now
 */
public class NormalizedFIXOrderSender extends AbstractAdapter implements IOrderSender {

    private IOrderSenderObserver observer;

    private Initiator quickfixInitiator;
    private SessionSettings quickfixSessionSettings;
    private Application quickfixApplication;
    private SessionID sessionID;

    private BeginString beginString;
    private SenderCompID senderCompID;
    private TargetCompID targetCompID;

    /**
     * Create and configure QuickFIX session with provided parameters
     * @param host
     * @param portNumber
     * @param senderCompID Will be used in conversation, must match counterparty expected CompID
     * @param targetCompID Will be used in all messages, must match counterparty CompID
     */
    public NormalizedFIXOrderSender( String host, long portNumber, String senderCompID, String targetCompID) {
        super( "FIX-Adapter");

        observer = null;

        quickfixInitiator = null;

        long heartBeatInterval = 30;
        this.beginString = new BeginString( "FIX.4.4");
        String startTime = "01:00:00 UTC";
        String endTime = "23:00:00 UTC";
        this.senderCompID = new SenderCompID( senderCompID);
        this.targetCompID = new TargetCompID( targetCompID);

        quickfixSessionSettings = new SessionSettings();

        sessionID = new SessionID( beginString, this.senderCompID, this.targetCompID);

        quickfixSessionSettings.setString(sessionID, SessionSettings.BEGINSTRING, beginString.getValue());
        quickfixSessionSettings.setString( sessionID, DefaultSessionFactory.SETTING_CONNECTION_TYPE, DefaultSessionFactory.INITIATOR_CONNECTION_TYPE);

        quickfixSessionSettings.setLong( sessionID, Initiator.SETTING_SOCKET_CONNECT_PORT, portNumber);
        quickfixSessionSettings.setString( sessionID, Initiator.SETTING_SOCKET_CONNECT_HOST, host);

        quickfixSessionSettings.setString( sessionID, SessionSettings.SENDERCOMPID, senderCompID);
        quickfixSessionSettings.setString( sessionID, SessionSettings.TARGETCOMPID, targetCompID);

        quickfixSessionSettings.setString( sessionID, "DataDictionary", "FIX44.xml");
        quickfixSessionSettings.setString( sessionID, "FileStorePath", "fix/store");

        quickfixSessionSettings.setLong( sessionID, Session.SETTING_HEARTBTINT, heartBeatInterval);

        quickfixSessionSettings.setString( sessionID, "StartTime", startTime);
        quickfixSessionSettings.setString(sessionID, "EndTime", endTime);

    }

    /**
     * Send a new order via quickfix application
     * @param nos
     */
    public void send(NewOrderSingle nos) throws SendingException {

        Message message = new Message();
        Message.Header header = message.getHeader();

        header.setField( beginString);
        header.setField( senderCompID);
        header.setField( targetCompID);
        header.setField(new MsgType("D"));

        // Copies fields values
        for(org.finasoft.metal.core.normalization.Field field : nos.getFields()) {
            message.setField( new StringField( field.getNumber(), nos.getField( field)));
        }

        try {
            Session.sendToTarget(message);
        } catch (SessionNotFound sessionNotFound) {
            throw new SendingException( sessionNotFound.getMessage());
        }
    }

    public void setObserver(IOrderSenderObserver orderSenderObserver) {
        observer = orderSenderObserver;
    }

    /**
     * Create a new quickFIX application and start its initiator
     * @throws ConfigurationException
     */
    public void start() throws ConfigurationException {
        quickfixApplication = new QuickFIXApplication();

        MessageStoreFactory storeFactory = new FileStoreFactory(quickfixSessionSettings);
//        LogFactory logFactory = new FileLogFactory(quickfixSessionSettings);
        quickfix.MessageFactory messageFactory = new DefaultMessageFactory();
        try {
            quickfixInitiator = new SocketInitiator(quickfixApplication, storeFactory, quickfixSessionSettings, messageFactory);
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
            changeStatus(AdapterStatus.READY);
            observer.onStatusChange( AdapterStatus.READY);
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
            // is this an execution report?
            if( "8".equals(message.getHeader().getString(35))) {
                ExecutionReport er = new ExecutionReport();

                Iterator<quickfix.Field<?>> iterator = message.iterator();

                while( iterator.hasNext()) {
                    quickfix.Field<?> quickfixField = iterator.next();
                    int tagNumber = quickfixField.getField();
                    Field field = Field.parseInt( tagNumber);
                    if( field != null) {
                        er.setField( field, message.getString( tagNumber));
                    }
                }

                // notify observer
                observer.onExecutionReport( er);
            }
        }
    }
}

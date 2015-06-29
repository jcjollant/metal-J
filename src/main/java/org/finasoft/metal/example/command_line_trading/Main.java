package org.finasoft.metal.example.command_line_trading;

import org.finasoft.metal.adapter.fix.NormalizedFIXOrderSender;
import org.finasoft.metal.core.ConfigurationException;
import org.finasoft.metal.core.SendingException;
import org.finasoft.metal.core.normalization.*;
import org.finasoft.metal.core.normalization.message.NewOrderSingle;
import org.finasoft.metal.core.normalization.values.OrdType;
import org.finasoft.metal.core.normalization.values.Side;

import java.util.Scanner;

/**
 * Command line trading sends orders from command line input
 */
public class Main {

    static TimeUtility timeUtility;

    public static void main( String []args) {
        Scanner scanner = new Scanner( System.in);
        String command;

        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        System.out.println("Java version = " + System.getProperty("java.version"));

        // Instantiate relevant adapter
        // These values are hardcoded to match our local instance of Quickfix Executor
        IOrderSender adapter = new NormalizedFIXOrderSender("localhost", 5001, "CLIENT1", "EXECUTOR");

        // register our listener
        adapter.setObserver(new CommandLineObserver());

        try {
            adapter.start();
        } catch (ConfigurationException e) {
            System.out.println( "Could not start adapter " + adapter.getName() + " because " + e.getMessage());
            return;
        }

        timeUtility = new TimeUtility();

        System.out.println( "---------------------------------------------------------------");
        System.out.println( " Please type in your commands then \"exit\" when you are done");
        System.out.println( " Orders should be entered using {Side} {Qty} {Symbol} [Price]");
        System.out.println( " If price is omitted, \"market\" is assumed");
        System.out.println( " Example: \"buy 10 BA\" => Buys 10 Boeing @ Market");
        System.out.println( " Example: \"sell 20 INTC 35 \" => Sells 10 Intel @ 35.0");
        System.out.println( "---------------------------------------------------------------");        
        do {
            System.out.print("> ");
            command = scanner.nextLine();
            try {
                NewOrderSingle nos = parseNOS(command);

                adapter.send(nos);
            } catch (ParsingException e) {
                System.out.println("Parsing failed because " + e.getMessage());
            } catch (SendingException e) {
                System.out.println("Sending failed because " + e.getMessage());
            }
        } while( !"exit".equals( command));
    }

    /**
     * Expecting something like "buy 10 AAPL 400" or "sell 20 INTC"
     * If price is missing then market order is sent
     * @param command
     * @return
     * @throws ParsingException
     */
    private static NewOrderSingle parseNOS(String command) throws ParsingException {
        if( command == null) throw new ParsingException( "Command may not be null");

        // Perform split and basic validation
        String tokens[] = command.split(" ");
        if( tokens.length < 3 || tokens.length > 4) throw new ParsingException("Invalid command length. Should be 3 or 4 words.");

        // Client Order ID : Generated from current time
        String clOrdId = String.valueOf(System.currentTimeMillis());

        // Instrument, 3rd position
        Instrument instrument = Instrument.fromSymbol( tokens[2]);

        // Side, 1st position
        // Can be any of buy, sell with any case
        Side side;
        if( "buy".equalsIgnoreCase(tokens[0])) {
            side = Side.BUY;
        } else if( "sell".equalsIgnoreCase(tokens[0])) {
            side = Side.SELL;
        }
        else throw new ParsingException( "Unknown Side. Should be \"buy\" or \"sell\" was " + tokens[0]);

        // Transacttime : now
        String nowUTC = timeUtility.nowUTC();

        // Quantity, 2nd position
        OrderQtyData orderQtyData = OrderQtyData.fromOrderQty(tokens[1]);

        // Price, 4th position
        OrdType ordType;
        String price;
        if( tokens.length == 3) {
            // Market Order
            ordType = OrdType.Market;
            price = null;
        } else {
            // Limit Order
            ordType = OrdType.Limit;
            price = tokens[3];
        }

        // Create new order single with mandatory fields
        NewOrderSingle nos = new NewOrderSingle( clOrdId, instrument, side, nowUTC, orderQtyData, ordType);

        // then add price if necessary
        if( price != null) {
            nos.setField( Field.Price, price);
        }

        return nos;
    }
}

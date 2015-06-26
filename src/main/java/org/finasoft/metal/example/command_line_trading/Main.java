package org.finasoft.metal.example.command_line_trading;

import org.finasoft.metal.adapter.london_stock_exchange.NormalizedMilleniumOrderSender;
import org.finasoft.metal.core.normalization.*;
import org.finasoft.metal.core.normalization.message.NewOrderSingle;
import org.finasoft.metal.core.normalization.values.OrdType;
import org.finasoft.metal.core.normalization.values.Side;

import java.util.Scanner;

/**
 * Command line trading sends orders from command line input
 */
public class Main {

    static Utility utility;

    public static void main( String []args) {
        Scanner scanner = new Scanner( System.in);
        String command;

        // Instantiate relevant adapter
        NormalizedMilleniumOrderSender adapter = new NormalizedMilleniumOrderSender();

        // register our listener
        adapter.registerObserver(new CommandLineObserver());

        utility = new Utility();

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

                System.out.println( "New Order : " + nos.toString() );
            } catch (ParsingException e) {
                System.out.println("Parsing failed because " + e.getMessage());
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
        Side side;
        if( "buy".equals(tokens[0])) {
            side = Side.BUY;
        } else if( "sell".equals(tokens[0])) {
            side = Side.SELL;
        }
        else throw new ParsingException( "Unknown Side. Should be \"buy\" or \"sell\" was " + tokens[0]);

        // Transacttime : now
        String nowUTC = utility.nowUTC();

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

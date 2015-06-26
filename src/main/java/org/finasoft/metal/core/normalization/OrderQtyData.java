package org.finasoft.metal.core.normalization;

/**
 * This class represents all possible ways to store an Order Qty
 * Except that for now it is only capable of storing orderQty
 */
public class OrderQtyData {
    private String orderQty;

    private OrderQtyData() {
        orderQty = null;
    }

    public static OrderQtyData fromOrderQty( String orderQty) {
        OrderQtyData output = new OrderQtyData();
        output.orderQty = orderQty;

        return output;
    }

    public void encodeFields( PseudoFIXMessage pfm) {
        if( this.orderQty != null) {
            pfm.setField( Field.OrderQty, orderQty);
        }
    }
}

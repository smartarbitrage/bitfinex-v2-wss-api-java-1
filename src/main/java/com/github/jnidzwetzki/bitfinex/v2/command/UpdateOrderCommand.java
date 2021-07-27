package com.github.jnidzwetzki.bitfinex.v2.command;

import com.github.jnidzwetzki.bitfinex.v2.BitfinexWebsocketClient;
import com.github.jnidzwetzki.bitfinex.v2.entity.BitfinexSubmittedOrder;
import com.github.jnidzwetzki.bitfinex.v2.exception.BitfinexCommandException;
import org.json.JSONObject;

public class UpdateOrderCommand implements BitfinexOrderCommand {

    private final BitfinexSubmittedOrder updateOrder;

    public UpdateOrderCommand(BitfinexSubmittedOrder updateOrder) {
        this.updateOrder = updateOrder;
    }

    @Override
    public String getCommand(BitfinexWebsocketClient client) throws BitfinexCommandException {
        final JSONObject updateJson = new JSONObject();
        updateJson.put("id", updateOrder.getOrderId());
        updateJson.put("amount", updateOrder.getAmount().toString());
        updateJson.put("price", updateOrder.getPrice().toString());


        final StringBuilder sb = new StringBuilder();
        sb.append("[0,\"ou\", null, ");
        sb.append(updateJson.toString());
        sb.append("]\n");

        return sb.toString();
    }
}
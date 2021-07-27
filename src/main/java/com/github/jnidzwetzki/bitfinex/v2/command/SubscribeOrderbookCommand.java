/*******************************************************************************
 *
 *    Copyright (C) 2015-2018 Jan Kristof Nidzwetzki
 *  
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License. 
 *    
 *******************************************************************************/
package com.github.jnidzwetzki.bitfinex.v2.command;

import org.json.JSONObject;

import com.github.jnidzwetzki.bitfinex.v2.BitfinexWebsocketClient;
import com.github.jnidzwetzki.bitfinex.v2.exception.BitfinexCommandException;
import com.github.jnidzwetzki.bitfinex.v2.symbol.BitfinexOrderBookSymbol;
import com.github.jnidzwetzki.bitfinex.v2.symbol.BitfinexStreamSymbol;

public class SubscribeOrderbookCommand implements SubscribeCommand {

	/**
	 * The orderbook configuration
	 */
	private BitfinexOrderBookSymbol symbol;
	
	public SubscribeOrderbookCommand(final BitfinexOrderBookSymbol symbol) {
		this.symbol = symbol;
	}

	@Override
	public String getCommand(final BitfinexWebsocketClient client) throws BitfinexCommandException {
		final JSONObject subscribeJson = new JSONObject();
		subscribeJson.put("event", "subscribe");
		subscribeJson.put("channel", "book");
		String currentSymbol = symbol.getCurrencyPair().toBitfinexString();
		if (currentSymbol.equalsIgnoreCase("tLINKUSD")){
			currentSymbol = "tLINK:USD";
		}
//		if (currentSymbol.equals("tBCHNUSD")){
//			currentSymbol = "BCHN:USD";
//		}
		subscribeJson.put("symbol", currentSymbol);
		subscribeJson.put("prec", symbol.getPrecision().toString());
		if (symbol.getFrequency() != null) {
			subscribeJson.put("freq", symbol.getFrequency().toString());
		}
		if (symbol.getPricePoints() != null) {
			subscribeJson.put("len", Integer.toString(symbol.getPricePoints()));
		}
		return subscribeJson.toString();
	}

	@Override
	public BitfinexStreamSymbol getSymbol() {
		return symbol;
	}
}

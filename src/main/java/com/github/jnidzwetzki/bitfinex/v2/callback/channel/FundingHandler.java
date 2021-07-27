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
package com.github.jnidzwetzki.bitfinex.v2.callback.channel;

import com.github.jnidzwetzki.bitfinex.v2.entity.BitfinexFundingInfo;
import com.github.jnidzwetzki.bitfinex.v2.entity.currency.BitfinexFundingCurrency;
import com.github.jnidzwetzki.bitfinex.v2.exception.BitfinexClientException;
import com.github.jnidzwetzki.bitfinex.v2.symbol.BitfinexAccountSymbol;
import com.github.jnidzwetzki.bitfinex.v2.symbol.BitfinexStreamSymbol;
import com.google.common.collect.Lists;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiConsumer;

public class FundingHandler implements ChannelCallbackHandler {

    private final static Logger logger = LoggerFactory.getLogger(FundingHandler.class);

    private final int channelId;
    private final BitfinexAccountSymbol symbol;

    private BiConsumer<BitfinexAccountSymbol, Collection<BitfinexFundingInfo>> fundingInfoConsumer = (sym, e) -> {};

    public FundingHandler(int channelId, BitfinexAccountSymbol symbol) {
        this.channelId = channelId;
        this.symbol = symbol;
    }

    @Override
    public void handleChannelData(final String action, final JSONArray fundingInfo) throws BitfinexClientException {
        logger.info("Got funding info callback {}", fundingInfo.toString());
        List<BitfinexFundingInfo> bitfinexFundingInfos = Lists.newArrayList();

//		if (fundingInfo.isEmpty()){
//			fundingInfoConsumer.accept(symbol, bitfinexFundingInfos);
//			return;
//		}

        //if (fundingInfo.get(2) instanceof JSONArray){
        String currency = fundingInfo.getString(1);
        logger.info("current currency - " + currency);
        final JSONArray jsonArray = fundingInfo.getJSONArray(2);
        BitfinexFundingInfo fundingInfoResult = jsonToFundingInfoEntry(jsonArray, currency);
        bitfinexFundingInfos.add(fundingInfoResult);
        //}

        logger.info("fundingInfoResult - " + fundingInfoResult);
        fundingInfoConsumer.accept(symbol, bitfinexFundingInfos);

    }

    @Override
    public BitfinexStreamSymbol getSymbol() {
        return symbol;
    }

    @Override
    public int getChannelId() {
        return channelId;
    }

    private BitfinexFundingInfo jsonToFundingInfoEntry(final JSONArray jsonArray, String currency) {

        final Double yieldLoan = jsonArray.getDouble(0);
        final Double yieldLend = jsonArray.getDouble(1);
        final Double durationLoan = jsonArray.getDouble(2);
        final Double durationLend = jsonArray.getDouble(3);

        return new BitfinexFundingInfo(yieldLoan, yieldLend, durationLoan, durationLend, BitfinexFundingCurrency.fromSymbolString(currency));
    }

    public void onFundingEvent(BiConsumer<BitfinexAccountSymbol, Collection<BitfinexFundingInfo>> consumer) {
        this.fundingInfoConsumer = consumer;
    }

}

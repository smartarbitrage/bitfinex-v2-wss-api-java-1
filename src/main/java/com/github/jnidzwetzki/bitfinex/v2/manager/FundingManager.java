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
package com.github.jnidzwetzki.bitfinex.v2.manager;

import com.github.jnidzwetzki.bitfinex.v2.BitfinexWebsocketClient;
import com.github.jnidzwetzki.bitfinex.v2.command.*;
import com.github.jnidzwetzki.bitfinex.v2.entity.BitfinexFundingInfo;
import com.github.jnidzwetzki.bitfinex.v2.entity.BitfinexOrderBookEntry;
import com.github.jnidzwetzki.bitfinex.v2.exception.BitfinexClientException;
import com.github.jnidzwetzki.bitfinex.v2.symbol.BitfinexAccountSymbol;
import com.github.jnidzwetzki.bitfinex.v2.symbol.BitfinexOrderBookSymbol;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FundingManager extends SimpleCallbackManager<BitfinexFundingInfo> {

    /**
     * The channel callbacks
     */
    //private final BiConsumerCallbackManager<BitfinexAccountSymbol, Object> channelCallbacks;
    //private final Table<String, String, BitfinexFundingInfo> fundingInfoTable;
    private final List<BitfinexFundingInfo> fundingInfos;
    private Consumer<BitfinexFundingInfo> fundingInfoConsumer;

    public FundingManager(final BitfinexWebsocketClient client, final ExecutorService executorService) {
        super(executorService, client);

        registerCallback(fundingInfoConsumer);
        //this.fundingInfoTable = HashBasedTable.create();
        this.fundingInfos = new ArrayList<>();

        client.getCallbacks().onFundingEvent((account, infos) -> fundingInfos.forEach(this::updateFundingInfo));

//        client.getCallbacks().onFundingEvent((account, infos) -> infos.forEach(info -> {
//			try {
//				System.out.println("funding event info - " + info.toString());
//				Table<String, String, BitfinexFundingInfo> fundingInfoTable = getFundingInfoMap();
//				synchronized (fundingInfoTable) {
//					fundingInfoTable.put("funding", info.getCurrency().getCurrency(), info);
//					fundingInfoTable.notifyAll();
//				}
//			} catch (BitfinexClientException e) {
//				e.printStackTrace();
//			}
//		}));
    }

    @Override
    public void registerCallback(Consumer<BitfinexFundingInfo> callback) {
        super.registerCallback(callback);
    }

    public void updateFundingInfo(final BitfinexFundingInfo info){
        synchronized (fundingInfos){
            fundingInfos.removeIf(f -> f.getCurrency() == info.getCurrency());
            fundingInfos.add(info);
            fundingInfos.notifyAll();
        }
        notifyCallbacks(info);
    }

//	public Collection<BitfinexFundingInfo> getFundingInfo() throws BitfinexClientException {
//		throwExceptionIfUnauthenticated();
//
//		synchronized (fundingInfoTable){
//			return Collections.unmodifiableCollection(fundingInfoTable.values());
//		}
//	}

    public List<BitfinexFundingInfo> getFundingInfos(){
        synchronized (fundingInfos){
            return fundingInfos;
        }
    }

//	public Table<String, String, BitfinexFundingInfo> getFundingInfoMap() throws BitfinexClientException {
//		return fundingInfoTable;
//	}

    public void calculateFundingInfo(final String symbol) throws BitfinexClientException {
        //throwExceptionIfUnauthenticated();

        client.sendCommand(new CalculateCommand("funding_sym_" + symbol));
    }

    private void throwExceptionIfUnauthenticated() throws BitfinexClientException {
        if(! client.isAuthenticated()) {
            throw new BitfinexClientException("Unable to perform operation on an unauthenticated connection");
        }
    }
}

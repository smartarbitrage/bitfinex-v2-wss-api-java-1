package com.github.jnidzwetzki.bitfinex.v2.entity;

import com.github.jnidzwetzki.bitfinex.v2.entity.currency.BitfinexFundingCurrency;

public class BitfinexFundingInfo {

    private final Double yieldLoan;
    private final Double yieldLend;
    private final Double durationLoan;
    private final Double durationLend;
    private final BitfinexFundingCurrency currency;

    public BitfinexFundingInfo(Double yieldLoan, Double yieldLend, Double durationLoan, Double durationLend, BitfinexFundingCurrency currency) {
        this.yieldLoan = yieldLoan;
        this.yieldLend = yieldLend;
        this.durationLoan = durationLoan;
        this.durationLend = durationLend;
        this.currency = currency;
    }

    public Double getYieldLoan() {
        return yieldLoan;
    }

    public Double getYieldLend() {
        return yieldLend;
    }

    public Double getDurationLoan() {
        return durationLoan;
    }

    public Double getDurationLend() {
        return durationLend;
    }

    public BitfinexFundingCurrency getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "BitfinexFundingInfo{" +
                "yieldLoan=" + yieldLoan +
                ", yieldLend=" + yieldLend +
                ", durationLoan=" + durationLoan +
                ", durationLend=" + durationLend +
                ", currency='" + currency + '\'' +
                '}';
    }
}


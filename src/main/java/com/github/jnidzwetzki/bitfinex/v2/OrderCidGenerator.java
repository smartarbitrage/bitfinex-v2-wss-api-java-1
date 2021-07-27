package com.github.jnidzwetzki.bitfinex.v2;

import java.util.Random;

public class OrderCidGenerator {

    public static final int SALT_VALUE = 100_000_000;

    public static long generateNewCid() {
        long currentTimestamp = System.currentTimeMillis();
        long dividedTimestamp = currentTimestamp / SALT_VALUE;
        long randomNumber = new Random().nextInt(100);
        return currentTimestamp - dividedTimestamp * SALT_VALUE + randomNumber * SALT_VALUE;
    }

}

package com.coinomi.core.coins;

import org.bitcoinj.core.Coin;

/**
 * @author John L. Jegutanis
 */
public class LitecoinTest extends CoinType {
    private LitecoinTest() {
        id = "litecoin.test";

        addressHeader = 111;
        p2shHeader = 196;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        spendableCoinbaseDepth = 100;

        name = "Litecoin Test";
        symbol = "LTC";
        uriScheme = "litecoin";
        bip44Index = 1;
        unitExponent = 8;
        feePerKb = Coin.valueOf(100000);
        minNonDust = Coin.valueOf(1000); // 0.00001 LTC mininput
        softDustLimit = Coin.valueOf(100000); // 0.001 LTC
        softDustPolicy = SoftDustPolicy.BASE_FEE_FOR_EACH_SOFT_DUST_TXO;
    }

    private static LitecoinTest instance = new LitecoinTest();
    public static synchronized LitecoinTest get() {
        return instance;
    }
}

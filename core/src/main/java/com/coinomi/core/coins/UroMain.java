package com.coinomi.core.coins;

import org.bitcoinj.core.Coin;

/**
 * @author FuzzyHobbit
 */
public class UroMain extends CoinType {
    private UroMain() {
        id = "uro.main";

        addressHeader = 68;
        p2shHeader = 5;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        spendableCoinbaseDepth = 20;

        name = "Uro";
        symbol = "URO";
        uriScheme = "uro";
        bip44Index = 17;
        unitExponent = 8;
        feePerKb = Coin.valueOf(100000);
        minNonDust = Coin.valueOf(1000); // 0.00001 URO mininput
        softDustLimit = Coin.valueOf(100000); // 0.001 URO
        softDustPolicy = SoftDustPolicy.BASE_FEE_FOR_EACH_SOFT_DUST_TXO;
    }

    private static UroMain instance = new UroMain();
    public static synchronized UroMain get() {
        return instance;
    }
}

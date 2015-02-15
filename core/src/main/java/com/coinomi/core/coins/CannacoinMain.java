package com.coinomi.core.coins;

import org.bitcoinj.core.Coin;

/**
 * @author FuzzyHobbit
 */
public class CannacoinMain extends CoinType {
    private CannacoinMain() {
        id = "cannacoin.main";

        addressHeader = 28;
        p2shHeader = 5;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        spendableCoinbaseDepth = 50;
        dumpedPrivateKeyHeader = 189;
        transactionVersion = 2;

        name = "Cannacoin";
        symbol = "CCN";
        uriScheme = "cannacoin";
        bip44Index = 19;
        unitExponent = 8;
        feePerKb = Coin.valueOf(100000);
        minNonDust = Coin.valueOf(1000000);
        softDustLimit = Coin.valueOf(100000000);
        softDustPolicy = SoftDustPolicy.BASE_FEE_FOR_EACH_SOFT_DUST_TXO;
    }

    private static CannacoinMain instance = new CannacoinMain();
    public static synchronized CannacoinMain get() {
        return instance;
    }
}

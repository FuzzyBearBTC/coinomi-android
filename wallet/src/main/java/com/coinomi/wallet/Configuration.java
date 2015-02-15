package com.coinomi.wallet;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.text.format.DateUtils;

import com.coinomi.core.coins.CoinID;
import com.coinomi.core.coins.CoinType;
import com.coinomi.wallet.util.WalletUtils;
import com.coinomi.wallet.ExchangeRatesProvider.ExchangeRate;

import org.bitcoinj.core.Coin;
import org.bitcoinj.utils.Fiat;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * @author John L. Jegutanis
 * @author Andreas Schildbach
 */
public class Configuration {

    public final int lastVersionCode;

    private final SharedPreferences prefs;

    private static final String PREFS_KEY_LAST_VERSION = "last_version";
    private static final String PREFS_KEY_LAST_USED = "last_used";
    public static final String PREFS_KEY_LAST_POCKET = "last_pocket";

    public static final String PREFS_KEY_BTC_PRECISION = "btc_precision";
    public static final String PREFS_KEY_CONNECTIVITY_NOTIFICATION = "connectivity_notification";
    public static final String PREFS_KEY_EXCHANGE_CURRENCY = "exchange_currency";
    public static final String PREFS_KEY_DISCLAIMER = "disclaimer";
    public static final String PREFS_KEY_SELECTED_ADDRESS = "selected_address";

    private static final String PREFS_KEY_LABS_QR_PAYMENT_REQUEST = "labs_qr_payment_request";

    private static final String PREFS_KEY_CACHED_EXCHANGE_LOCAL_CURRENCY = "cached_exchange_local_currency";
    private static final String PREFS_KEY_CACHED_EXCHANGE_RATES_JSON = "cached_exchange_rates_json";

    private static final String PREFS_KEY_LAST_EXCHANGE_DIRECTION = "last_exchange_direction";
    private static final String PREFS_KEY_CHANGE_LOG_VERSION = "change_log_version";
    public static final String PREFS_KEY_REMIND_BACKUP = "remind_backup";

    private static final int PREFS_DEFAULT_BTC_SHIFT = 3;
    private static final int PREFS_DEFAULT_BTC_PRECISION = 2;

    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    public Configuration(final SharedPreferences prefs) {
        this.prefs = prefs;

        this.lastVersionCode = prefs.getInt(PREFS_KEY_LAST_VERSION, 0);
    }

    public void updateLastVersionCode(final int currentVersionCode) {
        if (currentVersionCode != lastVersionCode) {
            prefs.edit().putInt(PREFS_KEY_LAST_VERSION, currentVersionCode).apply();
        }

        if (currentVersionCode > lastVersionCode)
            log.info("detected app upgrade: " + lastVersionCode + " -> " + currentVersionCode);
        else if (currentVersionCode < lastVersionCode)
            log.warn("detected app downgrade: " + lastVersionCode + " -> " + currentVersionCode);
    }

    public long getLastUsedAgo() {
        final long now = System.currentTimeMillis();

        return now - prefs.getLong(PREFS_KEY_LAST_USED, 0);
    }

    public void touchLastUsed() {
        final long prefsLastUsed = prefs.getLong(PREFS_KEY_LAST_USED, 0);
        final long now = System.currentTimeMillis();
        prefs.edit().putLong(PREFS_KEY_LAST_USED, now).apply();

        log.info("just being used - last used {} minutes ago", (now - prefsLastUsed) / DateUtils.MINUTE_IN_MILLIS);
    }

    public CoinType getLastPocket() {
        String coinId = prefs.getString(PREFS_KEY_LAST_POCKET, Constants.DEFAULT_COIN.getId());
        return CoinID.fromId(coinId).getCoinType();
    }

    public void touchLastPocket(CoinType type) {
        String lastId = prefs.getString(PREFS_KEY_LAST_POCKET, Constants.DEFAULT_COIN.getId());
        if (!lastId.equals(type.getId())) {
            prefs.edit().putString(PREFS_KEY_LAST_POCKET, type.getId()).apply();
            log.info("last used wallet pocket: {} ", type.getName());
        }
    }

    /**
     * Returns the user selected currency. If defaultFallback is set to true it return a default
     * currency is no user selected setting found.
     */
    @Nullable
    public String getExchangeCurrencyCode(boolean useDefaultFallback) {
        String defaultCode = null;
        if (useDefaultFallback) {
            defaultCode = WalletUtils.localeCurrencyCode();
            defaultCode = defaultCode == null ? Constants.DEFAULT_EXCHANGE_CURRENCY : defaultCode;
        }
        return prefs.getString(PREFS_KEY_EXCHANGE_CURRENCY, defaultCode);
    }

    /**
     * Returns the user selected currency or if not set the default
     */
    public String getExchangeCurrencyCode() {
        return getExchangeCurrencyCode(true);
    }

    public void setExchangeCurrencyCode(final String exchangeCurrencyCode) {
        prefs.edit().putString(PREFS_KEY_EXCHANGE_CURRENCY, exchangeCurrencyCode).apply();
    }

    public JSONObject getCachedExchangeRatesJson() {
        try {
            return new JSONObject(prefs.getString(PREFS_KEY_CACHED_EXCHANGE_RATES_JSON, ""));
        } catch (JSONException e) {
            return null;
        }
    }

    public String getCachedExchangeLocalCurrency() {
        return prefs.getString(PREFS_KEY_CACHED_EXCHANGE_LOCAL_CURRENCY, null);
    }

    public void setCachedExchangeRates(String currency, JSONObject exchangeRatesJson) {
        final SharedPreferences.Editor edit = prefs.edit();
        edit.putString(PREFS_KEY_CACHED_EXCHANGE_LOCAL_CURRENCY, currency);
        edit.putString(PREFS_KEY_CACHED_EXCHANGE_RATES_JSON, exchangeRatesJson.toString());
        edit.apply();
    }

    public void registerOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener listener) {
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener listener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public boolean getLastExchangeDirection() {
        return prefs.getBoolean(PREFS_KEY_LAST_EXCHANGE_DIRECTION, true);
    }

    public void setLastExchangeDirection(final boolean exchangeDirection) {
        prefs.edit().putBoolean(PREFS_KEY_LAST_EXCHANGE_DIRECTION, exchangeDirection).apply();
    }

}

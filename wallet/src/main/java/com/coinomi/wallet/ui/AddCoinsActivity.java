package com.coinomi.wallet.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

import com.coinomi.core.coins.CoinID;
import com.coinomi.core.coins.CoinType;
import com.coinomi.core.wallet.Wallet;
import com.coinomi.wallet.Constants;
import com.coinomi.wallet.R;

import org.spongycastle.crypto.params.KeyParameter;

import java.util.ArrayList;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

public class AddCoinsActivity extends AbstractWalletActionBarActivity
        implements SelectCoinsFragment.Listener, PasswordConfirmationFragment.Listener{

    @CheckForNull private Wallet wallet;
    private WalletFromSeedTask addCoinTask;
    private CoinType selectedCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coins);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SelectCoinsFragment())
                    .commit();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        wallet = getWalletApplication().getWallet();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onCoinSelection(Bundle args) {
        ArrayList<String> ids = args.getStringArrayList(Constants.ARG_MULTIPLE_COIN_IDS);

        // For new we add only one coin at a time
        this.selectedCoin = CoinID.typeFromId(ids.get(0));

        if (wallet.isPocketExists(this.selectedCoin)) {
            String message = getResources().getString(R.string.add_coin_accounts_error);
            Toast.makeText(AddCoinsActivity.this, message, Toast.LENGTH_LONG).show();
            return;
        }

        if (wallet.isEncrypted()) {
            String message = getResources().getString(
                    R.string.password_add_coin, this.selectedCoin.getName());
            replaceFragment(PasswordConfirmationFragment.newInstance(message));
        } else {
            addCoin(null);
        }
    }

    @Override
    public void onPasswordConfirmed(Bundle args) {
        addCoin(args.getString(Constants.ARG_PASSWORD));
    }


    private void addCoin(@Nullable String password) {
        if (selectedCoin != null && addCoinTask == null) {
            addCoinTask = new WalletFromSeedTask(selectedCoin, password);
            addCoinTask.execute();
        }
    }

    private class WalletFromSeedTask extends AsyncTask<Void, Void, Exception> {
        private final CoinType type;
        @Nullable private final String password;
        private Dialogs.ProgressDialogFragment verifyDialog;

        private WalletFromSeedTask(CoinType type, @Nullable String password) {
            this.type = type;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            verifyDialog = Dialogs.ProgressDialogFragment.newInstance(
                    getResources().getString(R.string.adding_coin, type.getName()));
            verifyDialog.show(getSupportFragmentManager(), null);
        }

        @Override
        protected Exception doInBackground(Void... params) {
            KeyParameter key = null;
            Exception exception = null;
            try {
                if (wallet.isEncrypted() && wallet.getKeyCrypter() != null) {
                    key = wallet.getKeyCrypter().deriveKey(password);
                }
                wallet.createCoinPocket(type, true, key);
                wallet.saveNow();
            } catch (RuntimeException e) {
                exception = e;
            }

            return exception;
        }

        protected void onPostExecute(Exception e) {
            verifyDialog.dismiss();
            result(e == null ? null : e.getMessage());
        }
    }

    public void result(@Nullable String errorMessage) {
        final Intent result = new Intent();
        if (errorMessage != null) {
            String message = getResources().getString(R.string.add_coin_error,
                    selectedCoin.getName(), errorMessage);
            Toast.makeText(AddCoinsActivity.this, message, Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED, result);
        } else {
            result.putExtra(Constants.ARG_COIN_ID, selectedCoin.getId());
            setResult(RESULT_OK, result);
        }

        finish();
    }
}

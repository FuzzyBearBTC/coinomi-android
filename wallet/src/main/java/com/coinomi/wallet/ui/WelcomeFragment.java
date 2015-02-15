package com.coinomi.wallet.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coinomi.wallet.R;
import com.coinomi.wallet.util.Fonts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 *
 */
public class WelcomeFragment extends Fragment {
    private static final Logger log = LoggerFactory.getLogger(WelcomeFragment.class);

    private Listener mListener;

    public WelcomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
//        Fonts.setTypeface(view.findViewById(R.id.welcome_text), Fonts.Font.ROBOTO_REGULAR);

        view.findViewById(R.id.create_wallet).setOnClickListener(getOnCreateListener());
        view.findViewById(R.id.restore_wallet).setOnClickListener(getOnRestoreListener());

        return view;
    }

    private View.OnClickListener getOnCreateListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.info("Clicked create new wallet");
                if (mListener != null) {
                    mListener.onCreateNewWallet();
                }
            }
        };
    }

    private View.OnClickListener getOnRestoreListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.info("Clicked restore wallet");
                if (mListener != null) {
                    mListener.onRestoreWallet();
                }
            }
        };
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface Listener {
        public void onCreateNewWallet();
        public void onRestoreWallet();
        public void onSeedCreated(String seed);
        public void onNewSeedVerified(String seed);
        public void onExistingSeedVerified(String seed, boolean isSeedProtected);
    }

}

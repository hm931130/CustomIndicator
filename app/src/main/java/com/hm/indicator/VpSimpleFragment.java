package com.hm.indicator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/9/8/008.
 */

public class VpSimpleFragment extends Fragment {


    private static final String BUNDLER_TITLE = "title";

    public static VpSimpleFragment getInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLER_TITLE, title);
        VpSimpleFragment simpleFragment = new VpSimpleFragment();
        simpleFragment.setArguments(bundle);
        return simpleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TextView textView = new TextView(getActivity());

        Bundle bundle = getArguments();
        if (bundle != null) {

            textView.setText(bundle.getString(BUNDLER_TITLE));
        }
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}

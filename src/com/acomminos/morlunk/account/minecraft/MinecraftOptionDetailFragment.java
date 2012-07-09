package com.acomminos.morlunk.account.minecraft;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acomminos.morlunk.R;
import com.acomminos.morlunk.dummy.DummyContent;

public class MinecraftOptionDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    DummyContent.DummyItem mItem;

    public MinecraftOptionDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_minecraftoption_detail, container, false);
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.minecraftoption_detail)).setText(mItem.content);
        }
        return rootView;
    }
}

package com.app.test.airbnb.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.test.airbnb.R;

import butterknife.ButterKnife;

/**
 * Created by Juan on 09/03/2017.
 */
public class MapFragment extends Fragment {

    public static final String TAG = MapFragment.class.getName();


    public static Fragment newInstace() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container);
        ButterKnife.bind(this, view);
        return view;
    }
}

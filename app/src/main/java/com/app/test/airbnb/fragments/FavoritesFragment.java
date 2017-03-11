package com.app.test.airbnb.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.test.airbnb.R;
import com.app.test.airbnb.activities.AccommodationActivity;
import com.app.test.airbnb.adapters.AccommodationsAdapter;
import com.app.test.airbnb.services.AccommodationService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juan on 09/03/2017.
 */
public class FavoritesFragment extends Fragment {

    public static final String TAG = FavoritesFragment.class.getName();


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private AccommodationsAdapter mAccommodationAdapter;

    public static Fragment newInstace() {
        FavoritesFragment fragment = new FavoritesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        mAccommodationAdapter = new AccommodationsAdapter(AccommodationService.getInstance().getFavoritesSavedAccomodations(),
                getContext(),
                mAccommodation -> AccommodationActivity.start(mAccommodation, getActivity()));
        mRecyclerView.setAdapter(mAccommodationAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}

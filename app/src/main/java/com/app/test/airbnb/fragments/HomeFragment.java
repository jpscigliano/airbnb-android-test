package com.app.test.airbnb.fragments;

import android.app.ProgressDialog;
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
public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getName();




    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private AccommodationsAdapter mAccommodationAdapter;

    private ProgressDialog progressDialog;
    public static Fragment newInstace() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.searching));
        progressDialog.show();

        AccommodationService.getInstance().searchAccomodations(mAccommodations -> {
            mAccommodationAdapter = new AccommodationsAdapter(mAccommodations, HomeFragment.this.getContext(), mAccommodation -> {
                progressDialog.setMessage(getString(R.string.loadingAccommodation));
                progressDialog.show();

                AccommodationService.getInstance().fetchAccomodationById(mAccommodation.getId(), mAccomodations -> {
                    AccommodationActivity.start(mAccommodation, getActivity());
                    progressDialog.dismiss();
                });

            });
            mRecyclerView.setAdapter(mAccommodationAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(HomeFragment.this.getContext()));
            progressDialog.dismiss();
        });
    }


}

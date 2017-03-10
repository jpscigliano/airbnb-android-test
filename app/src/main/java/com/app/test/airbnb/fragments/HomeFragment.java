package com.app.test.airbnb.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.test.airbnb.R;
import com.app.test.airbnb.injections.DaggerServiceComponent;
import com.app.test.airbnb.injections.ServiceComponent;
import com.app.test.airbnb.injections.ServiceModule;
import com.app.test.airbnb.model.Accomodation;
import com.app.test.airbnb.services.AccommodationService;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by Juan on 09/03/2017.
 */
public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getName();

    @Inject
    AccommodationService accommodationService;

    public static Fragment newInstace() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // region Dagger
        ServiceComponent component = DaggerServiceComponent.
                builder().
                serviceModule(new ServiceModule()).
                build();
        accommodationService = component.provideAccommodationService();
        //endregion

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

        accommodationService.searchAccomodations(new AccommodationService.SearchAccomodationListListener() {
            @Override
            public void onAccomodationListResult(ArrayList<Accomodation> mAccomodations) {

            }
        });
    }
}

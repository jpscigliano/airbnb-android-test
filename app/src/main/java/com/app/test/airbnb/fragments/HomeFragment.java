package com.app.test.airbnb.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.test.airbnb.R;
import com.app.test.airbnb.activities.AccommodationActivity;
import com.app.test.airbnb.adapters.AccommodationsAdapter;
import com.app.test.airbnb.models.Accommodation;
import com.app.test.airbnb.services.AccommodationService;
import com.app.test.airbnb.utils.PermissionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juan on 09/03/2017.
 */
public class HomeFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = HomeFragment.class.getName();

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private AccommodationsAdapter mAccommodationAdapter;
    private Location lastLocation;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest request;

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
        request = LocationRequest.create();
        request.setNumUpdates(1).setMaxWaitTime(30000).setFastestInterval(0);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        fetchLocationData();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        getDefaultAccommodations();
                    }
                })
                .addApi(LocationServices.API)
                .build();

        mAccommodationAdapter = new AccommodationsAdapter(getContext(), mAccommodation -> {

            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(getString(R.string.loadingAccommodation));
            progressDialog.show();
            AccommodationService.getInstance().fetchAccomodationById(mAccommodation.getId(), new AccommodationService.FetchAccommodationistener() {
                @Override
                public void onAccommodationResult(Accommodation mAccomodations) {
                    AccommodationActivity.start(mAccommodation, getActivity());
                    progressDialog.dismiss();
                }

                @Override
                public void onError() {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), R.string.error_accommodation, Toast.LENGTH_LONG).show();

                }

            });
        });
        mRecyclerView.setAdapter(mAccommodationAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                if (PermissionUtils.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getContext())) {
                    fetchLocationData();
                } else {
                    Toast.makeText(getContext(), "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();
                    getDefaultAccommodations();
                }
                break;

            }
        }
    }

    private void fetchLocationData() {

        if (PermissionUtils.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getContext())) {
            if (mGoogleApiClient.isConnected()) {

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, location -> {
                    if (lastLocation == null || lastLocation != location) {
                        lastLocation = location;
                        getAccomodationsAndUpdateList(location);

                    }

                });
            }
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PermissionUtils.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }


    private void getAccomodationsAndUpdateList(Location location) {

        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());

        try {
            List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses.size() > 0) {

                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setCancelable(false);
                progressDialog.setMessage(getString(R.string.searching));
                progressDialog.show();
                AccommodationService.getInstance().searchAccomodations(
                        addresses.get(0).getLocality(),
                        new AccommodationService.SearchAccommodationListListener() {
                            @Override
                            public void onAccommodationListResult(ArrayList<Accommodation> mAccommodations) {
                                mAccommodationAdapter.setmAccommodations(mAccommodations);
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onError() {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), R.string.error_accommodation, Toast.LENGTH_LONG).show();

                            }
                        });

            } else {
                Log.d(TAG, "Current location is null. Using defaults.");
                getDefaultAccommodations();
            }

        } catch (IOException e) {
            Log.d(TAG, "Current location is null. Using defaults.");
            getDefaultAccommodations();

        }

    }

    /**
     * Get Default Accommodations. In these case is set up to look for Los  Angeles.
     */
    public void getDefaultAccommodations() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.searching));
        progressDialog.show();


        AccommodationService.getInstance().searchAccomodations(new AccommodationService.SearchAccommodationListListener() {
            @Override
            public void onAccommodationListResult(ArrayList<Accommodation> mAccommodations) {
                mAccommodationAdapter.setmAccommodations(mAccommodations);

                progressDialog.dismiss();
            }

            @Override
            public void onError() {
                progressDialog.dismiss();
                Toast.makeText(getContext(), R.string.error_accommodation, Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        getDefaultAccommodations();
    }


    @Override
    public void onResume() {

        if (PermissionUtils.isLocationEnabled(getContext())) {
            if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
        } else {
            getDefaultAccommodations();
        }
        super.onResume();

    }

    @Override
    public void onPause() {
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
        super.onPause();

    }

    @Override
    public void onDestroy() {
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }
}

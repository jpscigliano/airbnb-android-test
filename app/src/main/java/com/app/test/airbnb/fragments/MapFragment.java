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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.test.airbnb.R;
import com.app.test.airbnb.activities.AccommodationActivity;
import com.app.test.airbnb.models.Accommodation;
import com.app.test.airbnb.services.AccommodationService;
import com.app.test.airbnb.utils.PermissionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juan on 09/03/2017.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {

    public static final String TAG = MapFragment.class.getName();


    @BindView(R.id.map)
    MapView mapView;
    GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    private float DEFAULT_ZOOM = 12;
    private HashMap<Marker, Integer> haspMap = new HashMap<>();
    private LocationRequest request;

    public static Fragment newInstace() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
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
                    }
                })
                .addApi(LocationServices.API)
                .build();

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(MapFragment.this);

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
                }
                break;

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMyLocationButtonClickListener(() -> {
            fetchLocationData();
            return true;
        });

        if (PermissionUtils.isLocationEnabled(getContext())) {
            if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
        } else {
            getDefaultAccommodationsOnMap(mMap);
        }

    }


    private void fetchLocationData() {

        if (PermissionUtils.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getContext())) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, location -> {
                    getAccommodationAndUpdateMap(mMap, location);
                });
            } else {
                mGoogleApiClient.connect();
            }


        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PermissionUtils.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }


    }

    private void getAccommodationAndUpdateMap(GoogleMap mMap, Location location) {
        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());

        try {
            List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM));

            if (addresses.size() > 0) {
                AccommodationService.getInstance().searchAccomodations(addresses.get(0).getLocality(),
                        new AccommodationService.SearchAccommodationListListener() {
                            @Override
                            public void onAccommodationListResult(ArrayList<Accommodation> mAccommodations) {
                                createMarkersOnMap(mAccommodations);                            }

                            @Override
                            public void onError() {
                                Toast.makeText(getContext(), R.string.error_accommodation, Toast.LENGTH_LONG).show();
                            }
                        }
                );

            } else {
                getDefaultAccommodationsOnMap(mMap);
            }

        } catch (IOException e) {
            Log.d(TAG, "Current location is null. Using defaults.");
            getDefaultAccommodationsOnMap(mMap);

        }

    }

    private void getDefaultAccommodationsOnMap(GoogleMap mMap) {
        AccommodationService.getInstance().searchAccomodations(new AccommodationService.SearchAccommodationListListener() {
            @Override
            public void onAccommodationListResult(ArrayList<Accommodation> mAccommodations) {
                createMarkersOnMap(mAccommodations);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mAccommodations.get(0).getLatitude(), mAccommodations.get(0).getLongitude()), DEFAULT_ZOOM));

            }

            @Override
            public void onError() {
                Toast.makeText(getContext(), R.string.error_accommodation, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void createMarkersOnMap(ArrayList<Accommodation> mAccommodations) {
        mMap.clear();
        haspMap.clear();
        IconGenerator iconFactory = new IconGenerator(getContext());
        iconFactory.setStyle(IconGenerator.STYLE_GREEN);

        for (Accommodation accommodation : mAccommodations) {

            Marker mMarker = mMap.addMarker(new MarkerOptions().
                    icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(accommodation.getPrice() + " " + accommodation.getCurrency()))).
                    position(new LatLng(accommodation.getLatitude(), accommodation.getLongitude())).
                    anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV()));
            haspMap.put(mMarker, accommodation.getId());

        }

    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        int accommodationId = haspMap.get(marker);

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

        progressDialog.setMessage(getString(R.string.loadingAccommodation));
        progressDialog.show();

        AccommodationService.getInstance().fetchAccomodationById(accommodationId, new AccommodationService.FetchAccommodationistener() {
            @Override
            public void onAccommodationResult(Accommodation mAccomodation) {
                AccommodationActivity.start(mAccomodation, MapFragment.this.getActivity());
                progressDialog.dismiss();
            }
            @Override
            public void onError() {
                progressDialog.dismiss();
                Toast.makeText(getContext(), R.string.error_accommodation, Toast.LENGTH_LONG).show();

            }
        });

        return false;
    }

}

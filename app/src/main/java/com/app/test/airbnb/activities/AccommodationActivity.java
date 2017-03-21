package com.app.test.airbnb.activities;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.test.airbnb.R;
import com.app.test.airbnb.geofence.GeofenceErrorMessages;
import com.app.test.airbnb.geofence.GeofenceTransitionsIntentService;
import com.app.test.airbnb.models.Accommodation;
import com.app.test.airbnb.services.AccommodationService;
import com.app.test.airbnb.utils.PermissionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccommodationActivity extends AppCompatActivity implements OnMapReadyCallback, ResultCallback<Status> {
    private int accommodationId;

    private final int EXPIRATION_TIME = 12 * 60 * 60 * 1000; //(12 horas)
    private final int GEOFENCE_RADIUS = 1200;// (casi 1 km)

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.addFavorite)
    FloatingActionButton favorites;
    @BindView(R.id.imageView)
    ImageView image;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.subttitle)
    TextView subTitle;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.bedrooms)
    TextView bedrooms;
    @BindView(R.id.beds)
    TextView beds;
    @BindView(R.id.bathroom)
    TextView bathroom;
    @BindView(R.id.guest)
    TextView guest;
    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.map)
    MapView map;

    Accommodation mAccommodation;
    private GoogleApiClient mGoogleApiClient;
    private PendingIntent mGeofencePendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation);
        ButterKnife.bind(this);
        setToolbar();
        requestPermision();
        accommodationId = getIntent().getExtras().getInt(Accommodation.KEY);

        buildGoogleApiClient();

        mAccommodation = AccommodationService.getInstance().getSavedAccommodation(accommodationId);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);

        if (mAccommodation.isFavorite()) {
            favorites.setImageResource(R.mipmap.ic_favorite_white);
        } else {
            favorites.setImageResource((R.mipmap.ic_favorite_border_white));
        }

        favorites.setOnClickListener(view -> {

            if (!mAccommodation.isFavorite()) {
                favorites.setImageResource(R.mipmap.ic_favorite_white);
                Snackbar.make(view, R.string.addTofavorite, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                addGeoFence(mAccommodation.getId(), mAccommodation.getLatitude(), mAccommodation.getLongitude());

            } else {
                favorites.setImageResource((R.mipmap.ic_favorite_border_white));
                Snackbar.make(view, R.string.removeTofavorite, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                removeGeoFence(mAccommodation.getId());
            }

            AccommodationService.getInstance().saveOrUpdateFavoriteAccommodation(mAccommodation, !mAccommodation.isFavorite());

        });

        Picasso.with(this)
                .load(mAccommodation.getImage())
                .into(image);

        title.setText(mAccommodation.getName());
        subTitle.setText(mAccommodation.getPropertyType());
        price.setText("" + mAccommodation.getPrice() + " " + mAccommodation.getCurrency());
        beds.setText("" + mAccommodation.getBeds());
        bathroom.setText("" + mAccommodation.getBathroom());
        guest.setText("" + mAccommodation.getGuest());
        bedrooms.setText("" + mAccommodation.getBedrooms());
        description.setText(mAccommodation.getDescription());

    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    public static void start(Accommodation mAccommodation, Activity activity) {
        Intent intent = new Intent(activity, AccommodationActivity.class);
        Bundle extras = new Bundle();
        extras.putInt(Accommodation.KEY, mAccommodation.getId());
        intent.putExtras(extras);
        activity.startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng accommodatioLoc = new LatLng(mAccommodation.getLatitude(), mAccommodation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(accommodatioLoc)
                .title(mAccommodation.getName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(accommodatioLoc));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        googleMap.animateCamera(zoom);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {

                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(LocationServices.API)
                .build();
    }

    public void addGeoFence(int id, double latitude, double longitude) {

        Geofence mGeofence = new Geofence.Builder()
                .setRequestId("" + id)
                .setCircularRegion(latitude, longitude, GEOFENCE_RADIUS)
                .setExpirationDuration(EXPIRATION_TIME)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.GeofencingApi.addGeofences(
                mGoogleApiClient,
                getGeofencingRequest(mGeofence),
                getGeofencePendingIntent()
        ).setResultCallback(status -> {
            if (status.isSuccess()) {

                Toast.makeText(
                        this,
                        "add Geofence",
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                // Get the status code for the error and log it using a user-friendly message.
                String errorMessage = GeofenceErrorMessages.getErrorString(this,
                        status.getStatusCode());
                Log.e(GeofenceTransitionsIntentService.TAG, errorMessage);
            }
        });

    }
    private void removeGeoFence(int id) {
        ArrayList ids=new ArrayList<>();
        ids.add(String.valueOf(id));
        LocationServices.GeofencingApi.removeGeofences(
                mGoogleApiClient,
                ids
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {

                    Toast.makeText(
                            AccommodationActivity.this,
                            "remove Geofence",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    // Get the status code for the error and log it using a user-friendly message.
                    String errorMessage = GeofenceErrorMessages.getErrorString(AccommodationActivity.this,
                            status.getStatusCode());
                    Log.e(GeofenceTransitionsIntentService.TAG, errorMessage);
                }
            }
        });
    }

    private GeofencingRequest getGeofencingRequest(Geofence mGeofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        builder.addGeofence(mGeofence);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {

        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {

            Toast.makeText(
                    this,
                    "add/remove Geofence",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
            Log.e(GeofenceTransitionsIntentService.TAG, errorMessage);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                if (PermissionUtils.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, this)) {

                } else {
                    Toast.makeText(this, getString(R.string.error_permission_location) + " " + getString(R.string.error_geofence), Toast.LENGTH_LONG).show();

                }
                break;

            }
        }
    }

    public void requestPermision() {
        if (!PermissionUtils.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, this)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PermissionUtils.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }
    }
}

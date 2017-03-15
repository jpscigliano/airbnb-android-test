package com.app.test.airbnb.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.test.airbnb.R;
import com.app.test.airbnb.models.Accommodation;
import com.app.test.airbnb.services.AccommodationService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccommodationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private int accommodationId;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation);
        ButterKnife.bind(this);
        setToolbar();

        accommodationId = getIntent().getExtras().getInt(Accommodation.KEY);

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
            } else {
                favorites.setImageResource((R.mipmap.ic_favorite_border_white));
                Snackbar.make(view, R.string.removeTofavorite, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }
}

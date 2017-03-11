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
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccommodationActivity extends AppCompatActivity {
    private int accommodationId;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.addFavorite)
    FloatingActionButton favorites;
    @BindView(R.id.imageView)
    ImageView image;

    @BindView(R.id.title)
    TextView title ;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation);
        ButterKnife.bind(this);
        setToolbar();

        accommodationId = getIntent().getExtras().getInt(Accommodation.KEY);

        Accommodation mAccommodation = AccommodationService.getInstance().getSavedAccommodation(accommodationId);

        if(mAccommodation.isFavorite()){
            favorites.setImageResource(R.drawable.ic_menu_favorites);
        }else{
            favorites.setImageResource(R.drawable.ic_menu_favorites_border);
        }

        favorites.setOnClickListener(view -> {

          if(!mAccommodation.isFavorite()){
              favorites.setImageResource(R.drawable.ic_menu_favorites);
              Snackbar.make(view, R.string.addTofavorite, Snackbar.LENGTH_LONG)
                      .setAction("Action", null).show();
          }else{
              favorites.setImageResource(R.drawable.ic_menu_favorites_border);
              Snackbar.make(view, R.string.removeTofavorite, Snackbar.LENGTH_LONG)
                      .setAction("Action", null).show();
          }

            AccommodationService.getInstance().saveFavoriteAccommodation(mAccommodation,!mAccommodation.isFavorite());

        });

        Picasso.with(this)
                .load(mAccommodation.getImage())
                .into(image);

        title.setText(mAccommodation.getName());
        subTitle.setText(mAccommodation.getPropertyType());
        price.setText(""+mAccommodation.getPrice()+" "+mAccommodation.getCurrency());
        beds.setText(""+mAccommodation.getBeds());
        bathroom.setText(""+mAccommodation.getBathroom());
        guest.setText(""+mAccommodation.getGuest());
        bedrooms.setText(""+mAccommodation.getBedrooms());
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
}

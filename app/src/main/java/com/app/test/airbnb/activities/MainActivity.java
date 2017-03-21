package com.app.test.airbnb.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.test.airbnb.R;
import com.app.test.airbnb.activities.base.BaseAppCompatActivity;
import com.app.test.airbnb.activities.base.NavigationCallback;
import com.app.test.airbnb.fragments.FavoritesFragment;
import com.app.test.airbnb.fragments.HomeFragment;
import com.app.test.airbnb.fragments.MapFragment;
import com.app.test.airbnb.models.Accommodation;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationCallback {

    public static final String COME_FROM_PUSH = "come_from_push";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;


    ImageView imageViewUser;
    TextView nameUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getBoolean(COME_FROM_PUSH, false)) {
                AccommodationActivity.start(new Accommodation(getIntent().getExtras().getInt(Accommodation.KEY)), this);
            }
        }


        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        goToHome();
        setupUserLoginUI();
    }

    private void setupUserLoginUI() {
        imageViewUser = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageViewHeader);
        nameUser = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nameHeader);
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            Picasso.with(this).load(profile.getProfilePictureUri(100, 100)).into(imageViewUser);
            nameUser.setText(profile.getFirstName() + " " + profile.getLastName());
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home && !isFragmentVisible(HomeFragment.TAG)) {
            goToHome();
        } else if (id == R.id.nav_map && !isFragmentVisible(MapFragment.TAG)) {
            goToMap();
        } else if (id == R.id.nav_favorites && !isFragmentVisible(FavoritesFragment.TAG)) {
            goToFavorites();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isFragmentVisible(String tag) {
        Fragment fg = getSupportFragmentManager().findFragmentByTag(tag);
        if (fg != null && fg.isVisible()) {
            return true;
        } else {
            return false;
        }
    }


    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    @Override
    public void goToHome() {
        setTitle(R.string.home);
        start(HomeFragment.newInstace(), false, R.id.content_frame, HomeFragment.TAG);
    }

    @Override
    public void goToFavorites() {

        setTitle(R.string.favorites);
        start(FavoritesFragment.newInstace(), false, R.id.content_frame, FavoritesFragment.TAG);
    }

    @Override
    public void goToMap() {
        setTitle(R.string.map);
        start(MapFragment.newInstace(), false, R.id.content_frame, MapFragment.TAG);
    }
}

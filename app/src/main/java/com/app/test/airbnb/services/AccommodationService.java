package com.app.test.airbnb.services;

import android.util.Log;

import com.app.test.airbnb.models.Accommodation;
import com.app.test.airbnb.services.response.SearchDataResponse;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Juan on 09/03/2017.
 */


public class AccommodationService extends BaseService {

    public AccommodationApi mApi;
    private final static String cliente_id = "3092nxybyb0otqw18e8nh5nty";
    private final static String formatListing = "v1_legacy_for_p3";
    private Realm realm = Realm.getInstance(new RealmConfiguration.Builder().build());

    public interface SearchAccomodationListListener {
        void onAccomodationListResult(ArrayList<Accommodation> mAccommodations);
    }

    public interface FetchAccomodationistener {
        void onAccomodationResult(Accommodation mAccomodations);
    }

    @Inject
    public AccommodationService() {
        super();
        mApi = buildApi(AccommodationApi.class);
    }


    public void saveAccomodations(ArrayList<Accommodation> accommodations) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(accommodations);
        realm.commitTransaction();
    }

    public void deleteSavedAccomodations() {
        realm.beginTransaction();
        realm.delete(Accommodation.class);
        realm.commitTransaction();

    }

    public ArrayList<Accommodation> getSavedAccomodations() {
        realm.beginTransaction();
        RealmResults<Accommodation> realmResult = realm.where(Accommodation.class).findAll();
        realmResult.toArray();
        ArrayList<Accommodation> accommodations = new ArrayList<>();
        accommodations.addAll(realm.copyFromRealm(realmResult));
        realm.commitTransaction();
        return accommodations;
    }

    public void saveAccommodation(Accommodation accommodation) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(accommodation);
        realm.beginTransaction();
    }

    public void deleteSavedAccommodation(Accommodation accommodation) {
        realm.beginTransaction();
        realm.where(Accommodation.class).equalTo("id", accommodation.getId()).findFirst().deleteFromRealm();
        realm.commitTransaction();
    }

    public Accommodation getSavedAccommodation(int accomodationId) {
        Accommodation mAccommodation;
        realm.beginTransaction();
        mAccommodation = realm.where(Accommodation.class).equalTo("id", accomodationId).findFirst();
        realm.commitTransaction();
        return mAccommodation;
    }

    public void searchAccomodations(final SearchAccomodationListListener listener) {


        mApi.getAccommodationsByClientId(cliente_id, "Buenos Aires", null, null, 30).
                subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(response -> {

                    ArrayList<Accommodation> mAccomodations = new ArrayList<>();
                    for (SearchDataResponse searchresult : response.searchData) {
                        Accommodation mAccomodation = new Accommodation(searchresult.listingData);
                        mAccomodation.setPrice(searchresult.pricingData.price);
                        mAccomodation.setCurrency(searchresult.pricingData.currency);
                        mAccomodations.add(mAccomodation);
                    }
                    saveAccomodations(mAccomodations);
                    listener.onAccomodationListResult(mAccomodations);
                }, throwable -> {

                    Log.d("Response", "Response Error: " + throwable.toString());
                });
    }

    public void fetchAccomodationById(int id, FetchAccomodationistener listener) {
        mApi.getAccommodationById(id, cliente_id, formatListing).
                subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(response -> {
                    Accommodation mAccommodation = new Accommodation(response);
                    saveAccommodation(mAccommodation);
                    listener.onAccomodationResult(mAccommodation);
                }, throwable -> {
                    Log.d("Response", "Response Error: " + throwable.toString());
                });

    }
}

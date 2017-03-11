package com.app.test.airbnb.services;

import android.util.Log;

import com.app.test.airbnb.models.Accommodation;
import com.app.test.airbnb.services.response.SearchDataResponse;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Juan on 09/03/2017.
 */


public class AccommodationService extends BaseService {

    private static AccommodationService singleton;

    public AccommodationApi mApi;
    private final static String cliente_id = "3092nxybyb0otqw18e8nh5nty";
    private final static String formatListing = "v1_legacy_for_p3";
    private Realm realm;


    public interface SearchAccommodationListListener {
        void onAccommodationListResult(ArrayList<Accommodation> mAccommodations);
    }

    public interface FetchAccommodationistener {
        void onAccommodationResult(Accommodation mAccomodations);
    }

    public static AccommodationService getInstance() {
        if (singleton == null) {
            singleton = new AccommodationService();
        }
        return singleton;
    }

    private AccommodationService() {
        super();
        mApi = buildApi(AccommodationApi.class);
        realm = Realm.getDefaultInstance();
    }


    public void saveAccomodations(ArrayList<Accommodation> accommodations) {

        boolean isnew=true;
        for (Accommodation mAccomodation : accommodations) {
            for (Accommodation mSavedAccomodation : getSavedAccomodations()) {
                if (mAccomodation.getId() == mSavedAccomodation.getId()) {
                    realm.beginTransaction();
                    mAccomodation.setFavorite(mSavedAccomodation.isFavorite());
                    realm.copyToRealmOrUpdate(mAccomodation);
                    realm.commitTransaction();
                    isnew=false;
                    break;
                }
            }
            if(isnew){
                //Lets save so it could work offline inthe future.
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(mAccomodation);
                realm.commitTransaction();
            }

        }


    }

    public void deleteSavedAccomodations() {
        realm.beginTransaction();
        realm.delete(Accommodation.class);
        realm.commitTransaction();

    }

    public ArrayList<Accommodation> getSavedAccomodations() {
        realm.beginTransaction();
        RealmResults<Accommodation> realmResult = realm.where(Accommodation.class).findAll();
        ArrayList<Accommodation> accommodations = new ArrayList<>();
        accommodations.addAll(realm.copyFromRealm(realmResult));
        realm.commitTransaction();
        return accommodations;
    }

    public ArrayList<Accommodation> getFavoritesSavedAccomodations() {
        realm.beginTransaction();
        RealmResults<Accommodation> realmResult = realm.where(Accommodation.class).equalTo("isFavorite", true).findAll();
        ArrayList<Accommodation> accommodations = new ArrayList<>();
        accommodations.addAll(realm.copyFromRealm(realmResult));
        realm.commitTransaction();
        return accommodations;
    }

    public void saveAccommodation(Accommodation accommodation) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(accommodation);
        realm.commitTransaction();
    }

    public void saveFavoriteAccommodation(Accommodation mAccommodation, boolean isFavorite) {
        realm.beginTransaction();
        mAccommodation.setFavorite(isFavorite);
        realm.copyToRealmOrUpdate(mAccommodation);
        realm.commitTransaction();
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

    public void searchAccomodations(final SearchAccommodationListListener listener) {


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
                    listener.onAccommodationListResult(mAccomodations);
                }, throwable -> {

                    Log.d("Response", "Response Error: " + throwable.toString());
                });
    }

    public void fetchAccomodationById(int id, FetchAccommodationistener listener) {
        mApi.getAccommodationById(id, cliente_id, formatListing).
                subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(response -> {
                    Accommodation mAccommodation = new Accommodation(response.listing);
                    saveAccommodation(mAccommodation);
                    if (listener != null) {
                        listener.onAccommodationResult(mAccommodation);
                    }

                }, throwable -> {
                    Log.d("Response", "Response Error: " + throwable.toString());
                });

    }
}

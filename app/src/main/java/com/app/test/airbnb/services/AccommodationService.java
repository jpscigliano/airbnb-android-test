package com.app.test.airbnb.services;

import android.util.Log;

import com.app.test.airbnb.model.Accomodation;
import com.app.test.airbnb.services.response.SearchDataResponse;
import com.app.test.airbnb.services.response.data.ListingDataResult;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Juan on 09/03/2017.
 */


public class AccommodationService extends BaseService {

    public AccommodationApi mApi;
    private final static String cliente_id = "3092nxybyb0otqw18e8nh5nty";
    private final static String formatListing = "v1_legacy_for_p3";

    public interface SearchAccomodationListListener {
        void onAccomodationListResult(ArrayList<Accomodation> mAccomodations);
    }

    public interface FetchAccomodationistener {
        void onAccomodationResult(Accomodation mAccomodations);
    }

    @Inject
    public AccommodationService() {
        super();
        mApi = buildApi(AccommodationApi.class);
    }

    public void searchAccomodations(final SearchAccomodationListListener listener) {


        mApi.getAccommodationsByClientId(cliente_id, "Buenos Aires", null, null).
                subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(response -> {

                    ArrayList<Accomodation> mAccomodatinos = new ArrayList<>();
                    for (SearchDataResponse<ListingDataResult> searchresult : response.searchData) {
                        mAccomodatinos.add(new Accomodation(searchresult.listingData));
                    }
                    listener.onAccomodationListResult(mAccomodatinos);
                }, throwable -> {

                    Log.d("Response", "Response Error: " + throwable.toString());
                });
    }

    public void fetchAccomodationById(int id, FetchAccomodationistener listener) {
        mApi.getAccommodationById(id, cliente_id, formatListing).
                subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(response -> {
                    Accomodation mAccomodation = new Accomodation(response);
                    listener.onAccomodationResult(mAccomodation);
                }, throwable -> {
                    Log.d("Response", "Response Error: " + throwable.toString());
                });

    }
}

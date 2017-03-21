package com.app.test.airbnb.services;

import com.app.test.airbnb.services.response.AccommodationResponse;
import com.app.test.airbnb.services.response.SearchAccomodationResponse;
import com.app.test.airbnb.services.response.SearchDataResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by Juan on 09/03/2017.
 */
public interface AccommodationApi {

    @GET("search_results")
    Observable<SearchAccomodationResponse<SearchDataResponse>> getAccommodationsByClientId(
            @Query("client_id") String Id,
            @Query("location") String location,
            @Query("_limit") int limit
    );

    @GET("listings/{listing_id}")
    Observable<AccommodationResponse> getAccommodationById(
            @Path("listing_id") int listingId,
            @Query("client_id") String Id,
            @Query("_format") String format
    );

}

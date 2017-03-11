package com.app.test.airbnb.services.response;

import com.app.test.airbnb.services.response.data.ListingDataResult;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Juan on 09/03/2017.
 */
public class AccommodationResponse {

    @SerializedName("listing")
    public ListingDataResult listing;


}

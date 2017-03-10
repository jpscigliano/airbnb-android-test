package com.app.test.airbnb.services.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Juan on 10/03/2017.
 */
public class SearchDataResponse<R> {
    @SerializedName("listing")
    public R listingData;
}


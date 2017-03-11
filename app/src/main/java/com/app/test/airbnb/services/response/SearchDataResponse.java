package com.app.test.airbnb.services.response;

import com.app.test.airbnb.services.response.data.ListingDataResult;
import com.app.test.airbnb.services.response.data.PricingDataResult;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Juan on 10/03/2017.
 */
public class SearchDataResponse {
    @SerializedName("listing")
    public ListingDataResult listingData;

    @SerializedName("pricing_quote")
    public PricingDataResult pricingData;
}


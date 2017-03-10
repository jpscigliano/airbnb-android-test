package com.app.test.airbnb.services.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Juan on 09/03/2017.
 */
public class SearchAccomodationResponse<T> {

    @SerializedName("search_results")
    public ArrayList<T> searchData;


}

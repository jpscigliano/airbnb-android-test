package com.app.test.airbnb.services.response.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Juan on 09/03/2017.
 */
public class ListingDataResult {

    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;

    @SerializedName("city")
    public String city;
    @SerializedName("country")
    public String country;
    @SerializedName("property_type")
    public String propertyType;
    @SerializedName("room_type")
    public String roomType;

    @SerializedName("native_currency")
    public String currency;
    @SerializedName("price")
    public int price;
    @SerializedName("picture_url")
    public String image;
    @SerializedName("description")
    public String description;
    @SerializedName("lat")
    public double latitude;
    @SerializedName("lng")
    public double longitude;
    @SerializedName("person_capacity")
    public int capacity;
    @SerializedName("bathrooms")
    public float bathroom;
    @SerializedName("beds")
    public int beds;
    @SerializedName("bedroom")
    public int bedroom;
    @SerializedName("guests_included")
    public int guest;


}

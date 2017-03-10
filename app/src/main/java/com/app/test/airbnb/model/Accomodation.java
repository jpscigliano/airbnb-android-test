package com.app.test.airbnb.model;

import com.app.test.airbnb.services.response.data.ListingDataResult;

/**
 * Created by Juan on 10/03/2017.
 */
public class Accomodation {
    public int id, price, capacity, beds;
    public String name, city, country, propertyType, roomType, currency, image, description;
    public double latitude, longitude;
    public float bathroom;

    public Accomodation(ListingDataResult result) {
        this.id = result.id;
        this.price = result.price;
        this.capacity = result.capacity;
        this.beds = result.beds;
        this.name = result.name;
        this.city = result.city;
        this.country = result.country;
        this.propertyType = result.propertyType;
        this.roomType = result.roomType;
        this.currency = result.currency;
        this.image = result.image;
        this.description = result.description;
        this.latitude = result.latitude;
        this.longitude = result.longitude;
        this.bathroom = result.bathroom;
    }
}

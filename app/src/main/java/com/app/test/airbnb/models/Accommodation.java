package com.app.test.airbnb.models;

import com.app.test.airbnb.services.response.data.ListingDataResult;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Juan on 10/03/2017.
 */
public class Accommodation extends RealmObject {
    @PrimaryKey
    private int id;
    private int price, capacity, beds;
    private String name, city, country, propertyType, roomType, currency, image, description;
    private double latitude, longitude;
    private float bathroom;

    public Accommodation(){}
    public Accommodation(ListingDataResult result) {
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

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getBeds() {
        return beds;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getCurrency() {
        return currency;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getBathroom() {
        return bathroom;
    }
}

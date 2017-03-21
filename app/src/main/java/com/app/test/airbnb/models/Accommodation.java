package com.app.test.airbnb.models;

import com.app.test.airbnb.services.response.data.ListingDataResult;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Juan on 10/03/2017.
 */
public class Accommodation extends RealmObject implements Serializable {
    public static final String KEY = "accomodation";

    @PrimaryKey
    private int id;
    private int price, capacity, beds, bedrooms, guest;
    private String name, city, country, propertyType, roomType, currency, image, description;

    private double latitude, longitude;
    private float bathroom;
    private boolean isFavorite;

    public Accommodation() {
    }
    public Accommodation(int id) {
        this.id=id;
    }

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
        this.guest = result.guest;
        this.bedrooms = result.bedroom;
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

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getGuest() {
        return guest;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void update(Accommodation accommodation) {
        if (accommodation.getCurrency() != null && !accommodation.getCurrency().isEmpty()) {
            this.currency = accommodation.getCurrency();
        }
        if (accommodation.getPropertyType() != null && !accommodation.getPropertyType().isEmpty()) {
            this.propertyType = accommodation.getPropertyType();
        }
        if (accommodation.getPrice() != 0) {
            this.price = accommodation.getPrice();
        }
        if (accommodation.getBathroom() != 0) {
            this.bathroom = accommodation.getBathroom();
        }
        if (accommodation.getBeds() != 0) {
            this.beds = accommodation.getBeds();
        }
        if (accommodation.getBedrooms() != 0) {
            this.bedrooms = accommodation.getBedrooms();
        }
        if (accommodation.getGuest() != 0) {
            this.guest = accommodation.getGuest();
        }
        if (accommodation.getDescription() != null && !accommodation.getDescription().isEmpty()) {
            this.description = accommodation.getDescription();
        }

        if (accommodation.getLongitude() != 0) {
            this.latitude = accommodation.getLatitude();
        }
        if (accommodation.getLongitude() != 0) {
            this.longitude = accommodation.getLongitude();
        }

        if (accommodation.getRoomType() != null && !accommodation.getRoomType().isEmpty()) {
            this.roomType = accommodation.getRoomType();
        }
        if (accommodation.getCity() != null && !accommodation.getCity().isEmpty()) {
            this.city = accommodation.getCity();
        }

    }
}

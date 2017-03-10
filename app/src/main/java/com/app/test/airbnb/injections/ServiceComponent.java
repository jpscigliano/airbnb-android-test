package com.app.test.airbnb.injections;

import com.app.test.airbnb.activities.MainActivity;
import com.app.test.airbnb.services.AccommodationService;
import com.google.gson.annotations.SerializedName;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Juan on 09/03/2017.
 */

    @Singleton
    @Component(modules = {ServiceModule.class})
    public interface ServiceComponent {
       AccommodationService provideAccommodationService();
    }


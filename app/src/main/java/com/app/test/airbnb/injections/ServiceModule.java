package com.app.test.airbnb.injections;

import com.app.test.airbnb.services.AccommodationService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Juan on 09/03/2017.
 */

@Module
public class ServiceModule {


    @Provides    @Singleton
    AccommodationService provideAccommodationService() {
        return new AccommodationService();
    }
}

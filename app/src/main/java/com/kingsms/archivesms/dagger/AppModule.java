package com.kingsms.archivesms.dagger;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


//this class created to put main providers an all application
@Module
public class AppModule {
    private Application application;


    //provide  application reference
    public AppModule(Application application) {
        this.application = application;
    }


    //provide  context
    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }









}

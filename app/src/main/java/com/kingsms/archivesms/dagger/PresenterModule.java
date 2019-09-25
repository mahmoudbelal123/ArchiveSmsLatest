package com.kingsms.archivesms.dagger;


import android.content.Context;

import com.kingsms.archivesms.view.HomeActivity.HomeSenderNamesPresenter;
import com.kingsms.archivesms.view.login.LoginPresenter;
import com.kingsms.archivesms.view.register.RegisterPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;



//this class created to put  providers for Presenters

@Module
public class PresenterModule {


    @Provides
    @Singleton
    RegisterPresenter provideRegisterPresenter(Context context) {

        return new RegisterPresenter(context);
    }



    @Provides
    @Singleton
    LoginPresenter provideLoginPresenter(Context context) {

        return new LoginPresenter(context);
    }
    @Provides
    @Singleton
    HomeSenderNamesPresenter provideHomePresenter(Context context) {

        return new HomeSenderNamesPresenter(context);
    }



}


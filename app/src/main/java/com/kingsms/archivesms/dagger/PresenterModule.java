package com.kingsms.archivesms.dagger;


import android.content.Context;

import com.kingsms.archivesms.view.HomeActivity.HomeSenderNamesPresenter;
import com.kingsms.archivesms.view.activation_code.ActivationCodePresenter;
import com.kingsms.archivesms.view.login.LoginPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;



//this class created to put  providers for Presenters

@Module
public class PresenterModule {



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

    @Provides
    @Singleton
    ActivationCodePresenter activationCodePresenter(Context context) {

        return new ActivationCodePresenter(context);
    }


}


package com.kingsms.archivesms.dagger;


import com.kingsms.archivesms.view.HomeActivity.HomeSenderNamesActivity;
import com.kingsms.archivesms.view.HomeActivity.HomeSenderNamesPresenter;
import com.kingsms.archivesms.view.activation_code.ActivationCodeActivity;
import com.kingsms.archivesms.view.activation_code.ActivationCodePresenter;
import com.kingsms.archivesms.view.login.LoginActivity;
import com.kingsms.archivesms.view.login.LoginPresenter;

import javax.inject.Singleton;

import dagger.Component;


// this class created for register who need inject
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, PresenterModule.class})
public interface AppComponent {


    void inject(LoginActivity loginActivity);

    void inject(LoginPresenter loginPresenter);

    void inject(HomeSenderNamesActivity homeSenderNamesActivity);

    void inject(HomeSenderNamesPresenter homeSenderNamesPresenter);

    void inject(ActivationCodeActivity activationCodeActivity);

    void inject(ActivationCodePresenter activationCodePresenter);

}

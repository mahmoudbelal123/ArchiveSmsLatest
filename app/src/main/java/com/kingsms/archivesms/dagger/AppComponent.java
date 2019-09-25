package com.kingsms.archivesms.dagger;


import com.kingsms.archivesms.view.HomeActivity.HomeSenderNamesActivity;
import com.kingsms.archivesms.view.HomeActivity.HomeSenderNamesPresenter;
import com.kingsms.archivesms.view.login.LoginActivity;
import com.kingsms.archivesms.view.login.LoginPresenter;
import com.kingsms.archivesms.view.register.RegisterActivity;
import com.kingsms.archivesms.view.register.RegisterPresenter;

import javax.inject.Singleton;

import dagger.Component;



// this class created for register who need inject
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class,PresenterModule.class})
public interface AppComponent {

    void inject(RegisterActivity registerActivity);
    void inject(RegisterPresenter registerPresenter);

    void inject(LoginActivity loginActivity);
    void inject(LoginPresenter loginPresenter);

    void inject(HomeSenderNamesActivity homeSenderNamesActivity);
    void inject(HomeSenderNamesPresenter homeSenderNamesPresenter);


}

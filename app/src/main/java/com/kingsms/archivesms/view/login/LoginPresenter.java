package com.kingsms.archivesms.view.login;

import android.content.Context;
import android.widget.Toast;

import com.kingsms.archivesms.apiClient.ApiInterface;
import com.kingsms.archivesms.baseClass.BasePresenter;
import com.kingsms.archivesms.dagger.DaggerApplication;
import com.kingsms.archivesms.helper.Utilities;
import com.kingsms.archivesms.model.login.LoginRequest;
import com.kingsms.archivesms.model.login.LoginResponse;
import com.kingsms.archivesms.model.register.RegisterRequest;
import com.kingsms.archivesms.model.register.RegisterResponse;
import com.kingsms.archivesms.view.register.RegisterView;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class LoginPresenter implements BasePresenter<LoginView> {
    LoginView mView;
    @Inject
    ApiInterface mApiInterface;
    @Inject
    Context mContext;
    LoginRequest loginRequest;



    @Override
    public void onAttach(LoginView view) {
        mView = view;
        mView.onAttache();

    }



    @Override
    public void onDetach() {
        mView = null;
    }
    //create Constructor to get reference of api interface object
    public LoginPresenter(Context context){
        ((DaggerApplication)context).getAppComponent().inject(this);
    }

    //this function created to load items from specific endpoint
    public void loginPresenter(String firebaseToken , String phone ,String password ) {

        try {
            if (!Utilities.checkConnection(mContext)) {
                mView.showErrorMessage("No Internet !");
                return;
            }


            else if (phone.equals("")) {
                mView.showPhoneError();
                return;

            }
            else if (password.equals("")) {

                mView.showPasswordError();
                return;

            }
            else {
                mView.showLoading();

                loginRequest = new LoginRequest();
                loginRequest.setPhone(phone);
                loginRequest.setPassword(password);
                loginRequest.setFirebase_token(firebaseToken);

                mApiInterface.loginObservable(loginRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<LoginResponse>() {
                            @Override
                            public final void onCompleted() {

                                mView.hideLoading();
                            }

                            @Override
                            public final void onError(Throwable e) {


                                Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                mView.hideLoading();


                            }

                            @Override
                            public final void onNext(LoginResponse response) {
                               mView.hideLoading();
                                mView.showSuccessMessage(""+response.getCode());
                                Utilities.saveUserInfo(mContext , response);

                            }
                        });


            }

        }catch (Exception e)
        {
            mView.showErrorMessage("error \n"+e.getMessage());

        }

    }











}

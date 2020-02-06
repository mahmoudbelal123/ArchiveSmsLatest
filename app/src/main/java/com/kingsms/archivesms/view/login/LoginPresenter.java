package com.kingsms.archivesms.view.login;

import android.content.Context;
import android.widget.Toast;

import com.kingsms.archivesms.R;
import com.kingsms.archivesms.apiClient.ApiInterface;
import com.kingsms.archivesms.baseClass.BasePresenter;
import com.kingsms.archivesms.dagger.DaggerApplication;
import com.kingsms.archivesms.helper.Utilities;
import com.kingsms.archivesms.model.login.LoginRequest;
import com.kingsms.archivesms.model.login.LoginResponse;
import com.kingsms.archivesms.model.login.LoginResponse2;
import com.kingsms.archivesms.model.register.RegisterRequest;
import com.kingsms.archivesms.model.register.RegisterResponse;

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
    public void loginPresenter(String firebaseToken , String phone  ) {

        try {
            if (!Utilities.checkConnection(mContext)) {
                mView.showErrorMessage(mContext.getString(R.string.check_internet));
                return;
            }


            else if (phone.equals("")) {
                mView.showPhoneError();
                return;

            }

            else {
                mView.showLoading();

                loginRequest = new LoginRequest();
                loginRequest.setPhone(phone);
                loginRequest.setFirebase_token(firebaseToken);

                mApiInterface.loginObservable(loginRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<LoginResponse2>() {
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
                            public final void onNext(LoginResponse2 response) {
                               mView.hideLoading();
                                mView.showSuccessMessage(""+response.getCode());
                               //>> Utilities.saveUserInfo(mContext , response);

                            }
                        });


            }

        }catch (Exception e)
        {
            mView.showErrorMessage("error \n"+e.getMessage());

        }

    }











}

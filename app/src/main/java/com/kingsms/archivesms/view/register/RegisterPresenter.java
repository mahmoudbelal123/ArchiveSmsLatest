package com.kingsms.archivesms.view.register;

import android.content.Context;
import android.widget.Toast;

import com.kingsms.archivesms.R;
import com.kingsms.archivesms.apiClient.ApiInterface;
import com.kingsms.archivesms.baseClass.BasePresenter;
import com.kingsms.archivesms.dagger.DaggerApplication;
import com.kingsms.archivesms.helper.Utilities;
import com.kingsms.archivesms.model.register.RegisterRequest;
import com.kingsms.archivesms.model.register.RegisterResponse;

import javax.inject.Inject;


import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class RegisterPresenter implements BasePresenter<RegisterView> {
    RegisterView mView;
    @Inject
    ApiInterface mApiInterface;
    @Inject
    Context mContext;
    RegisterRequest registerRequest;



    @Override
    public void onAttach(RegisterView view) {
        mView = view;
        mView.onAttache();

    }



    @Override
    public void onDetach() {
        mView = null;
    }
    //create Constructor to get reference of api interface object
    public RegisterPresenter(Context context){
        ((DaggerApplication)context).getAppComponent().inject(this);
    }

    //this function created to load items from specific endpoint
    public void registerPresenter(String full_name , String phone ,String password , String confirmPassword ) {

        try {
            if (!Utilities.checkConnection(mContext)) {
                mView.showErrorMessage("No Internet !");
                return;
            }


            else if (full_name.equals("")) {
                mView.showNameError();
                return;

            } else if (phone.equals("")) {

                mView.showPhoneError();
                return;

            }
            else if (password.equals("")) {

                mView.showPasswordError();
                return;

            }
            else if (!password.equals(confirmPassword)) {

                mView.showConfirmPasswordError();
                return;

            }
            else {
                mView.showLoading();

                registerRequest = new RegisterRequest();
                registerRequest.setFull_name(full_name);
                registerRequest.setPhone(phone);
                registerRequest.setPassword(password);

                mApiInterface.registerObservable(registerRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<RegisterResponse>() {
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
                            public final void onNext(RegisterResponse response) {
                               mView.hideLoading();
                                mView.showSuccessMessage(response.getMessage());

                            }
                        });


            }

        }catch (Exception e)
        {
            mView.showErrorMessage("error \n"+e.getMessage());

        }

    }











}

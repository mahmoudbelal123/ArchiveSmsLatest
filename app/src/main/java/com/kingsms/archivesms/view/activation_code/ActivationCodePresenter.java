package com.kingsms.archivesms.view.activation_code;

import android.content.Context;
import android.widget.Toast;

import com.kingsms.archivesms.R;
import com.kingsms.archivesms.apiClient.ApiInterface;
import com.kingsms.archivesms.baseClass.BasePresenter;
import com.kingsms.archivesms.dagger.DaggerApplication;
import com.kingsms.archivesms.helper.Utilities;
import com.kingsms.archivesms.model.activation_code.ActivationRequest;
import com.kingsms.archivesms.model.activation_code.ActivationResponse;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ActivationCodePresenter implements BasePresenter<ActivationCodeView> {
    ActivationCodeView mView;
    @Inject
    ApiInterface mApiInterface;
    @Inject
    Context mContext;
    ActivationRequest activationRequest;


    //create Constructor to get reference of api interface object
    public ActivationCodePresenter(Context context) {
        ((DaggerApplication) context).getAppComponent().inject(this);
    }

    @Override
    public void onAttach(ActivationCodeView view) {
        mView = view;
        mView.onAttache();

    }

    @Override
    public void onDetach() {
        mView = null;
    }

    //this function created to load items from specific endpoint
    public void activatePresenter(String firebaseToken, String code, String phone) {

        try {
            if (!Utilities.checkConnection()) {
                mView.showErrorMessage(mContext.getString(R.string.check_internet));
                return;
            } else if (phone.equals("")) {
                mView.showPhoneError();
                return;

            } else {
                mView.showLoading();

                activationRequest = new ActivationRequest();
                activationRequest.setCode(code);
                activationRequest.setPhone(phone);
                activationRequest.setFirebase_token(firebaseToken);

                mApiInterface.activateObservable(activationRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ActivationResponse>() {
                            @Override
                            public final void onCompleted() {

                                mView.hideLoading();
                            }

                            @Override
                            public final void onError(Throwable e) {


                                Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                mView.hideLoading();


                            }

                            @Override
                            public final void onNext(ActivationResponse response) {
                                mView.hideLoading();
                                mView.showSuccessMessage(response);
                                Utilities.saveUserInfo(mContext, response);

                            }
                        });


            }

        } catch (Exception e) {
            mView.showErrorMessage("error \n" + e.getMessage());

        }

    }


}

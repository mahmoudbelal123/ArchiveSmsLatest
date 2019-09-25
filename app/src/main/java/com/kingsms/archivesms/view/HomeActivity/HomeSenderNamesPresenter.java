package com.kingsms.archivesms.view.HomeActivity;

import android.content.Context;
import android.widget.Toast;

import com.kingsms.archivesms.apiClient.ApiInterface;
import com.kingsms.archivesms.baseClass.BasePresenter;
import com.kingsms.archivesms.dagger.DaggerApplication;
import com.kingsms.archivesms.helper.Utilities;
import com.kingsms.archivesms.model.confirm_message_delivery.ConfirmMessageDeliveryResponse;
import com.kingsms.archivesms.model.confirm_message_delivery.ConfirmMessageRequest;
import com.kingsms.archivesms.model.register.RegisterRequest;
import com.kingsms.archivesms.model.register.RegisterResponse;
import com.kingsms.archivesms.view.register.RegisterView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HomeSenderNamesPresenter implements BasePresenter<HomeSenderNamesView> {
    HomeSenderNamesView mView;
    @Inject
    ApiInterface mApiInterface;
    @Inject
    Context mContext;
    ConfirmMessageRequest confirmMessageRequest;



    @Override
    public void onAttach(HomeSenderNamesView view) {
        mView = view;
        mView.onAttache();

    }



    @Override
    public void onDetach() {
        mView = null;
    }
    //create Constructor to get reference of api interface object
    public HomeSenderNamesPresenter(Context context){
        ((DaggerApplication)context).getAppComponent().inject(this);
    }

//    public void confirmPresenter(String token , List<String> messageIdsList ) {
//
//                List<Integer> list = new ArrayList<>();
//
//                for(int i = 0 ; i < messageIdsList.size() ; i++)
//                {
//                    list.add(Integer.parseInt(messageIdsList.get(i)));
//                }
//                //confirmMessageRequest = new ConfirmMessageRequest();
//              //  confirmMessageRequest.setConfirmed_ids(list);
//
//                mApiInterface.confirmMessageDelivery(token , list)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Subscriber<ConfirmMessageDeliveryResponse>() {
//                            @Override
//                            public final void onCompleted() {
//
//                                mView.hideLoading();
//                            }
//
//                            @Override
//                            public final void onError(Throwable e) {
//
//
//                                Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                                mView.hideLoading();
//
//
//                            }
//
//                            @Override
//                            public final void onNext(ConfirmMessageDeliveryResponse response) {
//                               mView.hideLoading();
//                               mView.showResponse(response);
//
//                            }
//                        });
//
//
//            }
}
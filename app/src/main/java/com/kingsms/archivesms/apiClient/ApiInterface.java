package com.kingsms.archivesms.apiClient;


import com.kingsms.archivesms.model.activation_code.ActivationRequest;
import com.kingsms.archivesms.model.activation_code.ActivationResponse;
import com.kingsms.archivesms.model.confirm_message_delivery.ConfirmMessageDeliveryResponse;
import com.kingsms.archivesms.model.confirm_message_delivery.ConfirmMessageRequest;
import com.kingsms.archivesms.model.login.LoginRequest;
import com.kingsms.archivesms.model.login.LoginResponse2;
import com.kingsms.archivesms.model.logout.LogoutResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiInterface {

    @POST(EndPoints.VERIFY)
    Observable<ActivationResponse> activateObservable(@Body ActivationRequest activationRequest);

    @POST(EndPoints.LOGIN)
    Observable<LoginResponse2> loginObservable(@Body LoginRequest loginRequest);

    @POST(EndPoints.CONFIRM_MESSAGE_DELIVERY)
    Call<ConfirmMessageDeliveryResponse> confirmMessageDelivery(@Header("Authorization") String token, @Body ConfirmMessageRequest list);

    @GET(EndPoints.LOGOUT)
    Call<LogoutResponse> logout(@Header("Authorization") String token);


}

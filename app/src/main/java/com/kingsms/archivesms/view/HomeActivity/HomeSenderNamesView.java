package com.kingsms.archivesms.view.HomeActivity;


import com.kingsms.archivesms.baseClass.BaseView;
import com.kingsms.archivesms.model.confirm_message_delivery.ConfirmMessageDeliveryResponse;

//class created for register function for main view
public interface HomeSenderNamesView extends BaseView {

    void showLoading();

    void hideLoading();

    void showResponse(ConfirmMessageDeliveryResponse confirmMessageDeliveryResponse);


}

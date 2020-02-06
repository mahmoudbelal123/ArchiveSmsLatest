package com.kingsms.archivesms.view.activation_code;


import com.kingsms.archivesms.baseClass.BaseView;
import com.kingsms.archivesms.model.activation_code.ActivationResponse;

//class created for register function for main view
public interface ActivationCodeView extends BaseView {

    void showLoading();

    void hideLoading();

    void showErrorMessage(String message);
    void showSuccessMessage(ActivationResponse response);

    void showPasswordError();
    void showPhoneError();


}

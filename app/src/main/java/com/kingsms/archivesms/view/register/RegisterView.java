package com.kingsms.archivesms.view.register;


import com.kingsms.archivesms.baseClass.BaseView;

//class created for register function for main view
public interface RegisterView extends BaseView {

    void showLoading();

    void hideLoading();

    void showErrorMessage(String message);
    void showSuccessMessage(String message);

    void showNameError();
    void showPasswordError();
    void showPhoneError();
    void showConfirmPasswordError();


}

package com.kingsms.archivesms.view.login;


import com.kingsms.archivesms.baseClass.BaseView;

//class created for register function for main view
public interface LoginView extends BaseView {

    void showLoading();

    void hideLoading();

    void showErrorMessage(String message);

    void showSuccessMessage(String message);

    void showPasswordError();

    void showPhoneError();


}

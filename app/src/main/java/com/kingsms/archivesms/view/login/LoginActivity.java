package com.kingsms.archivesms.view.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.kingsms.archivesms.view.HomeActivity.HomeSenderNamesActivity;
import com.kingsms.archivesms.R;
import com.kingsms.archivesms.dagger.DaggerApplication;
import com.kingsms.archivesms.helper.Utilities;
import com.kingsms.archivesms.view.register.RegisterActivity;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity implements  LoginView , View.OnClickListener {


    @Inject
    LoginPresenter loginPresenter;
    EditText  editPhone, editPassword ;
    ProgressBar progressBarRegister;
    Button loginBtn , goToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if(Utilities.retrieveUserInfo(this) != null)
        {
            startActivity(new Intent(this , HomeSenderNamesActivity.class));
            this.finish();
            return;
        }
        editPhone = findViewById(R.id.edit_phone_login);
        editPassword = findViewById(R.id.edit_password_login);

        progressBarRegister = findViewById(R.id.progress_register_login);
        loginBtn = findViewById(R.id.button_login);
        goToRegister = findViewById(R.id.button_signup);

        goToRegister.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

        ((DaggerApplication) getApplication()).getAppComponent().inject(this);
        loginPresenter.onAttach(this);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.button_login :

loginLogic();
                break;

            case R.id.button_signup :
                startActivity(new Intent(this , RegisterActivity.class));
                break;
        }
    }

    @Override
    public void showLoading() {

        progressBarRegister.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {

        progressBarRegister.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showErrorMessage(String message) {

        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {

        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this , HomeSenderNamesActivity.class));

    }

    @Override
    public void showPasswordError() {

        editPassword.setError("Enter Password");
    }

    @Override
    public void showPhoneError() {

        editPhone.setError("Enter Phone");
    }

    @Override
    public void onAttache() {

    }

    @Override
    public void onDetach() {

    }

    private void loginLogic(){
       String newToken = FirebaseInstanceId.getInstance().getToken();
        loginPresenter.loginPresenter(newToken , editPhone.getText().toString() , editPassword.getText().toString());
    }
}

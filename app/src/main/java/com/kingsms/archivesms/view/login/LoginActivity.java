package com.kingsms.archivesms.view.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
import com.kingsms.archivesms.view.activation_code.ActivationCodeActivity;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity implements  LoginView , View.OnClickListener {


    private static final int REQUEST_READ_PHONE_STATE = 2020;
    @Inject
    LoginPresenter loginPresenter;
    EditText  editPhone, editPassword ;
    ProgressBar progressBarRegister;
    Button loginBtn , goToRegister;
    String phoneNumber  ,SN = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


       // int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

       /* if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO get my phone number
            TelephonyManager phoneManager = (TelephonyManager)
                    getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                   phoneNumber = phoneManager.getLine1Number();
                   SN = phoneManager.getImei();
        }*/

        if(Utilities.retrieveUserInfo(this) != null)
        {
            startActivity(new Intent(this , HomeSenderNamesActivity.class));
            this.finish();
            return;
        }
        editPhone = findViewById(R.id.edit_phone_login);
        //editPassword = findViewById(R.id.edit_password_login);

        progressBarRegister = findViewById(R.id.progress_register_login);
        loginBtn = findViewById(R.id.button_login);
        //goToRegister = findViewById(R.id.button_signup);

       // goToRegister.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

        ((DaggerApplication) getApplication()).getAppComponent().inject(this);
        loginPresenter.onAttach(this);



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO get my phone number
                    int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                    } else {
                        //TODO get my phone number
                        TelephonyManager phoneManager = (TelephonyManager)
                                getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                        phoneNumber = phoneManager.getLine1Number();
                        SN = phoneManager.getImei();
                    }
                }
                break;

            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.button_login :

                loginLogic();
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

      //  Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this , ActivationCodeActivity.class);
        intent.putExtra("phone", editPhone.getText().toString());
        startActivity(intent);

    }

    @Override
    public void showPasswordError() {

        editPassword.setError("");
    }

    @Override
    public void showPhoneError() {

        editPhone.setError(getString(R.string.enter_mobile_number));
    }

    @Override
    public void onAttache() {

    }

    @Override
    public void onDetach() {

    }

    private void loginLogic(){
       String newToken = FirebaseInstanceId.getInstance().getToken();
        loginPresenter.loginPresenter(newToken , editPhone.getText().toString());
    }
}

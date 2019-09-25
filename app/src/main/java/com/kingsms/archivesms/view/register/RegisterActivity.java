package com.kingsms.archivesms.view.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kingsms.archivesms.R;
import com.kingsms.archivesms.dagger.DaggerApplication;
import com.kingsms.archivesms.local_db.MyDatabaseAdapter;
import com.kingsms.archivesms.view.login.LoginActivity;

import javax.inject.Inject;

public class RegisterActivity extends AppCompatActivity implements RegisterView  , View.OnClickListener {


    @Inject
    RegisterPresenter registerPresenter;
    EditText editName, editPhone, editPassword , editConfirmPassword;
    ProgressBar progressBarRegister;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editName = findViewById(R.id.edit_full_name);
        editPhone = findViewById(R.id.edit_phone);
        editPassword = findViewById(R.id.edit_password);
        editConfirmPassword = findViewById(R.id.edit_confirm_password);

        progressBarRegister = findViewById(R.id.progress_register);
        registerBtn = findViewById(R.id.button_register);

        registerBtn.setOnClickListener(this);

        ((DaggerApplication) getApplication()).getAppComponent().inject(this);
         registerPresenter.onAttach(this);


        MyDatabaseAdapter db = new MyDatabaseAdapter(this);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId())
       {
           case R.id.button_register :


               registerLogic();
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
        startActivity(new Intent(RegisterActivity.this , LoginActivity.class));
    }

    @Override
    public void showNameError() {

        editName.setError("Enter name");
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
    public void showConfirmPasswordError() {

        editConfirmPassword.setError("not match with the password");
    }

    @Override
    public void onAttache() {

    }

    @Override
    public void onDetach() {

    }

    private void registerLogic()
    {
        if(editName.getText().toString() != null && editPhone.getText().toString() != null && editPassword.getText().toString() != null && editConfirmPassword.getText().toString()!= null)
        registerPresenter.registerPresenter(editName.getText().toString() , editPhone.getText().toString() , editPassword.getText().toString(), editConfirmPassword.getText().toString());

    }

}

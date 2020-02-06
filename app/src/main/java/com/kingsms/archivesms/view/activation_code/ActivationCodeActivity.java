package com.kingsms.archivesms.view.activation_code;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.kingsms.archivesms.R;
import com.kingsms.archivesms.dagger.DaggerApplication;
import com.kingsms.archivesms.model.activation_code.ActivationResponse;
import com.kingsms.archivesms.view.HomeActivity.HomeSenderNamesActivity;

import javax.inject.Inject;

public class ActivationCodeActivity extends AppCompatActivity implements ActivationCodeView {

    private EditText activationCodeEdit;
    private Button sendCodeBtn;
    private ProgressBar progressBarActivate;

    @Inject
    ActivationCodePresenter activationCodePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_code);


        activationCodeEdit = findViewById(R.id.edit_activation_code);
        sendCodeBtn = findViewById(R.id.button_send_code);
        progressBarActivate = findViewById(R.id.progress_activate);

        ((DaggerApplication) getApplication()).getAppComponent().inject(this);
        activationCodePresenter.onAttach(this);


        sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            activateLogic();
            }
        });
    }


    private void activateLogic(){
        String newToken = FirebaseInstanceId.getInstance().getToken();
        if(activationCodeEdit.getText().length() == 0)
        {
         activationCodeEdit.setError(getString(R.string.enter_code));
        }
        else {
            activationCodePresenter.activatePresenter(newToken, activationCodeEdit.getText().toString(), getIntent().getStringExtra("phone"));
        }

        }
    @Override
    public void showLoading() {

        progressBarActivate.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBarActivate.setVisibility(View.GONE);

    }

    @Override
    public void showErrorMessage(String message) {

        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(ActivationResponse response) {

      //  Toast.makeText(this, ""+response, Toast.LENGTH_SHORT).show();

        if(response.getCode() == 200)
            startActivity(new Intent(this , HomeSenderNamesActivity.class));
        else
        {
            Toast.makeText(this, ""+getString(R.string.invalid_code), Toast.LENGTH_LONG).show();
            Toast.makeText(this, ""+getString(R.string.invalid_code), Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void showPasswordError() {

    }

    @Override
    public void showPhoneError() {

        Toast.makeText(this, ""+getString(R.string.mobile_number_is_empty), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttache() {

    }

    @Override
    public void onDetach() {

    }
}

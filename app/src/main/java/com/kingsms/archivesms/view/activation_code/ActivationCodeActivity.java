package com.kingsms.archivesms.view.activation_code;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.kingsms.archivesms.R;
import com.kingsms.archivesms.dagger.DaggerApplication;
import com.kingsms.archivesms.helper.Constants;
import com.kingsms.archivesms.helper.Utilities;
import com.kingsms.archivesms.model.activation_code.ActivationResponse;
import com.kingsms.archivesms.view.HomeActivity.HomeSenderNamesActivity;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;

public class ActivationCodeActivity extends AppCompatActivity implements ActivationCodeView {

    @Inject
    ActivationCodePresenter activationCodePresenter;
    String phone = "";
    private EditText activationCodeEdit;
    private Button sendCodeBtn;
    private ProgressBar progressBarActivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_activation_code);


        activationCodeEdit = findViewById(R.id.edit_activation_code);
        sendCodeBtn = findViewById(R.id.button_send_code);
        progressBarActivate = findViewById(R.id.progress_activate);

        ((DaggerApplication) getApplication()).getAppComponent().inject(this);
        activationCodePresenter.onAttach(this);

        phone = Utilities.getPhoneActivatedCode(this);

        sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateLogic();
            }
        });
    }


    private void activateLogic() {
        String newToken = FirebaseInstanceId.getInstance().getToken();
        if (activationCodeEdit.getText() == null || TextUtils.isEmpty(activationCodeEdit.getText())) {
            activationCodeEdit.setError(getString(R.string.enter_code));
            return;
        }
        if (activationCodeEdit.getText().length() != 6) {
            Utilities.setActivatedCode(this, Constants.SENT_ACTIVATION_CODE_BUT_NOT_ENTERED_CASE, phone);
            activationCodeEdit.setError(getString(R.string.enter_valid_code));
        } else {

            activationCodePresenter.activatePresenter(newToken, activationCodeEdit.getText().toString(), phone);
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
        Utilities.setActivatedCode(this, Constants.SENT_ACTIVATION_CODE_BUT_ACTIVATION_FAILED, phone);
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(ActivationResponse response) {

        if (response.getCode() == 200) {
            Utilities.setActivatedCode(this, Constants.ACTIVATED_CASE, phone);
            Intent intent = new Intent(this, HomeSenderNamesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else {
            Utilities.setActivatedCode(this, Constants.SENT_ACTIVATION_CODE_BUT_ACTIVATION_FAILED, phone);
            Toast.makeText(this, "" + getString(R.string.invalid_code), Toast.LENGTH_LONG).show();
            Toast.makeText(this, "" + getString(R.string.invalid_code), Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void showPasswordError() {

    }

    @Override
    public void showPhoneError() {

        Toast.makeText(this, "" + getString(R.string.mobile_number_is_empty), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttache() {

    }

    @Override
    public void onDetach() {

    }
}

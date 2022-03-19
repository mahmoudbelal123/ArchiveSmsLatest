package com.kingsms.archivesms.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.kingsms.archivesms.R;
import com.kingsms.archivesms.helper.Constants;
import com.kingsms.archivesms.helper.Utilities;
import com.kingsms.archivesms.view.HomeActivity.HomeSenderNamesActivity;
import com.kingsms.archivesms.view.activation_code.ActivationCodeActivity;
import com.kingsms.archivesms.view.login.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Utilities.getActivatedCode(this) == 0
                || Utilities.getActivatedCode(this) == Constants.NOT_ACTIVATED_CASE) {
            setContentView(R.layout.activity_splash);
            openLoginActivity();
        } else if (Utilities.getActivatedCode(this) == Constants.ACTIVATED_CASE) {
            openHomeActivity();
            return;
        } else if (Utilities.getActivatedCode(this) == Constants.SENT_ACTIVATION_CODE_BUT_ACTIVATION_FAILED
                || Utilities.getActivatedCode(this) == Constants.SENT_ACTIVATION_CODE_BUT_NOT_ENTERED_CASE) {
            openActivationCodeActivity();
            return;
        }


    }


    private void openLoginActivity() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Button addNewAccount = findViewById(R.id.button_add_account);

        addNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    private void openHomeActivity() {
        Intent intent = new Intent(SplashActivity.this, HomeSenderNamesActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    private void openActivationCodeActivity() {
        Intent intent = new Intent(SplashActivity.this, ActivationCodeActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

}

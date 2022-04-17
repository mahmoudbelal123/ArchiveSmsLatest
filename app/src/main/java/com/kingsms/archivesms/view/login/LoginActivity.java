package com.kingsms.archivesms.view.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kingsms.archivesms.R;
import com.kingsms.archivesms.dagger.DaggerApplication;
import com.kingsms.archivesms.helper.Constants;
import com.kingsms.archivesms.helper.Utilities;
import com.kingsms.archivesms.local_db.MyDatabaseAdapter;
import com.kingsms.archivesms.model.phones.Contact;
import com.kingsms.archivesms.view.HomeActivity.HomeSenderNamesActivity;
import com.kingsms.archivesms.view.activation_code.ActivationCodeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class LoginActivity extends AppCompatActivity implements LoginView, View.OnClickListener {


    public static final int RequestPermissionCode = 1;
    private static final int REQUEST_READ_PHONE_STATE = 2020;
    String phone;
    String phoneNumber;
    List<Contact> contactList;
    Cursor cursor;
    @Inject
    LoginPresenter loginPresenter;
    EditText editPhone, editPassword;
    ProgressBar progressBarRegister;
    Button loginBtn;
    String codeKsa = null;
    private String mVerificationId;
    private String name, phonenumber;
    private TextView txtCodeKsa;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_login);
        if (Utilities.retrieveUserInfo(this) != null) {
            startActivity(new Intent(this, HomeSenderNamesActivity.class));
            this.finish();
            return;
        }

        mAuth = FirebaseAuth.getInstance();

        editPhone = findViewById(R.id.edit_phone_login);
        txtCodeKsa = findViewById(R.id.text_code_ksa);

        progressBarRegister = findViewById(R.id.progress_register_login);
        loginBtn = findViewById(R.id.button_login);
        loginBtn.setOnClickListener(this);

        ((DaggerApplication) getApplication()).getAppComponent().inject(this);
        loginPresenter.onAttach(this);


        /*if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            GetContactsIntoArrayList();
        } else {
            requestPermission();
        }*/


    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_PHONE_STATE);
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_PHONE_STATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetContactsIntoArrayList();
                } else {
                    // permission denied,Disable the
                    // Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EnableRuntimePermission();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_login:

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
        Utilities.setActivatedCode(this, Constants.NOT_ACTIVATED_CASE, "" + codeKsa + phone);

        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        if(!TextUtils.isEmpty(editPhone.getText().toString())) {
            phoneNumber = txtCodeKsa.getText().toString()  + validateMobileNumber(editPhone.getText().toString());
        }
        Utilities.setActivatedCode(this, Constants.SENT_ACTIVATION_CODE_BUT_NOT_ENTERED_CASE, "" +phoneNumber);
        Intent intent = new Intent(this, ActivationCodeActivity.class);
        intent.putExtra("phone", phoneNumber );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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

    private void loginLogic() {
        phone = editPhone.getText().toString();
        codeKsa = txtCodeKsa.getText().toString();
        if (validateMobileNumber(phone) == null) {
            editPhone.setError(getString(R.string.enter_valid_mobile));
            return;
        }
        String validPhone = validateMobileNumber(phone);
        String newToken = FirebaseInstanceId.getInstance().getToken();
        phonenumber = codeKsa + validPhone;
        loginPresenter.loginPresenter(newToken, codeKsa + validPhone);

    }

    private String validateMobileNumber(String mobileNumber) {
        if (mobileNumber == null || TextUtils.isEmpty(mobileNumber)) return null;
        if (mobileNumber.length() == 9) {
            if (mobileNumber.startsWith("5")) return mobileNumber;
        } else if (mobileNumber.length() < 9) {
            return null;
        } else if (mobileNumber.length() == 10) {
            if (mobileNumber.charAt(0) == '0') {
                if (mobileNumber.charAt(1) == '5') {
                    return mobileNumber.substring(1, mobileNumber.length());

                } else {
                    return null;
                }
            }
        } else if (mobileNumber.length() == 12 || mobileNumber.length() == 13) {
            if (mobileNumber.startsWith("966")) {
                if (mobileNumber.charAt(3) == '0') {
                    return mobileNumber.substring(4, mobileNumber.length());
                }
                if (mobileNumber.charAt(3) == '5') {
                    return mobileNumber.substring(3, mobileNumber.length());

                }
            }
        }

        return null;
    }

    // ===============Related to login with firebase logic===================================================
    private void doLogin1() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                } else if (e instanceof FirebaseTooManyRequestsException) {
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
                // resendVerificationCode("+201063649515", token);
                // verifyPhoneNumberWithCode(verificationId , "123456");

            }
        };
        startPhoneNumberVerification("+201063649515");
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            final FirebaseUser user = task.getResult().getUser();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    /*Intent intent = new Intent();
                                    intent.putExtra("PHONE_NUMBER", user.getPhoneNumber());
                                    setResult(1080, intent);
                                    finish();*/
                                }
                            }, 500);
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                           /* Intent intent = new Intent();
                            intent.putExtra("PHONE_NUMBER", "");
                            setResult(1080, intent);
                            finish();*/
                        }
                    }
                });
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]
        // startCounter();
    }


    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }


    //=====================================================================
    public void GetContactsIntoArrayList() {

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        contactList = new ArrayList<>();
        while (cursor.moveToNext()) {

            Contact contact = new Contact();
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
            if (phonenumber != null) {
                phonenumber = phonenumber.replace("+", "");
            }
            contact.setName("" + name);
            contact.setPhone("" + phonenumber);
            contactList.add(contact);
        }

        cursor.close();

        MyDatabaseAdapter db = new MyDatabaseAdapter(this);
        db.open();
        for (int i = 0; i < contactList.size(); i++) {
            db.insertContacts(contactList.get(i).getName(), contactList.get(i).getPhone());
        }


        db.close();
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                LoginActivity.this,
                Manifest.permission.READ_CONTACTS)) {

            Toast.makeText(LoginActivity.this, "CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

//            GetContactsIntoArrayList();
        } else {

            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

  /*  @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(LoginActivity.this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                    GetContactsIntoArrayList();
                } else {

                    Toast.makeText(LoginActivity.this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }*/


}

package com.kingsms.archivesms.view.my_profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingsms.archivesms.BuildConfig;
import com.kingsms.archivesms.R;
import com.kingsms.archivesms.helper.Utilities;
import com.kingsms.archivesms.view.HomeActivity.HomeSenderNamesActivity;
import com.kingsms.archivesms.view.login.LoginActivity;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MyProfile extends AppCompatActivity implements InternetConnectivityListener {

    private TextView phoneTxt;
    private Button logoutBtn;
    private TextView connectedTxt;
    private TextView txtVersion;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        phoneTxt = findViewById(R.id.text_phone_profile);
        logoutBtn = findViewById(R.id.button_log_out);
        connectedTxt = findViewById(R.id.text_connected);
        txtVersion = findViewById(R.id.text_version);

        txtVersion.setText(getString(R.string.app_version) + " " + BuildConfig.VERSION_NAME);


        if (Utilities.retrieveUserInfo(this).getPhone() != null)
            phoneTxt.setText(Utilities.retrieveUserInfo(this).getPhone());
        else {
            if (Utilities.getPhoneActivatedCode(this) != null)
                phoneTxt.setText(Utilities.retrieveUserInfo(this).getPhone());

        }


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogLogout();

            }
        });

        InternetAvailabilityChecker.init(this);
        InternetAvailabilityChecker.getInstance().addInternetConnectivityListener(this);

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeSenderNamesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
//
        //updateView();
    }

    public void updateView() {
        // boolean isConnected = isOnline();

       /* if(isConnected)
        {
             connectedTxt.setText(getString(R.string.connected));
            connectedImage.setImageResource(R.drawable.ic_green_dot);

        }
        else
        {
            connectedTxt.setText(getString(R.string.not_connected));
            connectedImage.setImageResource(R.drawable.ic_red_dot);
        }

        */
    }

    //    public  boolean  isOnline()
//    {
//       boolean isConnected = Utilities.checkConnection();
//       if(isConnected) {
//           return true;
//       }
//       else
//           {
//               return false;
//
//           }
//
//    }
    private void showDialogLogout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyProfile.this);
        alertDialog.setMessage(getString(R.string.log_out_note));
        alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Utilities.clearUserInfo(MyProfile.this);
                Utilities.clearAllUserInfo(MyProfile.this);

                Intent intent = new Intent(MyProfile.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                MyProfile.this.finish();

            }
        });
        alertDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {

        if (isConnected) {
            connectedTxt.setText(getString(R.string.connected));
            //connectedImage.setImageResource(R.drawable.ic_green_dot);

        } else {
            connectedTxt.setText(getString(R.string.not_connected));
            //connectedImage.setImageResource(R.drawable.ic_red_dot);
        }
    }
}

package com.kingsms.archivesms.helper;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.kingsms.archivesms.dagger.DaggerApplication;


// this class created to track internet connectivity live preview

public class ConnectivityReceiver extends BroadcastReceiver {
    public static ConnectivityReceiverListener connectivityReceiverListener;

    public ConnectivityReceiver() {
        super();
    }

    //this function created  for return this status of internet
    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) DaggerApplication.getDaggerApplication().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        int wifiStatus = 0;
        if (activeNetwork == null) {
            WifiManager
                    cmWifi = (WifiManager) DaggerApplication.getDaggerApplication().getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            wifiStatus = cmWifi.getWifiState();

        }
        return (activeNetwork != null
                && activeNetwork.isConnectedOrConnecting()) || wifiStatus == 1;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }

    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

}
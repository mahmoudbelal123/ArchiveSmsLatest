package com.kingsms.archivesms.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.kingsms.archivesms.model.activation_code.ActivationResponse;

import java.util.Locale;


public class Utilities {


    public static SharedPreferences sharedPreferences;
    public static String SharedPreferencesName = "user_info";
    public static String SharedPreferences_token_key = "token";


    public static String SharedPreferencesActivationCodeName = "activation_code";
    public static String SharedPreferences_is_activated_key = "isActivated";
    public static String SharedPreferences_is_activated_phone_key = "phone";


    public static void setActivatedCode(Context context, int activationStatus, String phone) {
        sharedPreferences = context.getSharedPreferences(SharedPreferencesActivationCodeName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SharedPreferences_is_activated_key, activationStatus);
        editor.putString(SharedPreferences_is_activated_phone_key, phone);
        editor.commit();

    }

    public static int getActivatedCode(Context context) {
        sharedPreferences = context.getSharedPreferences(SharedPreferencesActivationCodeName, 0);
        int activationStatus = sharedPreferences.getInt(SharedPreferences_is_activated_key, 0);
        return activationStatus;
    }

    public static String getPhoneActivatedCode(Context context) {
        sharedPreferences = context.getSharedPreferences(SharedPreferencesActivationCodeName, 0);
        String phone = sharedPreferences.getString(SharedPreferences_is_activated_phone_key, "");
        return phone;
    }

    public static void saveUserInfo(Context context, ActivationResponse activationResponse) {
        sharedPreferences = context.getSharedPreferences(SharedPreferencesName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(activationResponse);
        editor.putString(SharedPreferences_token_key, json);
        editor.commit();

    }

    public static void clearUserInfo(Context context) {
        sharedPreferences = context.getSharedPreferences(SharedPreferencesName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }

    public static void clearAllUserInfo(Context context) {
        sharedPreferences = context.getSharedPreferences(SharedPreferencesActivationCodeName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }


    public static ActivationResponse retrieveUserInfo(Context context) {
        sharedPreferences = context.getSharedPreferences(SharedPreferencesName, 0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SharedPreferences_token_key, null);
        ActivationResponse obj = gson.fromJson(json, ActivationResponse.class);

        return obj;
    }


    //   return internet status
    public static boolean checkConnection() {
        if (ConnectivityReceiver.isConnected()) {
            return true;
        } else {
            return false;
        }

    }


    public static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }


}

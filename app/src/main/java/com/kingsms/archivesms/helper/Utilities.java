package com.kingsms.archivesms.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.kingsms.archivesms.model.activation_code.ActivationResponse;

import java.util.Locale;


public class Utilities {


    public static SharedPreferences sharedPreference;
    public static String SharedPreferencesName = "user_info_";
    public static String SharedPreferences_token_key = "token_";


    public static String SharedPreferencesActivationCodeName = "activation_code_";
    public static String SharedPreferences_is_activated_key = "isActivated_";
    public static String SharedPreferences_is_activated_phone_key = "phone_";


    public static void setActivatedCode(Context context, int activationStatus, String phone) {
        sharedPreference = context.getSharedPreferences(SharedPreferencesActivationCodeName, 0);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putInt(SharedPreferences_is_activated_key, activationStatus);
        editor.putString(SharedPreferences_is_activated_phone_key, phone);
        editor.commit();

    }

    public static int getActivatedCode(Context context) {
        sharedPreference = context.getSharedPreferences(SharedPreferencesActivationCodeName, 0);
        int activationStatus = sharedPreference.getInt(SharedPreferences_is_activated_key, 0);
        return activationStatus;
    }

    public static String getPhoneActivatedCode(Context context) {
        sharedPreference = context.getSharedPreferences(SharedPreferencesActivationCodeName, 0);
        String phone = sharedPreference.getString(SharedPreferences_is_activated_phone_key, "");
        return phone;
    }

    public static void saveUserInfo(Context context, ActivationResponse activationResponse) {
        sharedPreference = context.getSharedPreferences(SharedPreferencesName, 0);
        SharedPreferences.Editor editor = sharedPreference.edit();
        Gson gson = new Gson();
        String json = gson.toJson(activationResponse);
        editor.putString(SharedPreferences_token_key, json);
        editor.commit();

    }

    public static void clearUserInfo(Context context) {
        sharedPreference = context.getSharedPreferences(SharedPreferencesName, 0);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.clear();
        editor.commit();

    }

    public static void clearAllUserInfo(Context context) {
        sharedPreference = context.getSharedPreferences(SharedPreferencesActivationCodeName, 0);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.clear();
        editor.commit();

    }


    public static ActivationResponse retrieveUserInfo(Context context) {
        sharedPreference = context.getSharedPreferences(SharedPreferencesName, 0);
        Gson gson = new Gson();
        String json = sharedPreference.getString(SharedPreferences_token_key, null);
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

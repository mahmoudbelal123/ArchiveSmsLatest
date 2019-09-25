package com.kingsms.archivesms.helper;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;

import java.util.Locale;


public class AppSettings {

    private static final String TAG = "APP_SETTINGS";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void setLang(Context context, String lang) {
        lang = lang.equalsIgnoreCase("ar") ? "ar" : "en";
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("txt", lang).apply();
        Configuration newConfig = new Configuration(context.getResources().getConfiguration());
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        newConfig.locale = locale;
        newConfig.setLayoutDirection(locale);
        context.getResources().updateConfiguration(newConfig, null);
    }

    public static String getLang(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("txt", "ar");
    }

}

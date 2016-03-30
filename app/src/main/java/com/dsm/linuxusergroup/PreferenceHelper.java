package com.dsm.linuxusergroup;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by dsm on 3/30/16.
 */
public class PreferenceHelper {

    //preference names
    final public static String TIME_LATEST_EVENTS = "time_latest_events";

    public static long getLong(Context context, String name, long defVal) {
        return  PreferenceManager.getDefaultSharedPreferences(context).getLong(name, defVal);
    }

    public static String getString(Context context, String name, String defVal) {
        return Utils.emptyToNull(PreferenceManager.getDefaultSharedPreferences(context).getString(name, defVal));
    }

    public static void putLong(Context context, String name, long value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(name, value).commit();
    }

    public static void putString(Context context, String name, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(name, value).commit();
    }
}
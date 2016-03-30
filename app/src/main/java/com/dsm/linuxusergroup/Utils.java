package com.dsm.linuxusergroup;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by dsm on 3/30/16.
 */
public class Utils {

    public static String emptyToNull(String string) {
        if ("".equals(string)) {
            return null;
        }
        return string;
    }

    public static long parseLong(String string, long defVal) {
        try {
            return Long.valueOf(string);
        } catch (NumberFormatException e) {
            return defVal;
        }
    }

    public static int getAccentColor(Context context) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        return value.data;
    }
}

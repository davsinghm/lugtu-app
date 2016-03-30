package com.dsm.linuxusergroup;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Cache {

    public static <T> void load(Context context, T t, ArrayList<T> arrayList, String fileName) {
        String cacheStr = FileUtils.readCache(context, fileName);
        if (cacheStr != null) {
            try {

                JSONArray jsonArray = new JSONArray(cacheStr);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jso = jsonArray.getJSONObject(i);
                    if (t instanceof Event)
                        t = (T) new Event().fromJSON(jso);
                    // can add more
                    else t = null;

                    if (t != null)
                        arrayList.add(t);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    public static <T> String arrayToJSONArray(ArrayList<T> arrayList) {
        JSONArray jsonArray = new JSONArray();

        try {
            for (T t : arrayList) {
                String str = t.toString();
                if (str == null)
                    continue;
                JSONObject jsonObject = new JSONObject(str);
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            return null;
        }
        return jsonArray.toString();
    }

}

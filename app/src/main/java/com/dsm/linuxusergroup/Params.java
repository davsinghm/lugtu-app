package com.dsm.linuxusergroup;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class Params {

    private HashMap<String ,String> hashMap;

    public Params() {
        hashMap = new HashMap<>();
    }

    public Params add(String key, String value) {
        hashMap.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        if (hashMap.isEmpty())
            return null;
        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : hashMap.keySet()) {
            try {
                if (i != 0) {
                    sbParams.append("&");
                }
                sbParams.append(key).append("=").append(URLEncoder.encode(hashMap.get(key), "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                return null;
            }
            i++;
        }
        return sbParams.toString();
    }
}

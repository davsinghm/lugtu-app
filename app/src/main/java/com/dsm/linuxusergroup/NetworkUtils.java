package com.dsm.linuxusergroup;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dsm on 3/30/16.
 */
public class NetworkUtils {

    final static int READ_TIMEOUT = 15;
    final static int CONNECT_TIMEOUT = 15;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public static String makeHttpRequest(String urlStr, Params params) throws IOException {

        HttpURLConnection httpURLConnection;


        if (params.toString() != null) {
            urlStr += "?" + params.toString();
        }

        URL url = new URL(urlStr);

        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoOutput(false);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
        httpURLConnection.setReadTimeout(READ_TIMEOUT * 1000);
        httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT * 1000);
        httpURLConnection.connect();

        InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();

    }

}

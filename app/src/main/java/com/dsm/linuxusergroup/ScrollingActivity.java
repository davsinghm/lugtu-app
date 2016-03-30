package com.dsm.linuxusergroup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ScrollingActivity extends AppCompatActivity implements DoInBackground.Callback {

    private TextView textView;
    private ImageView imageView;
    private DoInBackground doInBackground = null;
    private Context context;
    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        eventID = getIntent().getStringExtra("event_id");

        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Will do something", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        doInBackground = getDoInBackground();
        doInBackground.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (doInBackground != null)
            doInBackground.cancel(true);

    }


    @Override
    public void onPreExecute() {

        if (!NetworkUtils.isNetworkAvailable(context)) {
            doInBackground.cancel(true);
            showError("OFFLINE");
        }

    }

    @Override
    public void onCancelled() {
        doInBackground = null;
    }

    @Override
    public String doInBackground(String... strings) {

        try {
            Params params = new Params();
            if (eventID != null)
                params.add("event", eventID + "");
            params.add("option", "2");

            return NetworkUtils.makeHttpRequest(Constants.URL_SERVER_EVENT, params);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onPostExecute(String result) {

        doInBackground = null;

        try {

            if (Utils.emptyToNull(result) != null) {

                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                textView.setText(jsonObject.getString("description"));
                String imageUrl = jsonObject.getString("poster_img_url");
                Picasso.with(context).load(Uri.parse(Constants.URL_SERVER + imageUrl)).into(imageView);


            } else
                showError("NETWORK_ERROR");

        } catch (JSONException e) {
            e.printStackTrace();
            showError("NETWORK_ERROR");
        }

    }

    private void showError(String error) {
        if (error != null) {
            switch (error) {
                case "OFFLINE":
                    showOfflineSnackbar(false);
                    break;
                case "NETWORK_ERROR":
                    showOfflineSnackbar(true);
                    break;
                default:
                    Toast.makeText(context, "Error: " + error, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showOfflineSnackbar(boolean error) {
        if (error)
            Toast.makeText(context, "Couldn't connect to internet.", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, "You're offline", Toast.LENGTH_SHORT).show();

    }

    private DoInBackground getDoInBackground() {
        return new DoInBackground(context, this, "Please wait...");
    }
}

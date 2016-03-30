package com.dsm.linuxusergroup;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by dsm on 3/30/16.
 */
public class EventsFragment extends Fragment implements DoInBackground.Callback {

    private DoInBackground doInBackground = null;
    private EventAdapter adapter;
    private String lastTimestamp;
    private CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;
    private ArrayList<Event> eventList;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        eventList = new ArrayList<>();
        lastTimestamp = PreferenceHelper.getString(getContext(), PreferenceHelper.TIME_LATEST_EVENTS, null);

        if (lastTimestamp != null && FileUtils.readCache(getContext(), Constants.FILE_EVENTS) == null) {
            //checking if file doesn't exists (when read/write fails) and lastTimestamp should be made 0
            lastTimestamp = null;
        }

        Cache.load(getContext(), new Event(), eventList, Constants.FILE_EVENTS);

        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Utils.getAccentColor(getContext()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doInBackground = getDoInBackground();
                doInBackground.execute();
            }
        });
        adapter = new EventAdapter(eventList);

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        doInBackground = getDoInBackground();
        doInBackground.execute();
    }


    @Override
    public void onStop() {
        super.onStop();

        if (doInBackground != null)
            doInBackground.cancel(true);

    }


    @Override
    public void onPreExecute() {

        if (snackbar != null)
            snackbar.dismiss();

        if (!NetworkUtils.isNetworkAvailable(getContext())) {
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
            params.add("type", "1");
            params.add("option", "3");
            if (lastTimestamp != null)
                params.add("ts", lastTimestamp);

            return NetworkUtils.makeHttpRequest(Constants.URL_SERVER_API, params);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onPostExecute(String result) {

        doInBackground = null;

        int newAdded = 0, modified = 0; //TODO remove debug

        try {

            if (Utils.emptyToNull(result) != null) {

                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jso = jsonArray.getJSONObject(i);
                    Event event = new Event().fromJSON(jso);
                    if (event != null) {
                        if (eventList.contains(event)) {
                            int index = eventList.indexOf(event);
                            eventList.remove(index);
                            eventList.add(index, event);
                            modified++;
                        } else {
                            eventList.add(i, event);
                            newAdded++;
                        }
                    }
                }
                //Save current timestamp
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(System.currentTimeMillis());
                String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();

                PreferenceHelper.putString(getContext(), PreferenceHelper.TIME_LATEST_EVENTS, date); //todo

            } else
                showError("NETWORK_ERROR");

        } catch (JSONException e) {
            e.printStackTrace();
            showError("NETWORK_ERROR");
        }

        if (newAdded > 0) {
            String notiStr = Cache.arrayToJSONArray(eventList);
            if (notiStr != null)
                FileUtils.writeStringCache(getContext(), Constants.FILE_EVENTS, notiStr);
        }

        adapter.setArrayList(eventList);
        adapter.notifyDataSetChanged();
        //TODO remove this
        Toast.makeText(getContext(), "New: " + newAdded + ", Modified: " + modified + ", Total: " + eventList.size(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showOfflineSnackbar(boolean error) {
        snackbar = Snackbar.make(coordinatorLayout, error ? "Couldn't connect to internet" : "You're offline", Snackbar.LENGTH_INDEFINITE).setAction("Try Again", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doInBackground = getDoInBackground();
                doInBackground.execute();
            }
        });
        snackbar.show();
    }

    private DoInBackground getDoInBackground() {
        return new DoInBackground(this, swipeRefreshLayout);
    }

}

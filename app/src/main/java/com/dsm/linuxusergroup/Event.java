package com.dsm.linuxusergroup;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dsm on 3/30/16.
 */
public class Event {

    private String id;
    private String title;
    private String startTs;
    private String endTs;
    private String location;
    private String posterLink;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTs() {
        return startTs;
    }

    public void setStartTs(String startTs) {
        this.startTs = startTs;
    }

    public String getEndTs() {
        return endTs;
    }

    public void setEndTs(String endTs) {
        this.endTs = endTs;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPosterLink() {
        return posterLink;
    }

    public void setPosterLink(String posterLink) {
        this.posterLink = posterLink;
    }

    public Event fromJSON(JSONObject jsonObject) {
        try {
            setTitle(jsonObject.getString("title"));
            setId(jsonObject.getString("event_id"));
            setStartTs(jsonObject.getString("start_ts"));
            setEndTs(jsonObject.getString("start_ts"));
            setLocation(jsonObject.getString("location"));
            setPosterLink(jsonObject.getString("poster_img_url"));

            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("event_id", getId());
            jsonObject.put("title", getTitle());
            jsonObject.put("start_ts", getStartTs());
            jsonObject.put("end_ts", getEndTs());
            jsonObject.put("location", getLocation());
            jsonObject.put("poster_img_url", getPosterLink());

            return jsonObject.toString();
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return !(id != null ? !id.equals(event.id) : event.id != null);

    }
}

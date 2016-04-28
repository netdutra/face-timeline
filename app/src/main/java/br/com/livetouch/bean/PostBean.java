package br.com.livetouch.bean;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostBean {

    private String story = "";
    private String created_time = "";
    private String message =  "";

    public PostBean(JSONObject json ) {
        if (json == null || json.length() <= 0) {
            Log.e("", "[ PostBean Construtor: Erro criar os posts.]");
            return;
        }

        try {
            Object obj = json.get("story");
            story = (obj == null ? "" : obj.toString());

            obj = json.get("created_time");
            created_time = (obj == null ? "" : obj.toString());

            obj = json.get("message");
            message = (obj == null ? "" : obj.toString());

        } catch (JSONException ex){
        }
    }

    public Date createdDate() throws ParseException {
        SimpleDateFormat incomingFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        return incomingFormat.parse(this.created_time);
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

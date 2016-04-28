package br.com.livetouch.bean;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class UserBean {

    private String facebookID;

    private String name;

    private String email;

    private String gender;

    private String token;

    public UserBean(JSONObject json) {
        if (json == null || json.length() <= 0) {
            Log.e("", "Erro ao instanciar o usuário.");
            return;
        }

        try {
            Object obj = json.get("id");
            facebookID = (obj == null ? "" : obj.toString());


            obj = json.get("name");
            name = (obj == null ? "" : obj.toString());

            obj = json.get("gender");
            gender = (obj == null ? "" : obj.toString());

            obj = json.get("email");
            email = (obj == null ? "" : obj.toString());

        } catch (JSONException ex) {
        }
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
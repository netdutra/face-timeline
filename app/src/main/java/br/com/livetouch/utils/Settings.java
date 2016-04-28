package br.com.livetouch.utils;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.livetouch.bean.UserBean;

public class Settings {

    private static Settings settings;

    public final SharedPreferences preferences;

    public final static String ID_USUARIO_FACE = "id_usuario_facebook";
    public final static String NAME   = "name";
    public final static String TOKEN = "token";

    public static UserBean user;

    public static Settings getInstance(Context context) {
        if (settings == null) {
            settings = new Settings(context);
        }
        return settings;
    }

    private Settings(Context context) {
        preferences = context.getSharedPreferences("br.com.livetouch.app", Context.MODE_WORLD_WRITEABLE);
    }

    public static String getTOKEN() {
        return TOKEN;
    }

    public String getIdUsuario() {
        return preferences.getString(ID_USUARIO_FACE, "");
    }

    public void setIdUsuario(String idUsuario) {
        if (!Utils.empty(idUsuario)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(ID_USUARIO_FACE, idUsuario);
            editor.commit();
        }
    }

    public String getName() {
        return preferences.getString(NAME, "");
    }

    public void setName(String longitude) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NAME, longitude);
        editor.commit();
    }


    public static Settings getSettings() {
        return settings;
    }

    public static void setSettings(Settings settings) {
        Settings.settings = settings;
    }

    public static UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
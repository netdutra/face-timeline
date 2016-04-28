package br.com.livetouch.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.livetouch.bean.PostBean;

public class Utils {

    private static final String TAG = "AndroidUtils";

    private static Bitmap bitmap;

    /***
     * Verifica se existe conexão.
     *
     * @param contextAtual
     * @return (True) se existir conexão.
     */
    public static boolean isNetworkAvaible(Context contextAtual) {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) contextAtual.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
                return false;
            } else {
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        } catch (SecurityException ex) {
            Toast.makeText(contextAtual, ex.getClass().getSimpleName() + " " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    public static boolean empty(String text) {
        return text == null || text.equals("") ? true : false;
    }

    public static void loadPictureLarge(final ImageView imagem) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                URL imageURL = null;
                try {
                    imageURL = new URL("https://graph.facebook.com/" + Settings.user.getFacebookID() + "/picture?type=large&width=583");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                imagem.setImageBitmap(bitmap);
            }
        }.execute();
    }

    public static void loadPictureSmall(final ImageView imagem) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                URL imageURL = null;
                try {
                    imageURL = new URL("https://graph.facebook.com/" + Settings.user.getFacebookID() + "/picture?type=small");
//                    imageURL = new URL("https://graph.facebook.com/" + Settings.user.getFacebookID() + "/picture?type=large&width=50");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                imagem.setImageBitmap(bitmap);
            }
        }.execute();
    }

    public static List<PostBean> parseResultPost(JSONObject result) {
        Log.i(TAG, "[parseResultPost] " + result);

        List<PostBean> listPosts = null;
        try {
            JSONObject response = result;
            JSONArray posts = response.optJSONArray("data");
            if(posts!=null) {
                listPosts = new ArrayList<PostBean>();
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.optJSONObject(i);
                    if(post !=null){
                        PostBean itemFilm = new PostBean(post);
                        Log.i(TAG, itemFilm.getMessage());
                        listPosts.add(itemFilm);
                    } else {
                        Log.i(TAG, "Erro ao carregar a lista de Posts.");
                    }
                }
            } else {
                Log.i(TAG, "[parseResultSearch] é null" );
            }
        } catch (Exception e) {
            Log.i(TAG, "[parseResultPost] JSONException: " + e.getMessage());
            e.printStackTrace();
        }
        return listPosts;
    }
}

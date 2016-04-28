package br.com.livetouch.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

import br.com.livetouch.bean.UserBean;
import br.com.livetouch.utils.Settings;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private LoginButton btLogin;

    private CallbackManager callbackManager;

    private AccessToken accessToken;

    private String token = "";

    private UserBean user;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        loadFacebook();

        btLogin = (LoginButton) findViewById(R.id.btFacebook);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                } else {
                    loadFacebook();
                }
            }
        });
    }

    private void loadFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "email", "user_friends", "user_posts"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                token = loginResult.getAccessToken().getToken();
                Log.i(TAG, "[ Facebook token ]" + token);
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {

                                    user = new UserBean(object);
                                    if (user != null) {
                                        user.setToken(token);
                                        Settings.getInstance(MainActivity.this).setUser(user);
                                        startActivity(new Intent(MainActivity.this, TimelineActivity.class));
                                        finish();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "[ Facebook onCancel ]");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.i(TAG, "[ Facebook onError ]");
                Toast.makeText(MainActivity.this, "Erro ao tentar efetuar o login: " + exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();
    }

}
package br.com.livetouch.app;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.livetouch.bean.PostBean;
import br.com.livetouch.utils.Settings;
import br.com.livetouch.utils.Utils;

public class TimelineActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Toolbar mToolbar;

    private DrawerLayout mDrawerLayout;

    private NavigationView mNavigationView;

    private final static String TAG = "TimeLineActivity";

    private ImageView imgMenu;
    private ImageView imgHeader;

    private TextView txtUsuario;

    private Bitmap bitmap;

    private List<PostBean> postsList = new ArrayList<PostBean>();

    private RecyclerView mRecyclerView;

    private PostAdapter adapter;

    private JSONObject objResponsePosts = null;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_timelime);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        txtUsuario = (TextView) findViewById(R.id.txtNomeUsuario);
        txtUsuario.setText(Settings.user.getName());
        setUpToolbar();

        setUpNavDrawer();

        setUpNavigationView();

        imgMenu = (ImageView) findViewById(R.id.imgMenu);
        Utils.loadPictureLarge(imgMenu);
        imgHeader = (ImageView) findViewById(R.id.imgHeader);
        Utils.loadPictureLarge(imgHeader);

        mRecyclerView = (RecyclerView) findViewById(R.id.listRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        readPost();
    }

    private void readPost() {
        try {
            AccessToken token = AccessToken.getCurrentAccessToken();
            GraphRequest request = new GraphRequest(token, "/me/feed/", null, HttpMethod.GET, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    Log.i(TAG, "[ readPost: onCompleted ] response: " + response);
                    objResponsePosts = response.getJSONObject();
                    if (objResponsePosts != null) {
                        postsList = Utils.parseResultPost(objResponsePosts);
                        if (postsList != null) {
                            adapter = new PostAdapter(TimelineActivity.this, postsList, onClickPost());
                            mRecyclerView.setAdapter(adapter);
                        }
                    }
                }
            });
            request.executeAsync();
        } catch (Exception ex) {
            Log.i(TAG, "[readPost] Exception");
        }
    }

    private void setUpNavigationView() {
        mNavigationView = (NavigationView) findViewById(R.id.navView);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.Sair:
                        LoginManager.getInstance().logOut();
                        finish();
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_drawer);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    protected PostAdapter.PostOnClickListener onClickPost() {

        return new PostAdapter.PostOnClickListener() {
            @Override
            public void onClickPost(View view, int idx) {
                // Elemento da lista sem ação ao clicar.
//                PostBean p = postsList.get(idx);
//
//                ActivityOptionsCompat opts = ActivityOptionsCompat.makeSceneTransitionAnimation(TimelineActivity.this, null, "keyNet");
//                ActivityCompat.startActivity(TimelineActivity.this, intent, opts.toBundle());
            }
        };
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void show(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
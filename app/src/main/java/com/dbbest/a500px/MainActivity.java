package com.dbbest.a500px;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dbbest.a500px.data.storage.User;
import com.dbbest.a500px.fivehundredpx.api.FiveHundredException;
import com.dbbest.a500px.fivehundredpx.api.PxApi;
import com.dbbest.a500px.fivehundredpx.api.auth.AccessToken;
import com.dbbest.a500px.fivehundredpx.api.tasks.UserDetailTask;
import com.dbbest.a500px.fivehundredpx.api.tasks.XAuth500pxTask;

import org.json.JSONException;
import org.json.JSONObject;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import timber.log.Timber;

@SuppressFBWarnings(value = "UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
public class MainActivity extends AppCompatActivity implements
        XAuth500pxTask.Delegate, UserDetailTask.Delegate {

    private static final int CODE_GET_PHOTOS = 1;

    private RelativeLayout loadingView;
    private RelativeLayout relativeLayout1;

    private EditText passText;
    private EditText loginText;
    private Button loginBtn;

    private User user;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = new User();
        SharedPreferences preferences = getSharedPreferences(App.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        final String accessToken = preferences.getString(App.PREF_ACCESS_TOKEN, null);
        final String tokenSecret = preferences
                .getString(App.PREF_TOKEN_SECRET, null);

        if (null != accessToken && null != tokenSecret) {
            onSuccess(new AccessToken(accessToken, tokenSecret));
        }

        loginBtn = (Button) findViewById(R.id.login_btn);
        loadingView = (RelativeLayout) findViewById(R.id.loadingView);
        loginText = (EditText) findViewById(R.id.login_email);
        passText = (EditText) findViewById(R.id.login_password);
        relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showSpinner();
                final XAuth500pxTask loginTask = new XAuth500pxTask(
                        MainActivity.this);
                loginTask.execute(getString(R.string.px_consumer_key),
                        getString(R.string.px_consumer_secret), loginText
                                .getText().toString(), passText.getText()
                                .toString());
            }
        });
    }

    @Override
    public void onSuccess(AccessToken result) {
//        showSpinner();

        SharedPreferences preferences = getSharedPreferences(App.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(App.PREF_ACCESS_TOKEN, result.getToken());
        editor.putString(App.PREF_TOKEN_SECRET, result.getTokenSecret());
        editor.apply();


        final PxApi api = new PxApi(result,
                getString(R.string.px_consumer_key),
                getString(R.string.px_consumer_secret));

        new UserDetailTask(this).execute(api);

    }

    private void showSpinner() {
        loginBtn.setEnabled(false);
        loadingView.setVisibility(View.VISIBLE);
        relativeLayout1.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }

    private void hideSpinner() {
        loginBtn.setEnabled(true);
        loadingView.setVisibility(View.GONE);
        relativeLayout1.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
    }

    @Override
    public void onSuccess(JSONObject user) {
        try {
            this.user.fullName = user.getString("fullName");
            Toast.makeText(MainActivity.this,
                    this.user.fullName + ", You access is Granted!", Toast.LENGTH_LONG)
                    .show();
            moveToGallery();

            Timber.i("get Oauth information");
        } catch (JSONException e) {
            Timber.e(e);
        }
        hideSpinner();


    }

    @Override
    public void onFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideSpinner();
                Toast.makeText(MainActivity.this,
                        "Login Failed, please try again.", Toast.LENGTH_LONG)
                        .show();

            }
        });

    }

    @Override
    public void onFail(FiveHundredException e) {
        onFail();
    }

    public void moveToGallery() {
        Intent intent = App.graph().navigation().galleryActivity(this);
        startActivity(intent);
        finish();
    }

}

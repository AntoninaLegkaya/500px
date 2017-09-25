package com.dbbest.a500px.fivehundredpx.api.tasks;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.dbbest.a500px.fivehundredpx.api.FiveHundredException;
import com.dbbest.a500px.fivehundredpx.api.auth.AccessToken;
import com.dbbest.a500px.fivehundredpx.api.auth.OAuthAuthorization;
import com.dbbest.a500px.fivehundredpx.api.auth.XAuthProvider;

import timber.log.Timber;


public class XAuth500pxTask extends AsyncTask<String, Void, AccessToken> {
    private static final String TAG = "XAuth500pxTask";
    private Delegate _d;

    public XAuth500pxTask(Delegate delegate) {
        this._d = delegate;
    }

    @Override
    protected AccessToken doInBackground(String... params) {
        final String consumerKey = params[0];
        final String consumerSecret = params[1];
        final String _user = params[2];
        final String _password = params[3];

        try {

            final OAuthAuthorization oauth = new OAuthAuthorization.Builder()
                    .consumerKey(consumerKey)
                    .consumerSecret(consumerSecret)
                    .build();

            final AccessToken accessToken = oauth
                    .getAccessToken(new XAuthProvider(_user, _password));

            return accessToken;

        } catch (FiveHundredException e) {
            @SuppressLint("DefaultLocale") final String msg = String.format("error %d for username[%s]",
                    e.getStatusCode(), _user);
            Timber.e(" %s", msg);
            _d.onFail(e);
        }

        return null;

    }

    @Override
    protected void onPostExecute(AccessToken result) {

        if (null != result)
            _d.onSuccess(result);

    }

    public interface Delegate {
        public void onSuccess(AccessToken result);

        public void onFail(FiveHundredException e);
    }
}

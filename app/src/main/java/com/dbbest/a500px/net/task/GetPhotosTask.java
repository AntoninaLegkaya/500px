package com.dbbest.a500px.net.task;

import android.content.Context;
import android.os.AsyncTask;

import com.dbbest.a500px.App;
import com.dbbest.a500px.R;
import com.dbbest.a500px.fivehundredpx.api.PxApi;
import com.dbbest.a500px.fivehundredpx.api.auth.AccessToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetPhotosTask extends AsyncTask<Object, Void, JSONObject> {

    private final Delegate delegate;

    public GetPhotosTask(Delegate delegate) {
        super();
        this.delegate = delegate;
    }

    @Override
    protected JSONObject doInBackground(Object... params) {
        AccessToken accessToken = (AccessToken) params[0];
        String name = (String) params[1];
        String desc = (String) params[2];
        final Context context = App.instance();

        final ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("name", name));
        postParams.add(new BasicNameValuePair("description", desc));
        // dont post to profile. only to library
        postParams.add(new BasicNameValuePair("privacy", "1"));


        final PxApi api = new PxApi(accessToken,
                context.getString(R.string.px_consumer_key),
                context.getString(R.string.px_consumer_secret));
        JSONObject json = api.post("/photos", postParams);

//            Log.w(TAG,json.getJSONObject("photo").getString("id"));

        return json;
    }

    @Override
    protected void onPostExecute(JSONObject result) {

        if (null != result) {
            delegate.success(result);
        }
    }

    public interface Delegate {
        void success(JSONObject obj);

        void fail();
    }
}

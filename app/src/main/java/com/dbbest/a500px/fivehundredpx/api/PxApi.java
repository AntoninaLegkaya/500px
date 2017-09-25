package com.dbbest.a500px.fivehundredpx.api;

import android.annotation.SuppressLint;

import com.dbbest.a500px.fivehundredpx.api.auth.AccessToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import timber.log.Timber;

@SuppressFBWarnings(value = "DM_DEFAULT_ENCODING")
public class PxApi {

    private static String HOST = "https://api.500px.com/v1";

    private AccessToken accessToken;
    private String consumerKey;
    private String consumerSecret;

    public PxApi(AccessToken accessToken, String consumerKey,
                 String consumerSecret) {
        super();
        this.accessToken = accessToken;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }

    public PxApi(String consumerKey) {
        super();
        this.consumerKey = consumerKey;
    }

    public JSONObject get(String url) {
        if (null != accessToken) {
            HttpGet request = new HttpGet(HOST + url);
            return handleSigned(request);
        } else {
            final String finalUrl = String.format("%s%s&consumer_key=%s", HOST,
                    url, this.consumerKey);
            return handle(new HttpGet(finalUrl));
        }
    }

    public JSONObject post(String url, List<? extends NameValuePair> params) {
        HttpPost request = new HttpPost(HOST + url);
        try {
            if (params != null) request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Timber.e("Parameters in post are invalid");
        }
        return handleSigned(request);
    }

    private void signRequest(HttpUriRequest request)
            throws OAuthMessageSignerException,
            OAuthExpectationFailedException, OAuthCommunicationException {
        CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(
                consumerKey, consumerSecret);
        consumer.setTokenWithSecret(accessToken.getToken(),
                accessToken.getTokenSecret());
        consumer.sign(request);
    }

    private JSONObject handleSigned(HttpUriRequest request) {
        try {
            signRequest(request);
        } catch (Exception e) {
            Timber.e(e);
        }
        return handle(request);
    }

    @SuppressFBWarnings(value = "OS_OPEN_STREAM")
    private JSONObject handle(HttpUriRequest request) {
        try {
            DefaultHttpClient client = new DefaultHttpClient();

            HttpResponse response = client.execute(request);
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                @SuppressLint("DefaultLocale") final String msg = String.format(
                        "Error, statusCode not OK(%d). for url: %s",
                        statusCode, request.getURI().toString());
                Timber.e("Message %s", msg);
                return null;
            }

            HttpEntity responseEntity = response.getEntity();
            InputStream inputStream = responseEntity.getContent();
            BufferedReader r = new BufferedReader(new InputStreamReader(
                    inputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            JSONObject json = new JSONObject(total.toString());
            return json;
        } catch (Exception e) {
            Timber.e("Error obtaining response from 500px api. %s", e.toString());
        }
        return null;
    }

}

package com.dbbest.a500px.fivehundredpx.api.tasks;

import android.os.AsyncTask;

import com.dbbest.a500px.fivehundredpx.api.PxApi;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;


public class UserDetailTask extends AsyncTask<Object, Void, JSONObject> {

	private String url = "/users.json";

	public interface Delegate {
		public void onSuccess(JSONObject user);
		public void onFail();
	}

	private Delegate _d;

	public UserDetailTask(Delegate d) {
		this._d = d;
	}

	@Override
	protected JSONObject doInBackground(Object... params) {
		final PxApi api = (PxApi) params[0];
		
		final JSONObject obj = api.get(url);
		try {
			return obj.getJSONObject("user");
		} catch (JSONException e) {
			Timber.e(e);
			_d.onFail();
			return null;
		}
	}

	@Override
	protected void onPostExecute(JSONObject result) {

		if (result != null)
			_d.onSuccess(result);
	}

}

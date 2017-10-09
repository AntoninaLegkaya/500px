package com.dbbest.a500px.net.service;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

@SuppressLint("RestrictedApi")
public class ExecuteResultReceiver extends ResultReceiver {

    private Receiver receiver;

    public ExecuteResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver r) {
        this.receiver = r;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiver != null) {
            receiver.onReceiveResult(resultCode, resultData);
        }
    }

    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle resultData);
    }
}


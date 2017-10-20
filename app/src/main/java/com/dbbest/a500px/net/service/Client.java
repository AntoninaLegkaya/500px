package com.dbbest.a500px.net.service;

public interface Client {


    void onRequestStatusRunning();

    void onRequestStatusSuccess();

    void onRequestStatusFail();
}

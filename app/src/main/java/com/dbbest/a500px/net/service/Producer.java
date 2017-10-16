package com.dbbest.a500px.net.service;

import android.os.Handler;

public interface Producer {

    void executeService();

    void refreshData();

    void removeClient(Client client);

    void addClient(Client client);

    void registerHandler(Handler handler);

    boolean isLoading();


}

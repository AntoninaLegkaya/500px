package com.dbbest.a500px.net.service;

import android.content.Intent;
import android.os.Handler;

public interface Producer {

    void executeService(final Intent workIntent);

    void removeClient(Client client);

    void addClient(Client client);

    void registerHandler(Handler handler);

}

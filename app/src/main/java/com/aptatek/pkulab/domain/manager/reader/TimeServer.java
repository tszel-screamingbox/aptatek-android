package com.aptatek.pkulab.domain.manager.reader;

import android.support.annotation.NonNull;

public interface TimeServer {

    void startServer(@NonNull TimeServerCallbacks timeServerCallbacks);

    void stopServer(@NonNull TimeServerCallbacks timeServerCallbacks);

}

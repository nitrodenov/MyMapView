package com.mymapview.presentation.view.activity.base;

import com.mymapview.presentation.Lifecycle;


public interface ActivityLifecycle extends Lifecycle {

    void onCreate();

    void onStart();

    void onResume();

    void onMenuCreated();

    void onPause();

    void onStop();

    void onDestroy();
}

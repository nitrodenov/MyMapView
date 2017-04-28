package com.mymapview.presentation.view.activity.base;

import com.mymapview.presentation.presenter.base.BasePresenter;


abstract public class BaseActivityPresenter<V extends ActivityView>
        extends BasePresenter<V, ActivityLifecycle> implements ActivityPresenter<V>, ActivityLifecycle {

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {
    }

    @Override
    public void onMenuCreated() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        lifecycleSubscription.unsubscribe();
    }

}

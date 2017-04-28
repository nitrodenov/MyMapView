package com.mymapview.presentation.view.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import rx.Observable;
import rx.subjects.PublishSubject;

import static com.mymapview.presentation.view.activity.base.ActivityLifecycleEvents.CREATE;
import static com.mymapview.presentation.view.activity.base.ActivityLifecycleEvents.DESTROY;
import static com.mymapview.presentation.view.activity.base.ActivityLifecycleEvents.MENU_CREATED;
import static com.mymapview.presentation.view.activity.base.ActivityLifecycleEvents.PAUSE;
import static com.mymapview.presentation.view.activity.base.ActivityLifecycleEvents.RESUME;
import static com.mymapview.presentation.view.activity.base.ActivityLifecycleEvents.START;
import static com.mymapview.presentation.view.activity.base.ActivityLifecycleEvents.STOP;


abstract public class BaseActivity<P extends ActivityPresenter<?>>
        extends AppCompatActivity implements ActivityView {

    private PublishSubject<ActivityLifecycleEvents> lifecycleSubject = PublishSubject.create();

    abstract protected P getPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        @SuppressWarnings("unchecked")
        ActivityPresenter<ActivityView> presenter =
                (ActivityPresenter<ActivityView>) getPresenter();

        presenter.setView(this);

        lifecycleSubject = PublishSubject.create();

        presenter.observeLifecycle(getLifecycleEvents());
        lifecycleSubject.onNext(CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(RESUME);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        lifecycleSubject.onNext(MENU_CREATED);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifecycleSubject.onNext(PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lifecycleSubject.onNext(STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(DESTROY);
    }

    @Override
    public Observable<ActivityLifecycleEvents> getLifecycleEvents() {
        return lifecycleSubject;
    }

}

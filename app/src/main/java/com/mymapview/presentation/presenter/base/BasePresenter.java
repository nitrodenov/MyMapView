package com.mymapview.presentation.presenter.base;

import android.util.Log;

import com.mymapview.presentation.Lifecycle;
import com.mymapview.presentation.LifecycleCallbackTrigger;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;


public abstract class BasePresenter<V, L extends Lifecycle> implements Presenter<V, L> {
    protected V view;

    protected CompositeSubscription lifecycleSubscription;

    public void setView(V view) {
        this.view = view;
    }

    @Override
    public <T extends LifecycleCallbackTrigger<L>> void observeLifecycle(Observable<T> observable) {
        lifecycleSubscription = new CompositeSubscription();

        Subscription subscription = observable.subscribe(
                new Action1<T>() {
                    @Override
                    public void call(T trigger) {

                        @SuppressWarnings("unchecked")
                        L lifecycle = (L) BasePresenter.this;

                        trigger.call(lifecycle);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(getClass().getSimpleName(),
                                "Caught exception while observing view's lifecycle...", throwable);
                    }
                }
        );

        lifecycleSubscription.add(subscription);
    }
}

package com.mymapview.presentation.presenter.base;

import com.mymapview.presentation.Lifecycle;
import com.mymapview.presentation.LifecycleCallbackTrigger;

import rx.Observable;


public interface Presenter<V, L extends Lifecycle> {

    void setView(V view);

    <T extends LifecycleCallbackTrigger<L>> void observeLifecycle(Observable<T> observable);
}

package com.mymapview.presentation.view.activity.base;

import rx.Observable;


public interface ActivityView {

    Observable<ActivityLifecycleEvents> getLifecycleEvents();

}

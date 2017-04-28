package com.mymapview.presentation;


public interface LifecycleCallbackTrigger<L extends Lifecycle> {
    void call(L lifecycle);
}
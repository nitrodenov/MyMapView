package com.mymapview.presentation.presenter.base;

import com.mymapview.presentation.view.fragment.base.FragmentLifecycle;
import com.mymapview.presentation.view.fragment.base.FragmentView;


abstract public class BaseFragmentPresenter<T extends FragmentView>
        extends BasePresenter<T, FragmentLifecycle> implements FragmentPresenter<T>, FragmentLifecycle {

    @Override
    public void onAttach() {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onCreateView() {

    }

    @Override
    public void onActivityCreated() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroyView() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onDetach() {
        lifecycleSubscription.unsubscribe();
    }

}
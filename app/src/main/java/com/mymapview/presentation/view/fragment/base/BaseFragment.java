package com.mymapview.presentation.view.fragment.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mymapview.presentation.presenter.base.FragmentPresenter;

import rx.subjects.PublishSubject;

import static com.mymapview.presentation.view.fragment.base.FragmentLifecycleEvents.ATTACH;
import static com.mymapview.presentation.view.fragment.base.FragmentLifecycleEvents.CREATE;
import static com.mymapview.presentation.view.fragment.base.FragmentLifecycleEvents.CREATE_VIEW;
import static com.mymapview.presentation.view.fragment.base.FragmentLifecycleEvents.DESTROY;
import static com.mymapview.presentation.view.fragment.base.FragmentLifecycleEvents.DESTROY_VIEW;
import static com.mymapview.presentation.view.fragment.base.FragmentLifecycleEvents.DETACH;
import static com.mymapview.presentation.view.fragment.base.FragmentLifecycleEvents.PAUSE;
import static com.mymapview.presentation.view.fragment.base.FragmentLifecycleEvents.RESUME;
import static com.mymapview.presentation.view.fragment.base.FragmentLifecycleEvents.START;
import static com.mymapview.presentation.view.fragment.base.FragmentLifecycleEvents.STOP;


abstract public class BaseFragment<T extends FragmentPresenter<?>>
        extends Fragment implements FragmentView {

    public final String TAG = getClass().getName();

    protected abstract T getPresenter();

    private PublishSubject<FragmentLifecycleEvents> lifecycleSubject;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        @SuppressWarnings("unchecked")
        FragmentPresenter<FragmentView> presenter =
                (FragmentPresenter<FragmentView>) getPresenter();

        presenter.setView(this);

        lifecycleSubject = PublishSubject.create();

        presenter.observeLifecycle(lifecycleSubject);
        lifecycleSubject.onNext(ATTACH);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(CREATE);
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        lifecycleSubject.onNext(CREATE_VIEW);
        return inflater.inflate(getLayoutResourceId(), container, false);
    }

    protected abstract int getLayoutResourceId();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lifecycleSubject.onNext(FragmentLifecycleEvents.ACTIVITY_CREATED);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        lifecycleSubject.onNext(PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycleSubject.onNext(STOP);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lifecycleSubject.onNext(DESTROY_VIEW);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(DESTROY);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lifecycleSubject.onNext(DETACH);
    }
}

package com.mymapview.presentation.view.fragment.base;

import com.mymapview.presentation.Lifecycle;


public interface FragmentLifecycle extends Lifecycle {

    void onAttach();

    void onCreate();

    void onCreateView();

    void onActivityCreated();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroyView();

    void onDestroy();

    void onDetach();
}
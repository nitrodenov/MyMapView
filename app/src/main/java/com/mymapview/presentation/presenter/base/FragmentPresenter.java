package com.mymapview.presentation.presenter.base;

import com.mymapview.presentation.view.fragment.base.FragmentLifecycle;
import com.mymapview.presentation.view.fragment.base.FragmentView;

public interface FragmentPresenter<T extends FragmentView> extends Presenter<T, FragmentLifecycle> {

}
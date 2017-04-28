package com.mymapview.presentation.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.mymapview.R;
import com.mymapview.presentation.presenter.base.MainActivityPresenter;
import com.mymapview.presentation.view.activity.base.BaseActivity;
import com.mymapview.presentation.view.fragment.map.MyMapViewFragment;

public class MainActivity extends BaseActivity<MainActivityPresenter> {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(TAG);

        if (fragment == null) {
            fm.beginTransaction()
                    .replace(R.id.container, new MyMapViewFragment(), TAG)
                    .commit();
        }
    }

    @Override
    protected MainActivityPresenter getPresenter() {
        return new MainActivityPresenter();
    }
}

package com.mymapview.presentation.view.fragment.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mymapview.R;
import com.mymapview.presentation.model.NewTileData;
import com.mymapview.presentation.model.TilePosition;
import com.mymapview.presentation.model.TileResultData;
import com.mymapview.presentation.model.cache.Cache;
import com.mymapview.presentation.model.cache.FileCache;
import com.mymapview.presentation.presenter.map.MyMapPresenter;
import com.mymapview.presentation.view.fragment.base.BaseFragment;
import com.mymapview.presentation.view.widget.MyMapView;

import rx.Observable;
import rx.subjects.PublishSubject;


public class MyMapViewFragment extends BaseFragment<MyMapPresenter> implements IMapView {

    public static final String TAG = MyMapViewFragment.class.getSimpleName();

    private MyMapView myMapView;
    private Cache<Integer, Bitmap> fileCache;
    private BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isConnected()) {
                String message = getResources().getString(R.string.no_internet_message);
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        fileCache = new FileCache(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_map_fragment, container, false);
        myMapView = (MyMapView) v.findViewById(R.id.mapview);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        initConnectivityReceiver();
        myMapView.requestLayout();
    }

    @Override
    public void onPause() {
        super.onPause();

        getContext().unregisterReceiver(connectivityReceiver);
        myMapView.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        myMapView.onDestroyView();
    }

    @Override
    protected MyMapPresenter getPresenter() {
        return new MyMapPresenter();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.my_map_fragment;
    }

    @Override
    public Observable<NewTileData> getNewTileRequest() {
        return myMapView.getNewTileObservable();
    }

    @Override
    public PublishSubject<TileResultData> getReceivedTileObservable() {
        return myMapView.getReceivedTileSubject();
    }

    @Override
    public Bitmap getTileFromLocalStorage(TilePosition tilePosition) {
        Bitmap bitmap = fileCache.get(tilePosition.hashCode());

        if (bitmap == null) {
            return null;
        }

        return bitmap;
    }

    @Override
    public void putTileInLocalStorage(TilePosition tilePosition, Bitmap myBitmap) {
        fileCache.put(tilePosition.hashCode(), myBitmap);
    }

    private void initConnectivityReceiver() {
        IntentFilter connectivityFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(connectivityReceiver, connectivityFilter);
    }

    private boolean isConnected() {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

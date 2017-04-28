package com.mymapview.presentation.view.fragment.map;

import android.graphics.Bitmap;

import com.mymapview.presentation.model.NewTileData;
import com.mymapview.presentation.model.TilePosition;
import com.mymapview.presentation.model.TileResultData;
import com.mymapview.presentation.view.fragment.base.FragmentView;

import rx.Observable;
import rx.subjects.PublishSubject;


public interface IMapView extends FragmentView {

    Observable<NewTileData> getNewTileRequest();

    PublishSubject<TileResultData> getReceivedTileObservable();

    Bitmap getTileFromLocalStorage(TilePosition tilePosition);

    void putTileInLocalStorage(TilePosition tilePosition, Bitmap myBitmap);
}

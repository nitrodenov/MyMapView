package com.mymapview.presentation.presenter.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.mymapview.presentation.model.NewTileData;
import com.mymapview.presentation.model.TilePosition;
import com.mymapview.presentation.model.TileResultData;
import com.mymapview.presentation.model.UrlHelper;
import com.mymapview.presentation.model.cache.Cache;
import com.mymapview.presentation.model.cache.InMemoryCache;
import com.mymapview.presentation.presenter.base.BaseFragmentPresenter;
import com.mymapview.presentation.view.fragment.map.IMapView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.mymapview.presentation.model.Constants.MAX_MEMORY;
import static com.mymapview.presentation.model.Constants.OPTIMAL_THREADS_COUNT;
import static com.mymapview.presentation.model.DataHelper.convertToBytes;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.from;


public class MyMapPresenter extends BaseFragmentPresenter<IMapView> {

    public static final String TAG = MyMapPresenter.class.getSimpleName();

    private CompositeSubscription newTileRequestSubscription;
    private CompositeSubscription downloadSubscription;
    private Scheduler myScheduler = from(newFixedThreadPool(OPTIMAL_THREADS_COUNT));
    private Cache<TilePosition, Bitmap> inMemoryCache =
            new InMemoryCache<TilePosition, Bitmap>(MAX_MEMORY / 8) {
                @Override
                protected int sizeOf(TilePosition key, Bitmap value) {
                    return value.getByteCount() / 1024;
                }
            };

    @Override
    public void onResume() {
        super.onResume();

        newTileRequestSubscription = new CompositeSubscription();
        downloadSubscription = new CompositeSubscription();

        Observable<NewTileData> newTileRequest = view.getNewTileRequest();
        Subscriber<NewTileData> newTileRequestSubscriber = getNewTileRequestSubscriber();
        Subscription subscription = newTileRequest.subscribe(newTileRequestSubscriber);

        newTileRequestSubscription.add(subscription);
    }

    @Override
    public void onPause() {
        super.onPause();

        newTileRequestSubscription.unsubscribe();
        downloadSubscription.unsubscribe();
    }

    private Observable<TileResultData> getResultDataObservable(final NewTileData newTileData) {
        return Observable.create(new Observable.OnSubscribe<TileResultData>() {
            @Override
            public void call(Subscriber<? super TileResultData> subscriber) {
                try {
                    TileResultData tileResultData = getTile(newTileData);

                    subscriber.onNext(tileResultData);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private TileResultData getTile(NewTileData newTileData) throws IOException {
        TilePosition tilePosition = newTileData.getTilePosition();
        Log.d(TAG, "start loading from memory" + System.currentTimeMillis());
        Bitmap tileInMemory = inMemoryCache.get(tilePosition);

        if (tileInMemory == null) {
            return getTileFromLocal(newTileData);
        }

        Log.d(TAG, "end loading from memory" + System.currentTimeMillis());

        return new TileResultData(newTileData, tileInMemory);
    }

    private TileResultData getTileFromLocal(NewTileData newTileData) throws IOException {
        Log.d(TAG, "start loading from DB" + System.currentTimeMillis());
        TilePosition tilePosition = newTileData.getTilePosition();
        Bitmap tileFromLocalStorage = view.getTileFromLocalStorage(tilePosition);

        if (tileFromLocalStorage == null) {
            return loadTile(newTileData);
        }

        Log.d(TAG, "end loading from DB" + System.currentTimeMillis());

        inMemoryCache.put(tilePosition, tileFromLocalStorage);

        return new TileResultData(newTileData, tileFromLocalStorage);
    }

    private TileResultData loadTile(NewTileData newTileData) throws IOException {
        Log.d(TAG, "start loading from server" + new Date(System.currentTimeMillis()));
        TilePosition tilePosition = newTileData.getTilePosition();
        String urlString = UrlHelper.getUrl(newTileData);
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();

        InputStream input = connection.getInputStream();

        byte[] bitmapBytes = convertToBytes(input);
        Bitmap myBitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
        inMemoryCache.put(tilePosition, myBitmap);

        view.putTileInLocalStorage(tilePosition, myBitmap);

        Log.d(TAG, "end loading from server" + System.currentTimeMillis());

        return new TileResultData(newTileData, myBitmap);
    }

    private Subscriber<NewTileData> getNewTileRequestSubscriber() {
        return new Subscriber<NewTileData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final NewTileData newTileData) {
                Observable<TileResultData> resultDataObservable =
                        getResultDataObservable(newTileData);
                Subscriber<TileResultData> resultDataSubscriber = getResultDataSubscriber();
                Subscription subscription = resultDataObservable
                        .subscribeOn(myScheduler)
                        .observeOn(mainThread())
                        .subscribe(resultDataSubscriber);

                downloadSubscription.add(subscription);

            }
        };
    }

    private Subscriber<TileResultData> getResultDataSubscriber() {
        return new Subscriber<TileResultData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.toString());
            }

            @Override
            public void onNext(TileResultData tileResultData) {
                view.getReceivedTileObservable().onNext(tileResultData);
            }
        };
    }

}

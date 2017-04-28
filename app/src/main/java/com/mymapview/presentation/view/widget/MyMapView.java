package com.mymapview.presentation.view.widget;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mymapview.presentation.model.Bounds;
import com.mymapview.presentation.model.FlingData;
import com.mymapview.presentation.model.NewTileData;
import com.mymapview.presentation.model.TilePosition;
import com.mymapview.presentation.model.TileResultData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.mymapview.presentation.model.Constants.FLING_DELAY;
import static com.mymapview.presentation.model.Constants.FLING_DURATION;
import static com.mymapview.presentation.model.Constants.START_COORDINATE;
import static com.mymapview.presentation.model.Constants.TILE_SIZE;
import static com.mymapview.presentation.model.Constants.X_TILE_COUNT;
import static com.mymapview.presentation.model.Constants.Y_TILE_COUNT;

public class MyMapView extends ViewGroup {

    private static final String TAG = MyMapView.class.getSimpleName();

    private GestureDetector gestureDetector;
    private Bounds bounds;
    private CompositeSubscription receivedTileSubscription;
    private PublishSubject<NewTileData> newTileObservable = PublishSubject.create();
    private PublishSubject<TileResultData> receivedTileObservable = PublishSubject.create();
    private Map<TilePosition, ImageView> viewCache = new HashMap<>();
    private Handler flingHandler = new Handler();
    private boolean isNeedRefresh;

    public MyMapView(Context context) {
        super(context);
        init();
    }

    public MyMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        gestureDetector = getGestureDetector();
        receivedTileSubscription = new CompositeSubscription();

        Subscriber<TileResultData> receivedTileSubscriber = getReceivedTileSubscriber();
        Subscription subscription = receivedTileObservable.subscribe(receivedTileSubscriber);
        receivedTileSubscription.add(subscription);
    }

    private GestureDetector getGestureDetector() {
        return new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                scroll(distanceX, distanceY);
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                fling(velocityX, velocityY);
                return true;
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        initLayout();
    }

    private void initLayout() {
        if (bounds == null || isNeedRefresh) {
            scroll(0, 0);
        }
    }

    private void scroll(float distanceX, float distanceY) {
        float dX = -distanceX;
        float dY = -distanceY;

        Bounds newBounds = getNewBounds(dX, dY);

        if (isInArea(newBounds)) {
            reconstructView(newBounds, dX, dY);
        }
    }

    private void reconstructView(Bounds newBounds, float dX, float dY) {
        trimToSizeViewCache(dX, dY);
        removeInvisibleItems(dX, dY);
        changeChildItemsPosition(dX, dY);
        fillBounds(newBounds);
        bounds = newBounds;
    }

    private void removeInvisibleItems(float dX, float dY) {
        int count = getChildCount() - 1;
        for (int i = count; i >= 0; i--) {
            View v = getChildAt(i);
            if (isOutOfView(v, dX, dY)) {
                removeViewInLayout(v);
            }
        }
    }

    private void changeChildItemsPosition(float dX, float dY) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int left = child.getLeft() + (int) dX;
            int top = child.getTop() + (int) dY;
            child.layout(left, top, left + TILE_SIZE, top + TILE_SIZE);
        }
    }

    private Bounds getNewBounds(float dX, float dY) {
        if (bounds == null) {
            return new Bounds(getWidth(), getHeight(), TILE_SIZE);
        }

        return bounds.getNewBounds((int) dX, (int) dY, getWidth(), getHeight());
    }

    private void trimToSizeViewCache(float dX, float dY) {
        Iterator<Map.Entry<TilePosition, ImageView>> it = viewCache.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<TilePosition, ImageView> entry = it.next();
            ImageView imageView = entry.getValue();
            if (imageView != null && isOutOfView(imageView, dX, dY)) {
                it.remove();
            }
        }
    }

    private boolean isOutOfView(View v, float dx, float dy) {
        int bottom = v.getBottom();
        int top = v.getTop();
        int right = v.getRight();
        int left = v.getLeft();
        int height = getHeight();
        int width = getWidth();

        return (((bottom + dy) < 0) || (top + dy > height)
                || ((right + dx) < 0) || (left + dx > width));
    }

    private void fillBounds(Bounds newBounds) {
        for (int x = newBounds.getLeft(); x < newBounds.getRight(); x++) {
            for (int y = newBounds.getTop(); y < newBounds.getBottom(); y++) {
                if (isNeedAddView(x, y)) {
                    int left = newBounds.getX() + (x - newBounds.getLeft()) * TILE_SIZE;
                    int top = newBounds.getY() + (y - newBounds.getTop()) * TILE_SIZE;

                    ImageView v = new ImageView(getContext());
                    v.layout(left, top, left + TILE_SIZE, top + TILE_SIZE);
                    addViewInLayout(v, -1, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

                    TilePosition tilePosition = new TilePosition(x, y);
                    NewTileData newTileData = new NewTileData(tilePosition, START_COORDINATE);
                    viewCache.put(tilePosition, v);

                    newTileObservable.onNext(newTileData);
                } else if (isNeedRefresh) {
                    TilePosition tilePosition = new TilePosition(x, y);
                    NewTileData newTileData = new NewTileData(tilePosition, START_COORDINATE);
                    newTileObservable.onNext(newTileData);
                }
            }
        }
        isNeedRefresh = false;
    }

    private boolean isNeedAddView(int x, int y) {
        return bounds == null || !bounds.contains(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if ((ev.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
            flingHandler.removeCallbacksAndMessages(null);
        }

        return gestureDetector.onTouchEvent(ev);
    }

    private Subscriber<TileResultData> getReceivedTileSubscriber() {
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
                NewTileData newTileData = tileResultData.getNewTileData();
                TilePosition tilePosition = newTileData.getTilePosition();
                ImageView v = viewCache.get(tilePosition);
                if (v != null) {
                    v.setImageBitmap(tileResultData.getBitmap());
                    v.invalidate();
                }
            }
        };
    }

    private boolean isInArea(Bounds newBounds) {
        if (newBounds == null) {
            return false;
        }

        int x = newBounds.getLeft();
        int y = newBounds.getTop();

        return x >= 0 && x < X_TILE_COUNT && y >= 0 && y < Y_TILE_COUNT;
    }

    private void fling(final float velocityX, final float velocityY) {
        final Runnable runnable = new Runnable() {

            private int counter = FLING_DURATION / FLING_DELAY;
            private FlingData fdX = new FlingData();
            private FlingData fdY = new FlingData();

            @Override
            public void run() {
                if (counter == 0) {
                    return;
                }

                float distanceX;
                float distanceY;

                if (counter == FLING_DURATION / FLING_DELAY) {
                    distanceX = fdX.countInitDist(velocityX);
                    distanceY = fdY.countInitDist(velocityY);
                } else {
                    distanceX = fdX.countDist();
                    distanceY = fdY.countDist();
                }

                scroll(distanceX, distanceY);

                counter--;
                flingHandler.postDelayed(this, FLING_DELAY);
            }
        };

        flingHandler.post(runnable);
    }

    public Observable<NewTileData> getNewTileObservable() {
        return newTileObservable;
    }

    public PublishSubject<TileResultData> getReceivedTileSubject() {
        return receivedTileObservable;
    }

    public void onDestroyView() {
        receivedTileSubscription.unsubscribe();
    }

    public void pause() {
        this.isNeedRefresh = true;
        flingHandler.removeCallbacksAndMessages(null);
    }
}

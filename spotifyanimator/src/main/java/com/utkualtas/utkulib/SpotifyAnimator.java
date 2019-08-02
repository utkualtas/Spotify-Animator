package com.utkualtas.utkulib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class SpotifyAnimator implements AnimatorInterface {

    private static final String TAG = "SpotifyAnimator";
    private final int VIEW_RELEASE_AREA_X_Y = 16;
    private float mScaleRatio = 0.97f;
    private int mDurationTime = 60; // millisecond
    private int mFilterColor = Color.parseColor("#33000000");


    private Context mContext;
    private View mActualView;
    private View mView;
    private Listener mListener;
    private float mOldX = 0;
    private float mOldY = 0;
    private boolean isReleased = false;
    private boolean isHasFocusFilter = false;

    private FrameLayout mFilterParentLayout;
    private RelativeLayout mFilterLayout;

    public SpotifyAnimator(Context context, View view) {
        this.mView = view;
        this.mContext = context;
    }

    public SpotifyAnimator init() {
        if (isHasFocusFilter) {
            setUpFilter();
            mActualView = mFilterParentLayout;
        } else {
            setUpOnTouch(mView);
            mActualView = mView;
        }
        return this;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpOnTouch(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mOldX = event.getX();
                    mOldY = event.getY();
                    view.animate().scaleX(mScaleRatio).scaleY(mScaleRatio).setInterpolator(new LinearInterpolator()).setDuration(10);
                    filterController(event);
                    isReleased = false;
                    return true;
                case MotionEvent.ACTION_UP:
                    if (!isReleased) {
                        view.animate().scaleX(1f).scaleY(1f).setInterpolator(new LinearInterpolator()).setDuration(mDurationTime);
                        filterController(event);
                        if(mListener != null)
                            mListener.onClickListener(mActualView);
                    }
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (calculateShouldRelease(event)) {
                        if (!isReleased) {
                            view.animate().scaleX(1f).scaleY(1f).setInterpolator(new LinearInterpolator()).setDuration(mDurationTime);
                            filterController(event);
                            isReleased = true;
                        }
                    }
                    return true;
                default:
                    return true;
            }
        });
    }

    @Override
    public AnimatorInterface setOnClickListener(Listener listener) {
        this.mListener = listener;
        return this;
    }

    @Override
    public AnimatorInterface setScaleRatio(float ratio) {
        this.mScaleRatio = ratio;
        return this;
    }

    @Override
    public AnimatorInterface setFilterColor(int color) {
        this.mFilterColor = color;
        return this;
    }

    @Override
    public AnimatorInterface setFilterColor(String color) {
        this.mFilterColor = Color.parseColor(color);
        return this;
    }

    @Override
    public AnimatorInterface setFocusFilter(boolean isHasFocusFilter) {
        this.isHasFocusFilter = isHasFocusFilter;
        return this;
    }

    private void setUpFilter() {
        ViewGroup.LayoutParams viewLayoutParams = mView.getLayoutParams();
        ViewGroup parentOfView = (ViewGroup) mView.getParent();
        int viewPosition = parentOfView.indexOfChild(mView);
        parentOfView.removeView(mView);
        mFilterParentLayout = new FrameLayout(mContext);
        mFilterParentLayout.addView(mView);
        mFilterLayout = new RelativeLayout(mContext);
        mFilterLayout.setBackgroundColor(mFilterColor);
        mFilterLayout.setVisibility(View.GONE);
        mFilterParentLayout.addView(mFilterLayout);
        parentOfView.addView(mFilterParentLayout, viewPosition, viewLayoutParams);
        clearOnTouch(mView);
        setUpOnTouch(mFilterParentLayout);
    }

    private void filterController(MotionEvent event) {
        if (isHasFocusFilter) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mFilterLayout.setVisibility(View.VISIBLE);
                    break;
                case MotionEvent.ACTION_UP:
                    mFilterLayout.setVisibility(View.GONE);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mFilterLayout.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    private boolean calculateShouldRelease(MotionEvent event) {
        float x = Math.abs(mOldX - event.getX());
        float y = Math.abs(mOldY - event.getY());
        if (x > VIEW_RELEASE_AREA_X_Y || y > VIEW_RELEASE_AREA_X_Y) {
            return true;
        }
        return false;
    }

    private void clearOnTouch(View view) {
        view.setOnTouchListener(null);
    }

    public interface Listener{
        void onClickListener(View v);
    }

}

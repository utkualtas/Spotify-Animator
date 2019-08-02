package com.utkualtas.utkulib;

public interface AnimatorInterface {
    AnimatorInterface setOnClickListener(SpotifyAnimator.Listener listener);
    AnimatorInterface setScaleRatio(float ratio);
    AnimatorInterface setFilterColor(int color);
    AnimatorInterface setFilterColor(String color);
    AnimatorInterface setFocusFilter(boolean isHasFocusFilter);
    SpotifyAnimator init();

}

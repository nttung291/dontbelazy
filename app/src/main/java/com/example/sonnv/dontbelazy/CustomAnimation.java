package com.example.sonnv.dontbelazy;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by SONNV on 9/22/2017.
 */

public class CustomAnimation {
    public static Animation fabOpen(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.fab_open);
    }

    public static Animation fabClose(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.fab_close);
    }
    public static Animation fabRotateClockwise(Context context) {
        return AnimationUtils.loadAnimation(context,R.anim.rotate_clockwise);
    }
    public static Animation fabRotateAntiClockwise(Context context) {
        return AnimationUtils.loadAnimation(context,R.anim.rotate_anticlockwise);
    }
}

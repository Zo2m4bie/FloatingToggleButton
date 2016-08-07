package com.zo2m4bie.floatingtoggle.utils;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.TextView;

import com.zo2m4bie.floatingtoggle.callback.ChangeColorCallback;

/**
 * Created by dima on 8/7/16.
 */
public class AnimationUtils {

    private static final int ANIMATION_SPEED = 250;

    public static ValueAnimator getChangeColorAnimation(TextView tv, int textColorFrom, int textColorTo){
        ValueAnimator va = ValueAnimator.ofObject(new ArgbEvaluator(), textColorFrom, textColorTo);
        va.setDuration(ANIMATION_SPEED); // milliseconds
        va.addUpdateListener(new ChangeColorCallback(tv));
        return va;
    }

    public static ObjectAnimator getToggleCircleAnimator(View toggleCircle, int width, int statecount, int from, int to) {
        int part = width / statecount;
        int formInPixel = from * part;
        int toInPixel = to * part;
        return ObjectAnimator.ofFloat(toggleCircle, "x", formInPixel , toInPixel).setDuration(ANIMATION_SPEED);
    }

}

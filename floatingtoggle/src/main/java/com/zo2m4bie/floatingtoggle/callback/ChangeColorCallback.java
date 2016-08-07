package com.zo2m4bie.floatingtoggle.callback;

import android.animation.ValueAnimator;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by dima on 8/7/16.
 */
public class ChangeColorCallback  implements ValueAnimator.AnimatorUpdateListener{
    private TextView mTv;

    public ChangeColorCallback(TextView tv){
        mTv = tv;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animator) {
        mTv.setTextColor((int) animator.getAnimatedValue());
    }
}
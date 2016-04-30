package com.zo2m4bie.floatingtoggle;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import model.setting.com.floatingtoggle.R;

/**
 * Created by dima on 4/28/16.
 */
public class FloatingToggleButton extends RelativeLayout implements View.OnClickListener {

    private FrameLayout mLayout;
    private View mToggleCircle;
    private TextView mTextView1, mTextView2, mTextView3;
    private String mText1, mText2, mText3;
    private int mStateCount = 2;

    private ObjectAnimator mCurrentAnimator;
    private IStateSelected mStateSelected;
    private int mCurrentState;
    private int mTextColorFrom, mTextColorTo;

    public FloatingToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.settings_toggle, this, true);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SettingsToggle);
        mStateCount = a.getInteger(R.styleable.SettingsToggle_statecount, 2);
        if(mStateCount > 3){
            mStateCount = 3;
        } else if(mStateCount < 2){
            mStateCount = 2;
        }
        mText1 = a.getString(R.styleable.SettingsToggle_text1);
        mText2 = a.getString(R.styleable.SettingsToggle_text2);
        if(mStateCount == 3)
            mText3 = a.getString(R.styleable.SettingsToggle_text3);

        mToggleCircle = findViewById(R.id.toggleCircle);
        mTextView1 = (TextView) findViewById(R.id.switch_tv_1);
        mTextView2 = (TextView) findViewById(R.id.switch_tv_2);
        mTextView3 = (TextView) findViewById(R.id.switch_tv_3);
        mTextView1.setText(mText1);
        mTextView2.setText(mText2);
        if(mStateCount == 2){
            mTextView3.setVisibility(GONE);
        } else {
            mTextView3.setText(mText3);
        }
        mLayout = (FrameLayout)findViewById(R.id.layout);
        mLayout.setOnClickListener(this);
        View mainView = findViewById(R.id.background_oval_off);
        Drawable dotdrawable = a.getDrawable(R.styleable.SettingsToggle_dotdrawable);
        if(dotdrawable == null) {
            int dotcolor = a.getColor(R.styleable.SettingsToggle_dotcolor, -1);
            if(dotcolor != -1)
                mToggleCircle.setBackgroundColor(dotcolor);
        } else {
            setBackgroundDrawable(mToggleCircle, dotdrawable);
        }

        Drawable backdrawable = a.getDrawable(R.styleable.SettingsToggle_backgrounddrawable);
        if(backdrawable == null) {
            int backcolor = a.getColor(R.styleable.SettingsToggle_backgroundcolor, -1);
            if(backcolor != -1)
                mainView.setBackgroundColor(backcolor);
        } else {
            setBackgroundDrawable(mainView, backdrawable);
        }
        mTextColorFrom = a.getColor(R.styleable.SettingsToggle_textcolorfrom, -1);
        mTextColorTo = a.getColor(R.styleable.SettingsToggle_textcolorto, -1);
        if(mTextColorFrom != -1){
            mTextView1.setTextColor(mTextColorFrom);
            mTextView2.setTextColor(mTextColorFrom);
            mTextView3.setTextColor(mTextColorFrom);
        }
        mCurrentState = 0;
    }

    public void setBackgroundDrawable(View view, Drawable draw){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            // only for gingerbread and newer versions
            view.setBackground(draw);
        } else {
            view.setBackgroundDrawable(draw);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getWidth();
        mToggleCircle.getLayoutParams().width = width / mStateCount;

        setState(mCurrentState);
    }

    public FloatingToggleButton(Context context) {
        this(context, null);
    }

    public void setState(int stateNumber) {
        if (isInEditMode())
            return; //isInEditMode(): if being rendered in IDE preview, skip code that will break

        switch (stateNumber) {
            case 0:
                mCurrentState = stateNumber;
                mToggleCircle.setX(0);
                if(mTextColorTo != -1) mTextView1.setTextColor(mTextColorTo);
                break;
            case 1:
                mCurrentState = stateNumber;
                mToggleCircle.setX(getWidth() / mStateCount);
                if(mTextColorTo != -1) mTextView2.setTextColor(mTextColorTo);
                break;
            case 2:
                if(mStateCount == 2){
                    return;
                }
                mCurrentState = stateNumber;
                mToggleCircle.setX((getWidth() / 3) * 2);
                if(mTextColorTo != -1) mTextView3.setTextColor(mTextColorTo);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if ((mCurrentAnimator != null) && mCurrentAnimator.isRunning()) return;
        int oldState = mCurrentState;
        mCurrentState = (mCurrentState + 1) > (mStateCount -1) ? 0 : (mCurrentState + 1);
        if(mStateSelected != null){
            mStateSelected.selectState(mCurrentState);
        }
        mCurrentAnimator = getAnimator(oldState, mCurrentState);
        mCurrentAnimator.start();
        changeColor(getSerlectedTextView(oldState), mTextColorTo, mTextColorFrom);
        changeColor(getSerlectedTextView(mCurrentState), mTextColorFrom, mTextColorTo);
    }

    private TextView getSerlectedTextView(int currentState) {
        switch (currentState){
            case 1:
                return mTextView2;
            case 2:
                return mTextView3;
            default:
                return mTextView1;
        }
    }

    private void changeColor(TextView tv, int textColorFrom, int textColorTo) {
        if(textColorFrom == -1 || textColorTo == -1)
            return;

        ValueAnimator colorAnimationTo = ValueAnimator.ofObject(new ArgbEvaluator(), textColorFrom, textColorTo);
        colorAnimationTo.setDuration(250); // milliseconds
        colorAnimationTo.addUpdateListener(new ChangeColorCallback(tv));
        colorAnimationTo.start();

    }

    public void setStateSelected(IStateSelected stateSelected) {
        mStateSelected = stateSelected;
    }

    private ObjectAnimator getAnimator(int from, int to) {
        int part = getWidth() / mStateCount;
        int formInPixel = from * part;
        int toInPixel = to * part;
        return ObjectAnimator.ofFloat(mToggleCircle, "x", formInPixel , toInPixel).setDuration(250);
    }

    class ChangeColorCallback implements ValueAnimator.AnimatorUpdateListener{
        private TextView mTv;

        public ChangeColorCallback(TextView tv){
            mTv = tv;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animator) {
            mTv.setTextColor((int) animator.getAnimatedValue());
        }
    }

    public TextView getTextView1() {
        return mTextView1;
    }

    public TextView getTextView2() {
        return mTextView2;
    }

    public TextView getTextView3() {
        return mTextView3;
    }
}

package com.zo2m4bie.floatingtoggle;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zo2m4bie.floatingtoggle.utils.AnimationUtils;

import model.setting.com.floatingtoggle.R;

/**
 * Created by dima on 4/28/16.
 */
public class FloatingToggleButton extends RelativeLayout implements View.OnClickListener {

    private static final int MAX_TEXT_VIEWS_COUNT = 3;
    private static final int MAX_STATE_COUNT = 3;
    private static final int MIN_STATE_COUNT = 2;

    private FrameLayout mLayout;
    private View mToggleCircle;
    private View mMainView;
    private TextView[] mTextViews;
    private String[] mTexts;
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
        initState(a);
        initTexts(a);
        initTextViews();
        initToggleircle(a);
        initBackgroundColor(a);
        initTextColors(a);
        mLayout = (FrameLayout)findViewById(R.id.layout);
        mLayout.setOnClickListener(this);
        mCurrentState = 0;
    }

    private void initState(TypedArray a) {
        mStateCount = a.getInteger(R.styleable.SettingsToggle_statecount, 2);
        if(mStateCount > MAX_STATE_COUNT){
            mStateCount = MAX_STATE_COUNT;
        } else if(mStateCount < MIN_STATE_COUNT){
            mStateCount = MIN_STATE_COUNT;
        }
    }

    private void initToggleircle(TypedArray a) {
        mToggleCircle = findViewById(R.id.toggleCircle);
        Drawable dotdrawable = a.getDrawable(R.styleable.SettingsToggle_dotdrawable);
        if(dotdrawable == null) {
            int dotcolor = a.getColor(R.styleable.SettingsToggle_dotcolor, -1);
            if(dotcolor != -1)
                mToggleCircle.setBackgroundColor(dotcolor);
        } else {
            setBackgroundDrawable(mToggleCircle, dotdrawable);
        }
    }

    private void initBackgroundColor(TypedArray a) {
        mMainView = findViewById(R.id.background_oval_off);
        Drawable backdrawable = a.getDrawable(R.styleable.SettingsToggle_backgrounddrawable);
        if(backdrawable == null) {
            int backcolor = a.getColor(R.styleable.SettingsToggle_backgroundcolor, -1);
            if(backcolor != -1)
                mMainView.setBackgroundColor(backcolor);
        } else {
            setBackgroundDrawable(mMainView, backdrawable);
        }
    }

    private void initTextColors(TypedArray a) {
        mTextColorFrom = a.getColor(R.styleable.SettingsToggle_textcolorfrom, -1);
        mTextColorTo = a.getColor(R.styleable.SettingsToggle_textcolorto, -1);
        if (mTextColorFrom != -1) {
            for (TextView tv : mTextViews)
                tv.setTextColor(mTextColorFrom);
        }
    }

    private void initTexts(TypedArray a) {
        mTexts = new String[MAX_TEXT_VIEWS_COUNT];
        mTexts[0] = a.getString(R.styleable.SettingsToggle_text1);
        mTexts[1] = a.getString(R.styleable.SettingsToggle_text2);
        if(mStateCount == MAX_STATE_COUNT)
            mTexts[2] = a.getString(R.styleable.SettingsToggle_text3);
    }

    private void initTextViews() {
        mTextViews = new TextView[MAX_TEXT_VIEWS_COUNT];
        mTextViews[0] = (TextView) findViewById(R.id.switch_tv_1);
        mTextViews[1] = (TextView) findViewById(R.id.switch_tv_2);
        mTextViews[2] = (TextView) findViewById(R.id.switch_tv_3);
        for(int i = 0; i < MAX_TEXT_VIEWS_COUNT; i++)
            mTextViews[i].setText(mTexts[i]);

        if(mStateCount == MIN_STATE_COUNT)
            mTextViews[2].setVisibility(GONE);
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
        int width = getMeasuredWidth();
        mToggleCircle.getLayoutParams().width = width / mStateCount;

        setState(mCurrentState);
    }

    public void setState(int stateNumber) {
        if (isInEditMode())
            return; //isInEditMode(): if being rendered in IDE preview, skip code that will break

        switch (stateNumber) {
            case 0:
                mCurrentState = stateNumber;
                mToggleCircle.setX(0);
                break;
            case 1:
                mCurrentState = stateNumber;
                mToggleCircle.setX(getWidth() / mStateCount);
                break;
            case 2:
                if(mStateCount == 2){
                    return;
                }
                mCurrentState = stateNumber;
                mToggleCircle.setX((getWidth() / 3) * 2);
                break;
        }

        mTextViews[stateNumber].setTextColor(mTextColorTo);
    }

    @Override
    public void onClick(View view) {
        if ((mCurrentAnimator != null) && mCurrentAnimator.isRunning()) return;
        int oldState = mCurrentState;
        mCurrentState = (mCurrentState + 1) > (mStateCount -1) ? 0 : (mCurrentState + 1);
        if(mStateSelected != null){
            mStateSelected.selectState(mCurrentState);
        }
        mCurrentAnimator = AnimationUtils.getToggleCircleAnimator(mToggleCircle, getWidth(),
                mStateCount, oldState, mCurrentState);
        mCurrentAnimator.start();

        AnimationUtils.getChangeColorAnimation(getSerlectedTextView(oldState),
                mTextColorTo, mTextColorFrom).start();
        AnimationUtils.getChangeColorAnimation(getSerlectedTextView(mCurrentState),
                mTextColorFrom, mTextColorTo).start();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        SavedState savedState = new SavedState(parcelable);
        savedState.setSelectedState(mCurrentState);
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState savedState = (SavedState)state;
        super.onRestoreInstanceState(savedState.getSuperState());

        mCurrentState = savedState.getSelectedState();
        setState(mCurrentState);
    }

    private TextView getSerlectedTextView(int currentState) {
        return mTextViews[currentState];
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mCurrentAnimator != null)
            mCurrentAnimator.cancel();
    }

    private void changeColor(TextView tv, int textColorFrom, int textColorTo) {
        AnimationUtils.getChangeColorAnimation(tv, textColorFrom, textColorTo);
    }

    public void setStateSelected(IStateSelected stateSelected) {
        mStateSelected = stateSelected;
    }


    public TextView getTextView(int position) {
        return mTextViews[position];
    }

    public int getCurrentSelectedState(){
        return mCurrentState;
    }

}

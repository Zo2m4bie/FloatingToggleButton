package com.zo2m4bie.floatingtoggle;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by dima on 8/7/16.
 */
public class SavedState extends View.BaseSavedState {

    private int mSelectedState;

    public SavedState(Parcelable superState) {
        super(superState);
    }

    private SavedState(Parcel in) {
        super(in);
        this.mSelectedState = in.readInt();
    }

    public int getSelectedState() {
        return mSelectedState;
    }

    public void setSelectedState(int selectedState) {
        mSelectedState = selectedState;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(this.mSelectedState);
    }

    //required field that makes Parcelables from a Parcel
    public static final Parcelable.Creator<SavedState> CREATOR =
            new Parcelable.Creator<SavedState>() {
                public SavedState createFromParcel(Parcel in) {
                    return new SavedState(in);
                }
                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
}

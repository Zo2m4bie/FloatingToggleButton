package com.floatintogglebutton;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.zo2m4bie.floatingtoggle.IStateSelected;
import com.zo2m4bie.floatingtoggle.FloatingToggleButton;

public class MyActivity extends Activity {

    private FloatingToggleButton mFirstToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        mFirstToggle = (FloatingToggleButton) findViewById(R.id.settings1);
        mFirstToggle.setStateSelected(new IStateSelected() {
            @Override
            public void selectState(int state) {
                Log.d("TAG", "First toggle selected = " + state);
            }
        });

    }

}

# FloatingToggleButton

Toggle button which has 2 or 3 states(optionally) and it has text inside of view for each state.

# Usage

For working implemination of this project see the `/app` folder.

1. Include following string in your `build.gradle` file:

```gradle
    compile 'com.zo2m4bie.floatingtoggle:floatingtoggle:0.0.4'
```

2. Include the FloatingToggleButton into your layout.

```xml
    <com.zo2m4bie.floatingtoggle.FloatingToggleButton
        android:id="@+id/toggle"
        android:id="match_parent"
        android:layout_width="match_parent"
        android:layout_height="@dimen/settings_toggle_height"
        widget:statecount="3"
        widget:backgrounddrawable="@drawable/layout_bg"
        widget:dotdrawable="@drawable/pin_bg"
        widget:textcolorfrom="@color/green"
        widget:textcolorto="@color/white"
        widget:text1="first"
        widget:text2="second"
        widget:text3="third" />
```

NOTE: Don't forget  include custom view attribute in top layout

     xmlns:widget="http://schemas.android.com/apk/res-auto"

3. In your activity/fragment bind view. Set StateSelected listener to be notified if state is changed.

        mFirstToggle = (FloatingToggleButton) findViewById(R.id.toggle);
        mFirstToggle.setStateSelected(new IStateSelected() {
            @Override
            public void selectState(int state) {
                Log.d("TAG", "State changed to = " + state);
            }
        });

# Customization

FloatingToggleButton has several options which you can use to customize toggle for your needs

* `statecount` set number of state into your view(max value = 3, min value = 2).
* For changing text inside of toggle use `text1`, `text2`, `text3` attributes.
* For changing text color use `textColor`. Text color by default is white.
* Use attributes `textcolorfrom` and `textcolorto` to animate changing text color on selected state.
* To customeze you background you can use:
  `backgroundcolor` - color from this attribute will used as background of toggle button
  `backgrounddrawable` - drawable from this attribute will used as background of toggle button
  `dotcolor` - color from this attribute will used as background for toggle's pin
  `dotdrawable` - drawable from this attribute will used as background for toggle's pin


# License

     Copyright (C) 2016 Dmytro Kovalenko
     Licensed under the Apache License, Version 2.0

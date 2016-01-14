package com.geniusmart.loveut;

import android.app.Activity;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ExampleUnitTest {

    @Test
    public void testMain() throws Exception {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        TextView view = (TextView) mainActivity.findViewById(R.id.textview);
        String s = view.getText().toString();
        assertEquals(s, "Hello World!");
    }

    @Test
    public void testLife(){
        ActivityController<MainActivity> activityController = Robolectric.buildActivity(MainActivity.class).create();
        activityController.resume();
    }
}
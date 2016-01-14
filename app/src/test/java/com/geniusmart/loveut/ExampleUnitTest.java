package com.geniusmart.loveut;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
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

    @Test
    public void testResource(){

        Context context = RuntimeEnvironment.application;
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        TextView view = (TextView) mainActivity.findViewById(R.id.textview);
        FloatingActionButton fab = (FloatingActionButton) mainActivity.findViewById(R.id.fab);
        fab.performClick();
        String s = view.getText().toString();
        assertEquals(s, context.getString(R.string.app_name));
    }

}
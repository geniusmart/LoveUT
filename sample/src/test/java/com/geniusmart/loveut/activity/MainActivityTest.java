package com.geniusmart.loveut.activity;

import android.app.Activity;
import android.view.Menu;

import com.geniusmart.loveut.BuildConfig;
import com.geniusmart.loveut.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class , sdk = 21)
public class MainActivityTest {

  @Test
  public void onCreateShouldInflateTheMenu() {
    Activity activity = Robolectric.setupActivity(MainActivity.class);

    final Menu menu = shadowOf(activity).getOptionsMenu();
    assertEquals(menu.findItem(R.id.item1).getTitle(),"First menu item");
    assertEquals(menu.findItem(R.id.item2).getTitle(),"Second menu item");
  }
}

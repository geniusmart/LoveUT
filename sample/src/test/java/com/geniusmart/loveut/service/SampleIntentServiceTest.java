package com.geniusmart.loveut.service;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.geniusmart.loveut.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboSharedPreferences;
import org.robolectric.util.ServiceController;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class , sdk = 21)
public class SampleIntentServiceTest {

    @Test
    public void addsDataToSharedPreference() {

        Application application = RuntimeEnvironment.application;
        RoboSharedPreferences preferences = (RoboSharedPreferences) application
                .getSharedPreferences("example", Context.MODE_PRIVATE);

        ServiceController<SampleIntentService> serviceController = Robolectric.buildService(SampleIntentService.class);

        serviceController.get().onHandleIntent(new Intent());

        assertEquals(preferences.getString("SAMPLE_DATA", ""), "sample data");
    }

}

package com.geniusmart.loveut.receiver;

import android.app.Application;
import android.content.Intent;

import com.geniusmart.loveut.BuildConfig;
import com.geniusmart.loveut.service.SampleIntentService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class , sdk = 21)
public class AlarmManagerReceiverTest {

    @Test
    public void startServiceForTheScheduledAlarm() {
        Application application = RuntimeEnvironment.application;
        Intent expectedService = new Intent(application, SampleIntentService.class);
        AlarmManagerReceiver alarmManagerReceiver = new AlarmManagerReceiver();
        alarmManagerReceiver.onReceive(application, new Intent());

        Intent serviceIntent = shadowOf(application).getNextStartedService();
        assertNotNull("Service started ",serviceIntent);
        assertEquals("Started service class ", serviceIntent.getComponent(),
                expectedService.getComponent());
    }
}

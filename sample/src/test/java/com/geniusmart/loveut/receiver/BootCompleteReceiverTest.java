package com.geniusmart.loveut.receiver;

import android.content.Intent;

import com.geniusmart.loveut.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class , sdk = 21)
public class BootCompleteReceiverTest {

    @Test
    public void registerServiceOnDeviceBootComplete() {
        Intent intent = new Intent(Intent.ACTION_BOOT_COMPLETED);

        ShadowApplication application = ShadowApplication.getInstance();

        assertTrue("Reboot Listener not registered ",
                application.hasReceiverForIntent(intent));
    }
}

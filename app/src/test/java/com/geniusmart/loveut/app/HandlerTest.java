package com.geniusmart.loveut.app;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.Transcript;

import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.internal.Shadow.newInstanceOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class HandlerTest {

    private Transcript transcript;
    TestRunnable scratchRunnable = new TestRunnable();

    private Handler.Callback callback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            hasHandlerCallbackHandledMessage = true;
            return false;
        }
    };

    private Boolean hasHandlerCallbackHandledMessage = false;

    @Before
    public void setUp() throws Exception {
        transcript = new Transcript();
    }

    @Test
    public void testInsertsRunnablesBasedOnLooper() throws Exception {
        Looper looper = newInstanceOf(Looper.class);

        Handler handler1 = new Handler(looper);
        handler1.post(new Say("first thing"));

        Handler handler2 = new Handler(looper);
        handler2.post(new Say("second thing"));

        shadowOf(looper).idle();

        transcript.assertEventsSoFar("first thing", "second thing");
    }

    private class Say implements Runnable {
        private String event;

        public Say(String event) {
            this.event = event;
        }

        @Override
        public void run() {
            transcript.add(event);
        }
    }
}

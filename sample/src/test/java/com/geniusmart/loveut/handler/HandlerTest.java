package com.geniusmart.loveut.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.geniusmart.loveut.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowHandler;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.util.Transcript;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.internal.Shadow.newInstanceOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class HandlerTest {

    private String TAG = "HandlerTest";
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
        ShadowLog.stream = System.out;
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

    @Test
    public void testDefaultConstructorUsesDefaultLooper() throws Exception {
        Handler handler1 = new Handler();
        handler1.post(new Say("first thing"));

        Handler handler2 = new Handler(Looper.myLooper());
        handler2.post(new Say("second thing"));

        shadowOf(Looper.myLooper()).idle();

        transcript.assertEventsSoFar("first thing", "second thing");
    }

    @Test
    public void testDifferentLoopersGetDifferentQueues() throws Exception {
        Looper looper1 = newInstanceOf(Looper.class);
        ShadowLooper.pauseLooper(looper1);

        Looper looper2 = newInstanceOf(Looper.class);
        ShadowLooper.pauseLooper(looper2);

        Handler handler1 = new Handler(looper1);
        handler1.post(new Say("first thing"));

        Handler handler2 = new Handler(looper2);
        handler2.post(new Say("second thing"));

        shadowOf(looper2).idle();

        transcript.assertEventsSoFar("second thing");
    }

    @Test
    public void shouldCallProvidedHandlerCallback() {
        Handler handler = new Handler(callback);
        handler.sendMessage(new Message());
        assertTrue(hasHandlerCallbackHandledMessage);
    }

    @Test
    public void testPostAndIdleMainLooper() throws Exception {
        new Handler().post(scratchRunnable);
        ShadowLooper.idleMainLooper();
        assertTrue(scratchRunnable.wasRun);
    }

    @Test
    public void postDelayedThenIdleMainLooper_shouldNotRunRunnable() throws Exception {
        new Handler().postDelayed(scratchRunnable, 1);
        ShadowLooper.idleMainLooper();
        assertFalse(scratchRunnable.wasRun);
    }

    @Test
    public void testPostDelayedThenRunMainLooperOneTask() throws Exception {
        new Handler().postDelayed(scratchRunnable, 1);
        ShadowLooper.runMainLooperOneTask();
        assertTrue(scratchRunnable.wasRun);
    }

    @Test
    public void testRemoveCallbacks() throws Exception {
        Handler handler = new Handler();
        ShadowLooper shadowLooper = shadowOf(handler.getLooper());
        shadowLooper.pause();
        handler.post(scratchRunnable);
        handler.removeCallbacks(scratchRunnable);

        shadowLooper.unPause();

        assertFalse(scratchRunnable.wasRun);
    }

    @Test
    public void testPostDelayedThenRunMainLooperToNextTask_shouldRunOneTask() throws Exception {
        new Handler().postDelayed(scratchRunnable, 1);
        ShadowLooper.runMainLooperToNextTask();
        assertTrue(scratchRunnable.wasRun);
    }

    @Test
    public void testPostDelayedTwiceThenRunMainLooperToNextTask_shouldRunMultipleTasks() throws Exception {
        TestRunnable task1 = new TestRunnable();
        TestRunnable task2 = new TestRunnable();

        new Handler().postDelayed(task1, 1);
        new Handler().postDelayed(task2, 1);

        ShadowLooper.runMainLooperToNextTask();
        assertTrue(task1.wasRun);
        assertTrue(task2.wasRun);
    }

    @Test
    public void testPostDelayedTwiceThenRunMainLooperOneTask_shouldRunOnlyOneTask() throws Exception {
        TestRunnable task1 = new TestRunnable();
        TestRunnable task2 = new TestRunnable();

        new Handler().postDelayed(task1, 1);
        new Handler().postDelayed(task2, 1);

        ShadowLooper.runMainLooperOneTask();
        assertTrue(task1.wasRun);
        assertFalse(task2.wasRun);
    }

    @Test
    public void testPostDelayedMultipleThenRunMainLooperOneTask_shouldRunMultipleTask() throws Exception {
        TestRunnable task1 = new TestRunnable();
        TestRunnable task2 = new TestRunnable();
        TestRunnable task3 = new TestRunnable();

        new Handler().postDelayed(task1, 1);
        new Handler().postDelayed(task2, 10);
        new Handler().postDelayed(task3, 100);

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        assertTrue(task1.wasRun);
        assertTrue(task2.wasRun);
        assertTrue(task3.wasRun);
    }

    @Test
    public void testPostAtFrontOfQueueThenRunMainLooperOneTaskAtATime_shouldRunFrontOfQueueTaskFirst() throws Exception {
        TestRunnable task1 = new TestRunnable();
        TestRunnable task2 = new TestRunnable();

        ShadowLooper.pauseMainLooper();
        new Handler().post(task1);
        boolean result = new Handler().postAtFrontOfQueue(task2);

        assertTrue(result);

        ShadowLooper.runMainLooperOneTask();
        assertTrue(task2.wasRun);
        assertFalse(task1.wasRun);
        ShadowLooper.runMainLooperOneTask();
        assertTrue(task1.wasRun);
    }

    @Test
    public void testSendMessageAtFrontOfQueueThenRunMainLooperOneMsgAtATime_shouldRunFrontOfQueueMsgFirst() throws Exception {
        Handler handler = new Handler();

        ShadowLooper.pauseMainLooper();
        // Post two messages to handler. Handle first message and confirm that msg posted
        // to front is removed.
        handler.obtainMessage(123).sendToTarget();
        Message frontMsg = handler.obtainMessage(345);
        boolean result = handler.sendMessageAtFrontOfQueue(frontMsg);

        assertTrue(result);

        assertTrue(handler.hasMessages(123));
        assertTrue(handler.hasMessages(345));
        ShadowHandler.runMainLooperOneTask();
        assertTrue(handler.hasMessages(123));
        assertFalse(handler.hasMessages(345));
        ShadowHandler.runMainLooperOneTask();
        assertFalse(handler.hasMessages(123));
        assertFalse(handler.hasMessages(345));
    }

    @Test
    public void sendEmptyMessage_addMessageToQueue() {
        ShadowLooper.pauseMainLooper();
        Handler handler = new Handler();
        assertThat(handler.hasMessages(123), equalTo(false));
        handler.sendEmptyMessage(123);
        assertThat(handler.hasMessages(456), equalTo(false));
        assertThat(handler.hasMessages(123), equalTo(true));
        ShadowLooper.idleMainLooper(0);
        assertThat(handler.hasMessages(123), equalTo(false));
    }



    @Test
    public void sendEmptyMessageDelayed_sendsMessageAtCorrectTime() {
        ShadowLooper.pauseMainLooper();
        Handler handler = new Handler();
        handler.sendEmptyMessageDelayed(123, 500);
        assertThat(handler.hasMessages(123), equalTo(true));
        ShadowLooper.idleMainLooper(100);
        assertThat(handler.hasMessages(123), equalTo(true));
        ShadowLooper.idleMainLooper(400);
        assertThat(handler.hasMessages(123), equalTo(false));
    }

    @Test
    public void removeMessages_takesMessageOutOfQueue() {
        ShadowLooper.pauseMainLooper();
        Handler handler = new Handler();
        handler.sendEmptyMessageDelayed(123, 500);
        handler.removeMessages(123);
        assertThat(handler.hasMessages(123), equalTo(false));
    }

    @Test
    public void removeMessage_withSpecifiedObject() throws Exception {
        ShadowLooper.pauseMainLooper();
        Handler handler = new Handler();
        Message.obtain(handler, 123, "foo").sendToTarget();
        Message.obtain(handler, 123, "bar").sendToTarget();

        assertThat(handler.hasMessages(123), equalTo(true));
        assertThat(handler.hasMessages(123, "foo"), equalTo(true));
        assertThat(handler.hasMessages(123, "bar"), equalTo(true));
        assertThat(handler.hasMessages(123, "baz"), equalTo(false));

        handler.removeMessages(123, "foo");
        assertThat(handler.hasMessages(123), equalTo(true));

        handler.removeMessages(123, "bar");
        assertThat(handler.hasMessages(123), equalTo(false));
    }

    @Test
    public void testHasMessagesWithWhatAndObject() {
        ShadowLooper.pauseMainLooper();
        Object testObject = new Object();
        Handler handler = new Handler();
        Message message = handler.obtainMessage(123, testObject);

        assertFalse(handler.hasMessages(123, testObject));

        handler.sendMessage(message);

        assertTrue(handler.hasMessages(123, testObject));
    }

    @Test
    public void testSendToTarget() {
        ShadowLooper.pauseMainLooper();
        Object testObject = new Object();
        Handler handler = new Handler();
        Message message = handler.obtainMessage(123, testObject);

        assertThat(handler, equalTo(message.getTarget()));

        message.sendToTarget();

        assertTrue(handler.hasMessages(123, testObject));
    }

    @Test
    public void removeMessages_removesFromLooperQueueAsWell() {
        final boolean[] wasRun = new boolean[1];
        ShadowLooper.pauseMainLooper();
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                wasRun[0] = true;
            }
        };
        handler.sendEmptyMessageDelayed(123, 500);
        handler.removeMessages(123);
        ShadowLooper.unPauseMainLooper();
        assertThat(wasRun[0], equalTo(false));
    }

    @Test
    public void shouldObtainMessage() throws Exception {
        Message m0 = new Handler().obtainMessage();
        assertThat(m0.what, equalTo(0));
        assertThat(m0.obj, nullValue());

        Message m1 = new Handler().obtainMessage(1);
        assertThat(m1.what, equalTo(1));
        assertThat(m1.obj, nullValue());

        Message m2 = new Handler().obtainMessage(1, "foo");
        assertThat(m2.what, equalTo(1));
        assertThat(m2.obj, equalTo((Object)"foo"));

        Message m3 = new Handler().obtainMessage(1, 2, 3);
        assertThat(m3.what, equalTo(1));
        assertThat(m3.arg1, equalTo(2));
        assertThat(m3.arg2, equalTo(3));
        assertThat(m3.obj, nullValue());

        Message m4 = new Handler().obtainMessage(1, 2, 3, "foo");
        assertThat(m4.what, equalTo(1));
        assertThat(m4.arg1, equalTo(2));
        assertThat(m4.arg2, equalTo(3));
        assertThat(m4.obj, equalTo((Object) "foo"));
    }

    @Test
    public void shouldSetWhenOnMessage() throws Exception {
        final List<Message> msgs = new ArrayList<Message>();
        Handler h = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                msgs.add(msg);
                return false;
            }
        });

        h.sendEmptyMessage(0);
        h.sendEmptyMessageDelayed(0, 4000l);
        Robolectric.getForegroundThreadScheduler().advanceToLastPostedRunnable();
        h.sendEmptyMessageDelayed(0, 12000l);
        Robolectric.getForegroundThreadScheduler().advanceToLastPostedRunnable();
        assertThat(msgs.size(), equalTo(3));

        Message m0 = msgs.get(0);
        Message m1 = msgs.get(1);
        Message m2 = msgs.get(2);

        assertThat(m0.getWhen(), equalTo(0l));
        assertThat(m1.getWhen(), equalTo(4000l));
        assertThat(m2.getWhen(), equalTo(16000l));
    }

    @Test
    public void shouldRemoveMessageFromQueueBeforeDispatching() throws Exception {
        Handler h = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                assertFalse(hasMessages(0));
            }
        };
        h.sendEmptyMessage(0);
        h.sendMessageAtFrontOfQueue(h.obtainMessage());
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

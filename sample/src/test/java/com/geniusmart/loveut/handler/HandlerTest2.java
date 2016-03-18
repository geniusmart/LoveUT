package com.geniusmart.loveut.handler;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.geniusmart.loveut.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowLooper;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class HandlerTest2 {

    private String TAG = "HandlerTest";

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
    }

    /**
     * 验证UT执行在主线程中
     */
    @Test
    public void testLooper() {
        assertEquals(Looper.myLooper(), Looper.getMainLooper());
        assertEquals(Thread.currentThread(), Looper.getMainLooper().getThread());
    }

    /**
     * Handler默认持有Looper对象，且为MainLooper
     */
    @Test
    public void testHandler() {
        Handler handler = new Handler();
        assertEquals(handler.getLooper(), Looper.getMainLooper());
    }

    @Test //TODO
    public void testWhen() throws InterruptedException {
        final List<Message> msgs = new ArrayList<Message>();
        final long start = SystemClock.currentThreadTimeMillis();
        Handler h = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                msgs.add(msg);
                Log.i(TAG, String.valueOf(SystemClock.currentThreadTimeMillis() - start));
                return false;
            }
        });
        h.sendEmptyMessageDelayed(100, 1000l);
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        Message m0 = msgs.get(0);
        assertEquals(m0.getWhen(), 1000l);
        ShadowLog.i(TAG, "here");
    }

    /**
     * 为Handler分配Looper对象--TODO
     */
    @Test
    public void test1() {
        OneThread oneThread = new OneThread();
        oneThread.start();
        assertNotNull(oneThread.looper);
        assertEquals(oneThread.looper, Looper.getMainLooper());
    }

    @Test //TODO
    public void testHandlerThread() {

        HandlerThread thread = new HandlerThread("name");
        //启动子线程，并创建与其关联的Looper对象
        thread.start();

        //主线程Handler
        final Handler mainHandler = new Handler();
        //主线程Looper
        final Looper mainLooper = Looper.getMainLooper();
        //子线程Looper
        Looper threadLooper = thread.getLooper();
        //关联子线程Handler和Looper对象
        Handler threadHandler = new Handler(thread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.e(TAG,"1111111111111111111");
                Message.obtain(mainHandler, msg.what + 1,
                        msg.obj + "-TO主线程").sendToTarget();
            }
        };

        assertNotEquals(threadLooper, mainLooper);
        assertEquals(mainHandler.getLooper(), mainLooper);

        Message threadMessage = Message.obtain(threadHandler, 1, "TO子线程");
        threadMessage.sendToTarget();

        assertTrue(threadHandler.hasMessages(1, "TO子线程"));
        assertTrue(mainHandler.hasMessages(2, "TO子线程-TO主线程"));

    }

    private class OneThread extends Thread {

        Looper looper;

        @Override
        public void run() {
            Looper.prepare();
            looper = Looper.myLooper();
            Looper.loop();
        }
    }
}

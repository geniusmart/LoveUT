package com.geniusmart.loveut.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.geniusmart.loveut.BuildConfig;
import com.geniusmart.loveut.R;
import com.geniusmart.loveut.fragment.SampleFragment;
import com.geniusmart.loveut.receiver.MyReceiver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.util.ActivityController;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class SampleActivityTest {

    private SampleActivity sampleActivity;
    private Button forwardBtn;
    private Button dialogBtn;
    private Button toastBtn;

    @Before
    public void setUp() {
        sampleActivity = Robolectric.setupActivity(SampleActivity.class);
        forwardBtn = (Button) sampleActivity.findViewById(R.id.btn_forward);
        dialogBtn = (Button) sampleActivity.findViewById(R.id.btn_dialog);
        toastBtn = (Button) sampleActivity.findViewById(R.id.btn_toast);
    }

    /**
     * 最基本的Activity测试
     */
    @Test
    public void testActivity() {
        SampleActivity sampleActivity = Robolectric.setupActivity(SampleActivity.class);
        assertNotNull(sampleActivity);
        assertEquals(sampleActivity.getTitle(), "SimpleActivity");
    }

    /**
     * Activity生命周期测试
     */
    @Test
    public void testLifecycle() {
        ActivityController<SampleActivity> activityController = Robolectric.buildActivity(SampleActivity.class).create().start();
        Activity activity = activityController.get();
        TextView textview = (TextView) activity.findViewById(R.id.tv_lifecycle_value);
        assertEquals("onCreate",textview.getText().toString());
        activityController.resume();
        assertEquals("onResume", textview.getText().toString());
        activityController.destroy();
        assertEquals("onDestroy", textview.getText().toString());
    }

    /**
     * Activity跳转测试
     */
    @Test
    public void testStartActivity() {
        //按钮点击后跳转到下一个Activity
        forwardBtn.performClick();
        Intent expectedIntent = new Intent(sampleActivity, LoginActivity.class);
        Intent actualIntent = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent, actualIntent);
    }

    /**
     * Toast的测试
     */
    @Test
    public void testToast(){
        //点击按钮，出现吐司
        toastBtn.performClick();
        assertEquals(ShadowToast.getTextOfLatestToast(),"we love UT");
    }

    /**
     * Dialog的测试
     */
    @Test
    public void testDialog(){
        //点击按钮，出现对话框
        dialogBtn.performClick();
        AlertDialog latestAlertDialog = ShadowAlertDialog.getLatestAlertDialog();
        assertNotNull(latestAlertDialog);
    }

    /**
     * 测试控件状态
     */
    @Test
    public void testViewState(){
        CheckBox checkBox = (CheckBox) sampleActivity.findViewById(R.id.checkbox);
        Button inverseBtn = (Button) sampleActivity.findViewById(R.id.btn_inverse);
        assertTrue(inverseBtn.isEnabled());

        checkBox.setChecked(true);
        //点击按钮，CheckBox反选
        inverseBtn.performClick();
        assertTrue(!checkBox.isChecked());
        inverseBtn.performClick();
        assertTrue(checkBox.isChecked());
    }

    /**
     * 资源文件访问测试
     */
    @Test
    public void testResources() {
        Application application = RuntimeEnvironment.application;
        String appName = application.getString(R.string.app_name);
        String activityTitle = application.getString(R.string.title_activity_simple);
        assertEquals("LoveUT", appName);
        assertEquals("SimpleActivity", activityTitle);
    }

    /**
     * 测试广播
     */
    @Test
    public void testBoradcast(){
        ShadowApplication shadowApplication = ShadowApplication.getInstance();

        String action = "com.geniusmart.loveut.login";
        Intent intent = new Intent(action);
        intent.putExtra("EXTRA_USERNAME", "geniusmart");

        //测试是否注册广播接收者
        assertTrue(shadowApplication.hasReceiverForIntent(intent));

        //以下测试广播接受者的处理逻辑是否正确
        MyReceiver myReceiver = new MyReceiver();
        myReceiver.onReceive(RuntimeEnvironment.application, intent);
        SharedPreferences preferences = shadowApplication.getSharedPreferences("account", Context.MODE_PRIVATE);
        assertEquals("geniusmart",preferences.getString("USERNAME", ""));
    }

    @Test
    public void testFragment(){
        //FragmentTestUtil.startFragment(sampleActivity.getSupportFragmentManager().findFragmentById(R.id.fragment));


        SampleFragment sampleFragment = new SampleFragment();
        //SupportFragmentTestUtil.startFragment(sampleFragment);

        //Fragment fragment = sampleActivity.getSupportFragmentManager().findFragmentById(R.id.fragment);
        assertNotNull(sampleFragment);

    }
}

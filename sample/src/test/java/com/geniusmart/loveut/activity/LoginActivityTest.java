package com.geniusmart.loveut.activity;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import com.geniusmart.loveut.BuildConfig;
import com.geniusmart.loveut.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class , sdk = 21)
public class LoginActivityTest {

    private EditText emailView;
    private EditText passwordView;
    private Button button;

    @Before
    public void setUp() {
        Activity activity = Robolectric.setupActivity(LoginActivity.class);
        button = (Button) activity.findViewById(R.id.email_sign_in_button);
        emailView = (EditText) activity.findViewById(R.id.email);
        passwordView = (EditText) activity.findViewById(R.id.password);
    }

    @Test
    public void loginSuccess() {
        emailView.setText("zhangzhan35@gmail.com");
        passwordView.setText("123");
        button.performClick();

        ShadowApplication application = ShadowApplication.getInstance();
        assertThat("Next activity has started", application.getNextStartedActivity(), is(notNullValue()));
    }

    @Test
    public void loginWithEmptyUsernameAndPassword() {
        button.performClick();

        ShadowApplication application = ShadowApplication.getInstance();
        assertThat("Next activity should not started", application.getNextStartedActivity(), is(nullValue()));
        assertThat("Show error for Email field ", emailView.getError(), is(notNullValue()));
        assertThat("Show error for Password field ", passwordView.getError(), is(notNullValue()));

        assertEquals(emailView.getError().toString(), RuntimeEnvironment.application.getString(R.string.error_field_required));
    }

    @Test
    public void loginFailure() {
        emailView.setText("invalid@email");
        passwordView.setText("invalidpassword");
        button.performClick();

        ShadowApplication application = ShadowApplication.getInstance();
        assertThat("Next activity should not started", application.getNextStartedActivity(), is(nullValue()));
        assertThat("Show error for Email field ", emailView.getError(), is(notNullValue()));
        assertThat("Show error for Password field ", passwordView.getError(), is(notNullValue()));
    }
}

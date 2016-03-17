package com.geniusmart.loveut.net;

import android.widget.ListAdapter;
import android.widget.ListView;

import com.geniusmart.loveut.BuildConfig;
import com.geniusmart.loveut.R;
import com.geniusmart.loveut.activity.CallbackActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowListView;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowToast;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MockGithubServiceTest {

    private static final String JSON_ROOT_PATH = "/json/";
    private String jsonFullPath;
    GithubService githubService;

    @Before
    public void setUp() throws URISyntaxException {

        //输出日志
        ShadowLog.stream = System.out;
        //获取测试json文件地址
        jsonFullPath = getClass().getResource(JSON_ROOT_PATH).toURI().getPath();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new MockInterceptor(jsonFullPath))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GithubService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        githubService = retrofit.create(GithubService.class);
    }

    @Test
    public void mockPublicRepositories() throws Exception {
        Response<List<Repository>> repositoryResponse = githubService.publicRepositories("geniusmart").execute();
        assertEquals(repositoryResponse.body().get(5).name, "LoveUT");
    }

    @Test
    public void mockFollowingUser() throws Exception {
        Response<List<User>> followingResponse = githubService.followingUser("geniusmart").execute();
        assertEquals(followingResponse.body().get(0).login,"JakeWharton");
    }

    @Test
    public void mockUser() throws Exception {
        Response<User> userResponse = githubService.user("geniusmart").execute();
        assertEquals(userResponse.body().login,"geniusmart");
    }

    @Test
    public void callback() throws IOException {
        CallbackActivity callbackActivity = Robolectric.setupActivity(CallbackActivity.class);
        ListView listView = (ListView) callbackActivity.findViewById(R.id.listView);
        callbackActivity.getCallback().onResponse(null, githubService.followingUser("geniusmart").execute());
        ListAdapter listAdapter = listView.getAdapter();

        assertEquals(listAdapter.getItem(0).toString(), "JakeWharton");
        assertEquals(listAdapter.getItem(1).toString(), "Trinea");

        ShadowListView shadowListView = Shadows.shadowOf(listView);

        //测试点击ListView的第3~5个Item后，吐司的文本
        shadowListView.performItemClick(2);
        assertEquals(ShadowToast.getTextOfLatestToast(), "daimajia");
        shadowListView.performItemClick(3);
        assertEquals(ShadowToast.getTextOfLatestToast(), "liaohuqiu");
        shadowListView.performItemClick(4);
        assertEquals(ShadowToast.getTextOfLatestToast(), "stormzhang");
    }
}

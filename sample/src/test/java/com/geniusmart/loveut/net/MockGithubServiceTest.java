package com.geniusmart.loveut.net;

import com.geniusmart.loveut.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

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
                .addInterceptor(new FekeInterceptor(jsonFullPath))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GithubService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        githubService = retrofit.create(GithubService.class);
    }

    @Test
    public void LoggingInterceptor() throws Exception {

        Response<List<Repository>> repositoryResponse = githubService.publicRepositories("geniusmart").execute();
        assertEquals(repositoryResponse.body().get(5).name, "LoveUT");

        Response<List<User>> followingResponse = githubService.followingUser("geniusmart").execute();
        assertEquals(followingResponse.body().get(0).login,"JakeWharton");

        Response<User> userResponse = githubService.user("geniusmart").execute();
        assertEquals(userResponse.body().login,"geniusmart");
    }
}

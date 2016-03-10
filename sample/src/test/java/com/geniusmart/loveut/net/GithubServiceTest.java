package com.geniusmart.loveut.net;

import com.geniusmart.loveut.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class GithubServiceTest {

    @Test
    public void git() throws Exception {

        GithubService githubService = GithubService.Factory.create();
        Call<List<Repository>> call = githubService.publicRepositories("geniusmart");

        call.enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                List<Repository> body = response.body();
                assertTrue(body.size()>0);
            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {

            }
        });
        //Response<List<Repository>> listResponse = call.execute();
        //List<Repository> body = listResponse.body();

        //assertTrue(body.size()>0);
    }

}
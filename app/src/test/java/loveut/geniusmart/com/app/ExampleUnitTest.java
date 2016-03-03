package loveut.geniusmart.com.app;

import com.geniusmart.loveut.app.BuildConfig;
import com.geniusmart.loveut.model.GithubService;
import com.geniusmart.loveut.model.Repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import retrofit2.Call;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ExampleUnitTest {

    @Test
    public void git() throws Exception {

        GithubService githubService = GithubService.Factory.create();
        Call<List<Repository>> call = githubService.publicRepositories("geniusmart");
    }
}
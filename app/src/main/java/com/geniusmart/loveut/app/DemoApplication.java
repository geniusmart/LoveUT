package com.geniusmart.loveut.app;

import android.app.Application;

import com.geniusmart.loveut.model.GithubService;

public class DemoApplication extends Application {

    private GithubService githubService;

    public GithubService getGithubService() {
        if (githubService == null) {
            githubService = GithubService.Factory.create();
        }
        return githubService;
    }

}

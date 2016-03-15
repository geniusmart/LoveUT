package com.geniusmart.loveut.net;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 参考文章：
 * http://stackoverflow.com/questions/17544751/square-retrofit-server-mock-for-testing
 * https://github.com/square/okhttp/wiki/Interceptors
 */
public class FekeInterceptor implements Interceptor {


    public String TAG = "FekeInterceptor";

    public static final String RESPONSE_1 = "[{\"id\":52758536,\"name\":\"android-support-23.2-sample\",\"description\":\"Sample Project for Android Support Library 23.2\",\"forks\":0,\"watchers\":0,\"stargazers_count\":0,\"language\":\"Java\",\"homepage\":\"http://android-developers.blogspot.com/2016/02/android-support-library-232.html\",\"owner\":{\"id\":7712056,\"url\":\"https://api.github.com/users/geniusmart\",\"login\":\"geniusmart\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/7712056?v\\u003d3\"},\"fork\":true},{\"id\":48989399,\"name\":\"AndroidStudioAndRobolectric\",\"description\":\"Minimal Robolectric and Android Studio example\",\"forks\":0,\"watchers\":0,\"stargazers_count\":0,\"language\":\"Java\",\"homepage\":\"\",\"owner\":{\"id\":7712056,\"url\":\"https://api.github.com/users/geniusmart\",\"login\":\"geniusmart\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/7712056?v\\u003d3\"},\"fork\":true},{\"id\":44965598,\"name\":\"custom-touch-examples\",\"description\":\"Collection of example applications to highlight doing custom touch event handling and using GestureDetectors in Android applications.\",\"forks\":0,\"watchers\":0,\"stargazers_count\":0,\"language\":\"Java\",\"owner\":{\"id\":7712056,\"url\":\"https://api.github.com/users/geniusmart\",\"login\":\"geniusmart\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/7712056?v\\u003d3\"},\"fork\":true},{\"id\":32380192,\"name\":\"CustomTouch\",\"description\":\"Android Touch\",\"forks\":0,\"watchers\":0,\"stargazers_count\":0,\"language\":\"Java\",\"owner\":{\"id\":7712056,\"url\":\"https://api.github.com/users/geniusmart\",\"login\":\"geniusmart\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/7712056?v\\u003d3\"},\"fork\":false},{\"id\":52280267,\"name\":\"DailyStudy\",\"description\":\"\",\"forks\":0,\"watchers\":0,\"stargazers_count\":0,\"language\":\"Java\",\"owner\":{\"id\":7712056,\"url\":\"https://api.github.com/users/geniusmart\",\"login\":\"geniusmart\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/7712056?v\\u003d3\"},\"fork\":false},{\"id\":51822817,\"name\":\"DroidPlugin\",\"description\":\"A plugin framework on android,Run any third-party apk without installation, modification or repackage\",\"forks\":0,\"watchers\":0,\"stargazers_count\":0,\"language\":\"Java\",\"homepage\":\"\",\"owner\":{\"id\":7712056,\"url\":\"https://api.github.com/users/geniusmart\",\"login\":\"geniusmart\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/7712056?v\\u003d3\"},\"fork\":true},{\"id\":33351891,\"name\":\"geniusmart.github.com\",\"description\":\"个人博客\",\"forks\":0,\"watchers\":0,\"stargazers_count\":0,\"owner\":{\"id\":7712056,\"url\":\"https://api.github.com/users/geniusmart\",\"login\":\"geniusmart\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/7712056?v\\u003d3\"},\"fork\":false},{\"id\":42001141,\"name\":\"handler-project\",\"description\":\"\",\"forks\":0,\"watchers\":0,\"stargazers_count\":0,\"language\":\"Java\",\"owner\":{\"id\":7712056,\"url\":\"https://api.github.com/users/geniusmart\",\"login\":\"geniusmart\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/7712056?v\\u003d3\"},\"fork\":false},{\"id\":49635520,\"name\":\"LoveUT\",\"description\":\"Love UT Forever\",\"forks\":3,\"watchers\":10,\"stargazers_count\":10,\"language\":\"Java\",\"owner\":{\"id\":7712056,\"url\":\"https://api.github.com/users/geniusmart\",\"login\":\"geniusmart\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/7712056?v\\u003d3\"},\"fork\":false},{\"id\":51972602,\"name\":\"MultiWindowSidebar\",\"description\":\"Android: MultiWindowSidebar, a sidebar app that mimics the Samsung MultiWindow FlashBar\",\"forks\":0,\"watchers\":0,\"stargazers_count\":0,\"language\":\"Java\",\"homepage\":\"\",\"owner\":{\"id\":7712056,\"url\":\"https://api.github.com/users/geniusmart\",\"login\":\"geniusmart\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/7712056?v\\u003d3\"},\"fork\":true},{\"id\":30139436,\"name\":\"totop\",\"description\":\"\",\"forks\":0,\"watchers\":0,\"stargazers_count\":0,\"language\":\"Java\",\"owner\":{\"id\":7712056,\"url\":\"https://api.github.com/users/geniusmart\",\"login\":\"geniusmart\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/7712056?v\\u003d3\"},\"fork\":false}]";

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {

        String responseString = RESPONSE_1;

        HttpUrl uri = chain.request().url();
        Log.i(TAG, "uri=" + uri.toString());
        if (uri.toString().equals("/path/of/interest")) {
            responseString = "";
        } else {
            responseString = RESPONSE_1;
        }

        // Get Query String.
        //final String query = uri.query();
        // Parse the Query String.final String[] parsedQuery = query.split("=");
       /* if(parsedQuery[0].equalsIgnoreCase("id") && parsedQuery[1].equalsIgnoreCase("1")) {
            responseString = TEACHER_ID_1;
        }
        else if(parsedQuery[0].equalsIgnoreCase("id") && parsedQuery[1].equalsIgnoreCase("2")){
            responseString = TEACHER_ID_2;
        }
        else {
            responseString = "";
        }*/

        Response response = new Response.Builder()
                .code(200)
                .message(responseString)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                .addHeader("content-type", "application/json")
                .build();
        return response;
    }

}

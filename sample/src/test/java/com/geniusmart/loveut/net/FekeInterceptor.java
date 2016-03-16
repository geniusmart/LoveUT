package com.geniusmart.loveut.net;

import android.util.Log;

import com.geniusmart.loveut.util.FileUtil;

import java.io.IOException;
import java.util.List;

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

    private final String jsonPath;
    private List<String> pathSegments;

    public FekeInterceptor(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {

        String responseString = createResponseBody(chain);

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

    /**
     * 读文件获取json，生成ResponseBody
     *
     * @param chain
     * @return
     */
    private String createResponseBody(Chain chain) {

        String responseString = null;

        HttpUrl uri = chain.request().url();
        pathSegments = uri.pathSegments();

        Log.i(TAG, "uri=" + uri.toString());
        Log.i(TAG, "pathSegments=" + pathSegments);

        //可根据请求的路径分段pathSegments和请求参数queryParameterNames来区分路径
        switch (pathSegments.size()) {
            case 2:
                if (equals("users", 0)) { //users/{username}
                    responseString = getResponseString("users.json");
                }
                break;
            case 3:
                if (equals("users", 0) && equals("repos", 2)) {//users/{username}/repos
                    responseString = getResponseString("users_repos.json");
                } else if (equals("users", 0) && equals("following", 2)) {
                    responseString = getResponseString("users_following.json");
                }
                break;
        }
        return responseString;
    }

    private boolean equals(String pathSegment, int pathSegmentIndex) {
        return pathSegment.equals(pathSegments.get(pathSegmentIndex));
    }

    private String getResponseString(String fileName) {
        return FileUtil.readFile(jsonPath + fileName, "UTF-8").toString();
    }

}

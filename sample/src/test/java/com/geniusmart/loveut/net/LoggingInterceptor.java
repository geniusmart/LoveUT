package com.geniusmart.loveut.net;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoggingInterceptor implements Interceptor {

    //参考http://stackoverflow.com/questions/17544751/square-retrofit-server-mock-for-testing
    //https://github.com/square/okhttp/wiki/Interceptors

    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        /*Request request = chain.request();

        long t1 = System.nanoTime();
        System.out.println(String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        System.out.println(String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));*/


        String responseString = "";

        HttpUrl uri = chain.request().url();
        if(uri.toString().equals("/path/of/interest")) {
            responseString = "JSON STRING HERE";
        } else {
            responseString = "OTHER JSON RESPONSE STRING";
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

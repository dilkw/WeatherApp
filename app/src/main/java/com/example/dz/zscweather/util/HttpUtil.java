package com.example.dz.zscweather.util;

import android.app.DownloadManager;
import android.net.Uri;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by DZ on 2019/11/12.
 */

public class HttpUtil {

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .get()
//                .url(address)
//                .build();
        Request request = new Request.Builder().get().url(address).build();
        client.newCall(request).enqueue(callback);
        Log.d("text", "sendOkHttpRequest: " + "method" + request.method() + "url" + request.url());
    }


}

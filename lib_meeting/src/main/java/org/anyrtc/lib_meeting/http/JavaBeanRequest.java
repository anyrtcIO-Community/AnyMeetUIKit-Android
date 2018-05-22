package org.anyrtc.lib_meeting.http;

import android.util.Log;

import com.google.gson.Gson;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.RestRequest;
import com.yanzhenjie.nohttp.rest.StringRequest;

/**
 * Created by liuxiaozhong on 2016/10/21.
 */

public class JavaBeanRequest<T> extends RestRequest<T> {

    private Class<T> clazz;

    public JavaBeanRequest(String url, Class<T> clazz) {
        this(url, RequestMethod.POST, clazz);
    }

    public JavaBeanRequest(String url, RequestMethod requestMethod, Class<T> clazz) {
        super(url, requestMethod);
        this.clazz = clazz;
    }

    @Override
    public T parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception {
        String response = StringRequest.parseResponseString(responseHeaders, responseBody);
        Log.e("HttpInfo",response);
        Gson JSON=new Gson();
        return JSON.fromJson(response, clazz);
    }
}
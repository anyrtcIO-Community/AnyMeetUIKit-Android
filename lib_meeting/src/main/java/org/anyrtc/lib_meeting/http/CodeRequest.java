package org.anyrtc.lib_meeting.http;

import android.util.Log;

import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.RestRequest;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.json.JSONObject;

/**
 * Created by liuxiaozhong on 2016/10/24.
 */

public class CodeRequest extends RestRequest<Integer> {
    public CodeRequest(String url) {
        this(url, RequestMethod.POST);
    }

    public CodeRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
    }


    @Override
    public Integer parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception {
        String response = StringRequest.parseResponseString(responseHeaders, responseBody);
        JSONObject jsonObject = new JSONObject(response);
        Log.i("HttpInfo", "parseResponse: " + response);
        return jsonObject.getInt("code");
    }
}

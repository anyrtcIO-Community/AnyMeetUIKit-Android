package org.anyrtc.lib_meeting.http;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import org.anyrtc.lib_meeting.meet.MeetSDK;
import org.anyrtc.lib_meeting.utils.MD5;
import org.anyrtc.lib_meeting.utils.StringUtils;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by liuxiaozhong on 2017/12/18.
 */

public class NetHttp {

    private static NetHttp instance;

    public static final String SERVER_URL = "http://meeting.anyrtc.cc:8799/teameeting/gateway/";
    public static NetHttp getInstance() {
        if (instance == null)
            synchronized (NetHttp.class) {
                if (instance == null)
                    instance = new NetHttp();
            }
        return instance;
    }

    private RequestQueue queue;

    private NetHttp() {
        queue = NoHttp.newRequestQueue(5);
    }

    public <T> void request(int what, Request<T> request, final ResultListener<T> listener) {
        queue.add(what, request, new SimpleResponseListener<T>() {
            @Override
            public void onStart(int what) {
                super.onStart(what);
            }

            @Override
            public void onSucceed(int what, Response<T> response) {
                listener.onSucceed(what, response);
            }

            @Override
            public void onFailed(int what, Response<T> response) {
                listener.onFailed(what, response);
            }

            @Override
            public void onFinish(int what) {
                super.onFinish(what);
            }
        });
    }

    private  static String getMD5FormatUrlNoSign(Map<String, String> paraMap) {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {

                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (!StringUtils.isEmpty(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    buf.append(key + "=" + val);
                    buf.append("&");
                }

            }
            buff = buf.toString();
            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            return null;
        }
        return MD5.getMD5(MeetSDK.getInstance().APPKEY+buff+ MeetSDK.getInstance().APPTOKEN);
    }

    private static   String getFullFormatUrl(Map<String, String> paraMap) {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {

                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (!StringUtils.isEmpty(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    val= URLEncoder.encode(val, "utf-8");
                    buf.append(key + "=" + val);
                    buf.append("&");
                }

            }
            buff = buf.toString();
            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            return null;
        }
        return buff;
    }

    public static String getMeetRequest(JSONObject jsonObject){
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time=System.currentTimeMillis();
        HashMap<String,String> param=new HashMap<>();
        param.put("biz_content",jsonObject.toString());
        param.put("version","1.0");
        param.put("timestamp",sdf1.format(time));
        param.put("charset","utf-8");
        param.put("verify_url","no_url");
        param.put("appid", MeetSDK.getInstance().APPID);
        param.put("developerid", MeetSDK.getInstance().DEVELOPERID);
        String sigin=NetHttp.getMD5FormatUrlNoSign(param);
        param.put("sign",sigin);
        return NetHttp.getFullFormatUrl(param);
    }

    // 完全退出app时，调用这个方法释放CPU。
    public void stop() {
        queue.stop();
    }

    public void cancle() {
        queue.cancelAll();
    }
}

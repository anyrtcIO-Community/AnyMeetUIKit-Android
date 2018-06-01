package org.anyrtc.meeting.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.anyrtc.lib_meeting.http.NetHttp;
import org.anyrtc.lib_meeting.http.ResultListener;
import org.anyrtc.meeting.Constans;
import org.anyrtc.meeting.utils.WechatMessageManager;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liuxiaozhong on 2018/5/31.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(this, Constans.WECHAT_ID,true);
        //将应用的appid注册到微信
        api.registerApp(Constans.WECHAT_ID);
        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            boolean result =  api.handleIntent(getIntent(), this);
            if(!result){
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result = "";
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String requestUrl="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+Constans.WECHAT_ID+"&secret="+Constans.WECHAT_KEY+"&code="+((SendAuth.Resp) baseResp).code+"&grant_type=authorization_code";
                StringRequest request=new StringRequest(requestUrl);
                NetHttp.getInstance().request(0, request, new ResultListener<String>() {
                    @Override
                    public void onSucceed(int what, Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.get());
                            String token=jsonObject.getString("access_token");
                            String openid=jsonObject.getString("openid");
                            getUserInfo(token,openid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(int what, Response<String> response) {

                    }
                });

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                Log.d("wechat", result);
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                Log.d("wechat", result);
                finish();
                break;
            default:
                result = "发送返回";
                Log.d("wechat", result);
                finish();
                break;
        }
    }


    public void getUserInfo(String access_token,String openid){
        String requestUrl= "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid;
        StringRequest request=new StringRequest(requestUrl);
        NetHttp.getInstance().request(0, request, new ResultListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                try {
                    JSONObject user=new JSONObject(response.get());
                    String id=user.getString("unionid");
                    String name=user.getString("nickname");
                    String icon=user.getString("headimgurl");
                    WechatMessageManager.getInstance().sendPersonInfo(id,name,icon);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        api.handleIntent(data,this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }
}

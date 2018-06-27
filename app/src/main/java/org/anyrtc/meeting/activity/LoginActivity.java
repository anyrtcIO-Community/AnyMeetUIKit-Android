package org.anyrtc.meeting.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.anyrtc.lib_meeting.meet.MeetSDK;
import org.anyrtc.meeting.Constans;
import org.anyrtc.meeting.R;
import org.anyrtc.meeting.bean.User;
import org.anyrtc.meeting.utils.SpUtil;
import org.anyrtc.meeting.utils.WechatMessageManager;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements WechatMessageManager.wechatLoginSuccess {


    @BindView(R.id.fl_login)
    FrameLayout flLogin;
    private IWXAPI api;
    private String webMeetId = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        neetChenJin = true;
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        WechatMessageManager.getInstance().setWechatLoginListener(this);

        if (SpUtil.getString(Constans.ANYRTC_OPENID) == null) {
            api = WXAPIFactory.createWXAPI(this, Constans.WECHAT_ID, true);
            api.registerApp(Constans.WECHAT_ID);
        } else {
            Intent intent = getIntent();
            if (intent.getData() != null && intent.getAction().equals(Intent.ACTION_VIEW)) {//处理从网页打开app
                webMeetId = intent.getData().toString();
                if (!TextUtils.isEmpty(webMeetId)) {
                    Intent i = new Intent(LoginActivity.this, JoinActivity.class);
                    if (webMeetId.contains("meetingplus://")) {
                        i.putExtra("meetid", webMeetId.split("//")[1]);
                    }
                    startActivity(i);
                    finishAnimActivity();
                }
            } else {
                startAnimActivity(MainActivity.class);
                finishAnimActivity();
            }

        }
    }

    @OnClick({R.id.fl_login,R.id.tv_web})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_login:
                if (api.isWXAppInstalled()) {
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";//
//                req.scope = "snsapi_login";//提示 scope参数错误，或者没有scope权限
                    req.state = "wechat_sdk_微信登录";
                    api.sendReq(req);
                } else {
                    Toast.makeText(this, "未安装微信", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_web:
                Intent intent = new Intent();
                intent.setData(Uri.parse("https://www.anyrtc.io/"));//Url 就是你要打开的网址
                intent.setAction(Intent.ACTION_VIEW);
                this.startActivity(intent); //启动浏览器
                break;

        }

    }

    public void initUser(String yourID, String yourName, String yourIcon) {
        MeetSDK.getInstance().initTeameeting(yourID, yourName, yourIcon, new MeetSDK.RequestResult() {
            @Override
            public void Result(String result) {
                User user = gson.fromJson(result, User.class);
                if (user.getCode() == 200) {
                    SpUtil.putString(Constans.ANYRTC_OPENID, user.getUserinfo().getU_anyrtc_openid());
                    SpUtil.putString(Constans.USERID, user.getUserinfo().getUserid());
                    SpUtil.putString(Constans.HEADURL, user.getUserinfo().getU_hd_icon());
                    SpUtil.putString(Constans.NICKNAME, user.getUserinfo().getU_nickname());
                    startAnimActivity(MainActivity.class);
                } else {
                    Toast.makeText(LoginActivity.this, user.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void Error(String message) {

            }
        });
    }

    @Override
    public void LoginSuccess(String unionid, String nickname, String headUrl) {
        initUser(unionid, nickname, headUrl);
    }
}

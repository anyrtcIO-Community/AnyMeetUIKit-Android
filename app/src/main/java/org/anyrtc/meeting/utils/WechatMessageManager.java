package org.anyrtc.meeting.utils;

/**
 * Created by liuxiaozhong on 2018/5/11.
 */
public class WechatMessageManager {
    private static WechatMessageManager instance;

    public static WechatMessageManager getInstance() {
        if (instance == null)
            synchronized (WechatMessageManager.class) {
                if (instance == null)
                    instance = new WechatMessageManager();
            }
        return instance;
    }

    private WechatMessageManager() {
    }

    private wechatLoginSuccess  wechatLoginSuccess;


    public void setWechatLoginListener(wechatLoginSuccess wechatLoginListener) {
        this.wechatLoginSuccess = wechatLoginListener;
    }


    public void sendPersonInfo(String unionid,String nickname,String headUrl) {
        wechatLoginSuccess.LoginSuccess(unionid,nickname,headUrl);
    }




    public interface wechatLoginSuccess {
        public void LoginSuccess(String unionid,String nickname,String headUrl);
    }

}

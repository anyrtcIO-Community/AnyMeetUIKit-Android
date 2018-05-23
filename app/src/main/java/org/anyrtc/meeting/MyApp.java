package org.anyrtc.meeting;

import org.anyrtc.lib_meeting.MeetApplication;
import org.anyrtc.lib_meeting.meet.MeetSDK;

/**
 * Created by liuxiaozhong on 2018/5/9.
 */

/**
 * 开发者需继承MeetApplication 并在super.onCreate(); 之前是设置自己的开发者信息
 */
public class MyApp extends MeetApplication {
    @Override
    public void onCreate() {
        MeetSDK.getInstance().setAnyRTCInfo(Constans.DEVELOPERID, Constans.APPID, Constans.APPKEY, Constans.APPTOKEN);
        super.onCreate();
    }
}

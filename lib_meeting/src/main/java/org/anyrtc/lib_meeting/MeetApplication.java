package org.anyrtc.lib_meeting;

import android.app.Application;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;

import org.anyrtc.lib_meeting.meet.MeetSDK;
import org.anyrtc.lib_meeting.utils.AlbumLoader;
import org.anyrtc.lib_meeting.utils.Utils;
import org.anyrtc.meet_kit.AnyRTCMeetEngine;
import org.anyrtc.whiteboard.http.WhiteboardConfig;

import java.util.Locale;

/**
 * Created by liuxiaozhong on 2018/5/9.
 */
public class MeetApplication extends Application {
    public final static String SHARE_URL="https://www.anyrtc.io/meetPlus/share/%s";
    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
        InitializationConfig  config = InitializationConfig.newBuilder(this)
                .connectionTimeout(15*1000)
                .readTimeout(15*1000)
                .retry(1).build();
        NoHttp.initialize(config);
        Logger.setDebug(true); // 开启NoHttp调试模式。
        Logger.setTag("HttpInfo"); // 设置NoHttp打印Log的TAG。

        WhiteboardConfig.getInstance().initAnyRTCInfo(MeetSDK.getInstance().DEVELOPERID, MeetSDK.getInstance().APPID, MeetSDK.getInstance().APPKEY, MeetSDK.getInstance().APPTOKEN);
        AnyRTCMeetEngine.Inst().initEngineWithAnyrtcInfo(getApplicationContext(), MeetSDK.getInstance().DEVELOPERID, MeetSDK.getInstance().APPID, MeetSDK.getInstance().APPKEY, MeetSDK.getInstance().APPTOKEN);
        AnyRTCMeetEngine.Inst().setFrontCameraMirrorEnable(true);
//        AnyRTCMeetEngine.Inst().configServerForPriCloud("teameeting.anyrtc.io",9060);
        AnyRTCMeetEngine.disableHWDecode();
        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(new AlbumLoader())
                .setLocale(Locale.getDefault())
                .build());
    }
}

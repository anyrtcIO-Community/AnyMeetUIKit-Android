package org.anyrtc.lib_meeting.meet;

import java.io.Serializable;

/**
 * Created by liuxiaozhong on 2018/5/10.
 */
public class MeetParams implements Serializable{

    public String meetId;
    public String anyRTCOpenId;
    public String hostId;
    public String meetTheme;
    public String meetPassword;
    public int meetMode;
    public int isLock;

    public MeetParams(String meetId, String anyRTCOpenId, String hostId, String meetTheme, String meetPassword, int meetMode,int isLock) {
        this.meetId = meetId;
        this.anyRTCOpenId = anyRTCOpenId;
        this.hostId = hostId;
        this.meetTheme = meetTheme;
        this.meetPassword = meetPassword;
        this.meetMode = meetMode;
        this.isLock=isLock;
    }

    public enum MeetMode{
        AnyRTCVideoQuality_Low1(0),
        AnyRTCVideoQuality_Low2(1),
        AnyRTCVideoQuality_Low3(2),
        AnyRTCVideoQuality_Medium1(3),
        AnyRTCVideoQuality_Medium2(4),
        AnyRTCVideoQuality_Medium3(5),
        AnyRTCVideoQuality_Height1(6),
        AnyRTCVideoQuality_Height2(7),
        AnyRTCVideoQuality_Height3(8);

        public final int level;

        private MeetMode(int level) {
            this.level = level;
        }
    }
}

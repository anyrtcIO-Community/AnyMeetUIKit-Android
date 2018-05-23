package org.anyrtc.lib_meeting.meet;

/**
 * Created by liuxiaozhong on 2018/5/10.
 */
public class MeetParams {

    public String anyRTCId;
    public String userId;
    public String hostId;
    public String headUrl;
    public String nickName;
    public String meetTheme;
    public String meetPassword;
    public int meetMode;


    /**
     *
     * @param anyRTCId  会议ID 保持唯一
     * @param userId  用户ID
     * @param hostId  主持会议的人的ID
     * @param headUrl  头像地址
     * @param nickName  用户昵称
     * @param meetTheme  会议主题
     * @param meetPassword  会议密码
     * @param meetMode  会议清晰度
     */
    public MeetParams(String anyRTCId, String userId, String hostId,String headUrl,String nickName,String  meetTheme,String meetPassword,int meetMode) {
        this.anyRTCId = anyRTCId;
        this.userId = userId;
        this.hostId=hostId;
        this.headUrl = headUrl;
        this.nickName=nickName;
        this.meetTheme=meetTheme;
        this.meetPassword=meetPassword;
        this.meetMode=meetMode;
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

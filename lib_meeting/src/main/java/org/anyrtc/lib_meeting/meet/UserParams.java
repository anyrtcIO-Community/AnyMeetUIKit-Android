package org.anyrtc.lib_meeting.meet;

import java.io.Serializable;

/**
 * Created by liuxiaozhong on 2018/5/31.
 */
public class UserParams implements Serializable{
    public String userId;
    public String headUrl;
    public String nickName;

    public UserParams(String userId, String headUrl, String nickName) {
        this.userId = userId;
        this.headUrl = headUrl;
        this.nickName = nickName;
    }
}

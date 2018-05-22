package org.anyrtc.lib_meeting.bean;

/**
 * Created by liuxiaozhong on 2017/12/21.
 */

public class ChatBean {
    public String content;
    public String icon;
    public boolean isSelf;
    public String name;

    public ChatBean( String content, String icon, boolean isSelf, String name) {
        this.content = content;
        this.icon=icon;
        this.isSelf=isSelf;
        this.name=name;
    }
}

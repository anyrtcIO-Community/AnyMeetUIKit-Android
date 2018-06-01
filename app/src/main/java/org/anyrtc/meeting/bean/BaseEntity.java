package org.anyrtc.meeting.bean;

/**
 * Created by liuxiaozhong on 2018/5/31.
 */
public class BaseEntity {
    public String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BaseEntity(String text) {
        this.text = text;
    }
}

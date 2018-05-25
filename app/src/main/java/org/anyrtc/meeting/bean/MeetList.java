package org.anyrtc.meeting.bean;

import java.util.List;

/**
 * Created by liuxiaozhong on 2018/5/25.
 */
public class MeetList {


    /**
     * requestid : 1527215811850
     * code : 200
     * meetinglist : [{"meetingid":"15907744","m_anyrtcid":"15907744","m_name":"Nick","m_userid":"594867993","m_type":1,"m_limit_type":0,"m_avcodec":0,"m_is_lock":0,"m_quality":0,"m_line_quality":1,"m_start_time":1527302223,"m_create_at":1527215807}]
     * total_number : 1
     * currenttime : 1527215811
     * message : get user meeting list success.
     */

    private long requestid;
    private int code;
    private int total_number;
    private int currenttime;
    private String message;
    private List<MeetinglistBean> meetinglist;

    public long getRequestid() {
        return requestid;
    }

    public void setRequestid(long requestid) {
        this.requestid = requestid;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public int getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(int currenttime) {
        this.currenttime = currenttime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MeetinglistBean> getMeetinglist() {
        return meetinglist;
    }

    public void setMeetinglist(List<MeetinglistBean> meetinglist) {
        this.meetinglist = meetinglist;
    }

    public static class MeetinglistBean {
        /**
         * meetingid : 15907744
         * m_anyrtcid : 15907744
         * m_name : Nick
         * m_userid : 594867993
         * m_type : 1
         * m_limit_type : 0
         * m_avcodec : 0
         * m_is_lock : 0
         * m_quality : 0
         * m_line_quality : 1
         * m_start_time : 1527302223
         * m_create_at : 1527215807
         */

        private String meetingid;
        private String m_anyrtcid;
        private String m_name;
        private String m_userid;
        private int m_type;
        private int m_limit_type;
        private int m_avcodec;
        private int m_is_lock;
        private int m_quality;
        private int m_line_quality;
        private int m_start_time;
        private int m_create_at;

        public String getMeetingid() {
            return meetingid;
        }

        public void setMeetingid(String meetingid) {
            this.meetingid = meetingid;
        }

        public String getM_anyrtcid() {
            return m_anyrtcid;
        }

        public void setM_anyrtcid(String m_anyrtcid) {
            this.m_anyrtcid = m_anyrtcid;
        }

        public String getM_name() {
            return m_name;
        }

        public void setM_name(String m_name) {
            this.m_name = m_name;
        }

        public String getM_userid() {
            return m_userid;
        }

        public void setM_userid(String m_userid) {
            this.m_userid = m_userid;
        }

        public int getM_type() {
            return m_type;
        }

        public void setM_type(int m_type) {
            this.m_type = m_type;
        }

        public int getM_limit_type() {
            return m_limit_type;
        }

        public void setM_limit_type(int m_limit_type) {
            this.m_limit_type = m_limit_type;
        }

        public int getM_avcodec() {
            return m_avcodec;
        }

        public void setM_avcodec(int m_avcodec) {
            this.m_avcodec = m_avcodec;
        }

        public int getM_is_lock() {
            return m_is_lock;
        }

        public void setM_is_lock(int m_is_lock) {
            this.m_is_lock = m_is_lock;
        }

        public int getM_quality() {
            return m_quality;
        }

        public void setM_quality(int m_quality) {
            this.m_quality = m_quality;
        }

        public int getM_line_quality() {
            return m_line_quality;
        }

        public void setM_line_quality(int m_line_quality) {
            this.m_line_quality = m_line_quality;
        }

        public int getM_start_time() {
            return m_start_time;
        }

        public void setM_start_time(int m_start_time) {
            this.m_start_time = m_start_time;
        }

        public int getM_create_at() {
            return m_create_at;
        }

        public void setM_create_at(int m_create_at) {
            this.m_create_at = m_create_at;
        }
    }
}

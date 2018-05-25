package org.anyrtc.meeting.bean;

/**
 * Created by liuxiaozhong on 2018/5/25.
 */
public class MeetInfo {


    /**
     * requestid : 1527214233382
     * code : 200
     * meetinginfo : {"meetingid":"50520360","m_anyrtcid":"50520360","m_name":"Sam","m_userid":"921711745","m_host_name":"Sam","m_type":1,"m_limit_type":0,"m_password":"","m_avcodec":0,"m_is_lock":0,"m_quality":0,"m_line_quality":1,"m_start_time":1527300595,"m_create_at":1527214176,"m_push_url":"rtmp://push.ws.anyrtc.io/live/anyrtc5FrikUFHlNal_50520360","m_pull_url":"rtmp://pull.ws.anyrtc.io/live/anyrtc5FrikUFHlNal_50520360","m_hls_url":"http://hls.ws.anyrtc.io/live/anyrtc5FrikUFHlNal_50520360/playlist.m3u8"}
     * message : get meeting information success.
     */

    private long requestid;
    private int code;
    private MeetinginfoBean meetinginfo;
    private String message;

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

    public MeetinginfoBean getMeetinginfo() {
        return meetinginfo;
    }

    public void setMeetinginfo(MeetinginfoBean meetinginfo) {
        this.meetinginfo = meetinginfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class MeetinginfoBean {
        /**
         * meetingid : 50520360
         * m_anyrtcid : 50520360
         * m_name : Sam
         * m_userid : 921711745
         * m_host_name : Sam
         * m_type : 1
         * m_limit_type : 0
         * m_password :
         * m_avcodec : 0
         * m_is_lock : 0
         * m_quality : 0
         * m_line_quality : 1
         * m_start_time : 1527300595
         * m_create_at : 1527214176
         * m_push_url : rtmp://push.ws.anyrtc.io/live/anyrtc5FrikUFHlNal_50520360
         * m_pull_url : rtmp://pull.ws.anyrtc.io/live/anyrtc5FrikUFHlNal_50520360
         * m_hls_url : http://hls.ws.anyrtc.io/live/anyrtc5FrikUFHlNal_50520360/playlist.m3u8
         */

        private String meetingid;
        private String m_anyrtcid;
        private String m_name;
        private String m_userid;
        private String m_host_name;
        private int m_type;
        private int m_limit_type;
        private String m_password;
        private int m_avcodec;
        private int m_is_lock;
        private int m_quality;
        private int m_line_quality;
        private int m_start_time;
        private int m_create_at;
        private String m_push_url;
        private String m_pull_url;
        private String m_hls_url;

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

        public String getM_host_name() {
            return m_host_name;
        }

        public void setM_host_name(String m_host_name) {
            this.m_host_name = m_host_name;
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

        public String getM_password() {
            return m_password;
        }

        public void setM_password(String m_password) {
            this.m_password = m_password;
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

        public String getM_push_url() {
            return m_push_url;
        }

        public void setM_push_url(String m_push_url) {
            this.m_push_url = m_push_url;
        }

        public String getM_pull_url() {
            return m_pull_url;
        }

        public void setM_pull_url(String m_pull_url) {
            this.m_pull_url = m_pull_url;
        }

        public String getM_hls_url() {
            return m_hls_url;
        }

        public void setM_hls_url(String m_hls_url) {
            this.m_hls_url = m_hls_url;
        }
    }
}

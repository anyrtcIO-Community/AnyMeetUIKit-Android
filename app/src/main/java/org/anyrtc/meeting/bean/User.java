package org.anyrtc.meeting.bean;

/**
 * Created by liuxiaozhong on 2018/5/24.
 */
public class User {


    /**
     * requestid : 1527155385211
     * code : 200
     * userinfo : {"userid":"231319128","u_country_code":"86","u_cellphone":"","u_email":"","u_icon":"","u_hd_icon":"","u_nickname":"Peter","u_auto_mute":0,"u_auto_camera_off":0,"u_room_notify":0,"u_create_at":1527155385,"u_line_meet_mode":1,"u_meet_mode":1,"u_type":1,"u_anyrtc_openid":"253944"}
     * message : init teameeting user success
     */

    private long requestid;
    private int code;
    private UserinfoBean userinfo;
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

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class UserinfoBean {
        /**
         * userid : 231319128
         * u_country_code : 86
         * u_cellphone :
         * u_email :
         * u_icon :
         * u_hd_icon :
         * u_nickname : Peter
         * u_auto_mute : 0
         * u_auto_camera_off : 0
         * u_room_notify : 0
         * u_create_at : 1527155385
         * u_line_meet_mode : 1
         * u_meet_mode : 1
         * u_type : 1
         * u_anyrtc_openid : 253944
         */

        private String userid;
        private String u_country_code;
        private String u_cellphone;
        private String u_email;
        private String u_icon;
        private String u_hd_icon;
        private String u_nickname;
        private int u_auto_mute;
        private int u_auto_camera_off;
        private int u_room_notify;
        private int u_create_at;
        private int u_line_meet_mode;
        private int u_meet_mode;
        private int u_type;
        private String u_anyrtc_openid;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getU_country_code() {
            return u_country_code;
        }

        public void setU_country_code(String u_country_code) {
            this.u_country_code = u_country_code;
        }

        public String getU_cellphone() {
            return u_cellphone;
        }

        public void setU_cellphone(String u_cellphone) {
            this.u_cellphone = u_cellphone;
        }

        public String getU_email() {
            return u_email;
        }

        public void setU_email(String u_email) {
            this.u_email = u_email;
        }

        public String getU_icon() {
            return u_icon;
        }

        public void setU_icon(String u_icon) {
            this.u_icon = u_icon;
        }

        public String getU_hd_icon() {
            return u_hd_icon;
        }

        public void setU_hd_icon(String u_hd_icon) {
            this.u_hd_icon = u_hd_icon;
        }

        public String getU_nickname() {
            return u_nickname;
        }

        public void setU_nickname(String u_nickname) {
            this.u_nickname = u_nickname;
        }

        public int getU_auto_mute() {
            return u_auto_mute;
        }

        public void setU_auto_mute(int u_auto_mute) {
            this.u_auto_mute = u_auto_mute;
        }

        public int getU_auto_camera_off() {
            return u_auto_camera_off;
        }

        public void setU_auto_camera_off(int u_auto_camera_off) {
            this.u_auto_camera_off = u_auto_camera_off;
        }

        public int getU_room_notify() {
            return u_room_notify;
        }

        public void setU_room_notify(int u_room_notify) {
            this.u_room_notify = u_room_notify;
        }

        public int getU_create_at() {
            return u_create_at;
        }

        public void setU_create_at(int u_create_at) {
            this.u_create_at = u_create_at;
        }

        public int getU_line_meet_mode() {
            return u_line_meet_mode;
        }

        public void setU_line_meet_mode(int u_line_meet_mode) {
            this.u_line_meet_mode = u_line_meet_mode;
        }

        public int getU_meet_mode() {
            return u_meet_mode;
        }

        public void setU_meet_mode(int u_meet_mode) {
            this.u_meet_mode = u_meet_mode;
        }

        public int getU_type() {
            return u_type;
        }

        public void setU_type(int u_type) {
            this.u_type = u_type;
        }

        public String getU_anyrtc_openid() {
            return u_anyrtc_openid;
        }

        public void setU_anyrtc_openid(String u_anyrtc_openid) {
            this.u_anyrtc_openid = u_anyrtc_openid;
        }
    }
}

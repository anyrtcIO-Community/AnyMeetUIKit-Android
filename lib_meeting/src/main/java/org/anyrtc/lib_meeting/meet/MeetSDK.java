package org.anyrtc.lib_meeting.meet;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.anyrtc.lib_meeting.activity.lib_MeetActivity;
import org.anyrtc.lib_meeting.bean.lib_Constans;
import org.anyrtc.lib_meeting.http.NetHttp;
import org.anyrtc.lib_meeting.http.ResultListener;
import org.anyrtc.lib_meeting.utils.StringUtils;
import org.anyrtc.meet_kit.AnyRTCMeetEngine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;

/**
 * Created by liuxiaozhong on 2018/5/9.
 */
public class MeetSDK {

    private static MeetSDK instance;

    public String DEVELOPERID = "";

    public String APPID = "";

    public String APPKEY = "";

    public String APPTOKEN = "";

    private lib_MeetActivity meetActivity;


    public static MeetSDK getInstance() {
        if (instance == null)
            synchronized (MeetSDK.class) {
                if (instance == null)
                    instance = new MeetSDK();
            }
        return instance;
    }

    private MeetSDK() {
    }

    public MeetSDK(lib_MeetActivity meetActivity) {
        this.meetActivity = meetActivity;
    }

    public interface RequestResult {
        void Result(String result);

        void Error(String message);
    }

    public void setAnyRTCInfo(@NonNull String DEVELOPERID, @NonNull String APPID, @NonNull String APPKEY, @NonNull String APPTOKEN) {
        if (StringUtils.isEmpty(DEVELOPERID) || StringUtils.isEmpty(APPID) || StringUtils.isEmpty(APPKEY) || StringUtils.isEmpty(APPTOKEN))
            throw new NullPointerException("初始化anyRTC参数不能为空");
        this.APPTOKEN = APPTOKEN;
        this.DEVELOPERID = DEVELOPERID;
        this.APPID = APPID;
        this.APPKEY = APPKEY;
    }


    /**
     * 注册Teameeting
     *
     * @param userId        用户第三方平台的userid 不能为空
     * @param nickName      用户第三方平台的昵称
     * @param headUrl       用户第三方平台的头像地址
     * @param requestResult
     */

    public void initTeameeting(@NonNull String userId, String nickName, String headUrl, @NonNull final RequestResult requestResult) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", userId);
            jsonObject.put("nickname", nickName);
            jsonObject.put("head_url", headUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startRequest("meeting/init_teameeting?", jsonObject, requestResult);
    }

    /**
     * /**
     * 创建会议
     *
     * @param userId        用户第三方平台的userid 不能为空
     * @param meetName      会议名称 不能为空
     * @param meetType      会议类型 1 交流  2 终端会议 不能为空
     * @param startTime     会议开始时间 格式 2018-08-08 12:12:12 （24小时制） 不能为空
     * @param password      会议密码 可选
     * @param limitType     会议的限制类型（0/1/2：开放/密码/指定人可以参加）    不能为空
     * @param memberList    会议的指定人员的用户id列表（字符串数组，[“userid0”,”userid1”…]) 可选
     * @param requestResult
     */

    public void creatMeetRoom(@NonNull String userId, @NonNull String meetName, @NonNull String meetType, @NonNull String startTime, String password, @NonNull String limitType, JSONArray memberList, @NonNull final RequestResult requestResult) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", userId);
            jsonObject.put("meet_name", meetName);
            jsonObject.put("meet_type", meetType);
            jsonObject.put("meet_start_time", startTime);
            jsonObject.put("meet_password", password);
            jsonObject.put("meet_limit_type", limitType);
            jsonObject.put("meet_member_list", memberList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startRequest("meeting/create_meeting_room?", jsonObject, requestResult);
    }

    /**
     * 删除会议
     *
     * @param userId        用户第三方平台的userid 必填
     * @param meetId        用户会议室id 必填
     * @param requestResult
     */
    public void deleteMeetRoom(@NonNull String userId, @NonNull String meetId, @NonNull final RequestResult requestResult) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", userId);
            jsonObject.put("meetingid", meetId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startRequest("meeting/delete_meeting_room?", jsonObject, requestResult);
    }

    /**
     * 获取会议信息
     *
     * @param meetId        用户会议室id 必填
     * @param requestResult
     */
    public void getMeetInfo(@NonNull String meetId, @NonNull final RequestResult requestResult) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("meetingid", meetId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startRequest("meeting/get_meeting_info?", jsonObject, requestResult);
    }

    /**
     * 获取会议列表
     *
     * @param userId        用户第三方平台的userid
     * @param pageNum       分页加载的页码（从1开始）
     * @param pageSize      每页多少条数据（必须大于0）
     * @param requestResult
     */
    public void getMeetList(@NonNull String userId, @NonNull String pageNum, @NonNull String pageSize, @NonNull final RequestResult requestResult) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", userId);
            jsonObject.put("page_num", pageNum);
            jsonObject.put("page_size", pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startRequest("meeting/get_user_meeting_list?", jsonObject, requestResult);
    }


    /**
     * 更新会议开始时间
     *
     * @param userId        用户第三方平台的userid
     * @param startTime     会议开始时间 （时间戳格式（2018-08-08 12:12:12））
     * @param meetingid     会议室id
     * @param requestResult
     */
    public void updataMeetStartTime(@NonNull String userId, @NonNull String startTime, @NonNull String meetingid, @NonNull final RequestResult requestResult) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", userId);
            jsonObject.put("meet_start_time", startTime);
            jsonObject.put("meetingid", meetingid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startRequest("meeting/update_meeting_start_time?", jsonObject, requestResult);
    }

    /**
     * 邀请或者添加参会人员
     *
     * @param userId           用户第三方平台的userid
     * @param meet_member_list 会议的指定人员的用户id列表，用户系统的userid，（字符串数组，[“userid0”,”userid1”…]）
     * @param meetId           会议室id
     * @param requestResult
     */
    public void inviteMeetMember(@NonNull String userId, @NonNull JSONArray meet_member_list, @NonNull String meetId, @NonNull final RequestResult requestResult) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", userId);
            jsonObject.put("meet_member_list", meet_member_list.toString());
            jsonObject.put("meetingid", meetId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startRequest("meeting/invite_meeting_member?", jsonObject, requestResult);
    }

    /**
     * 踢除或者删除参会人员
     *
     * @param userId           用户第三方平台的userid
     * @param meet_member_list 会议的指定人员的用户id列表，用户系统的userid，（字符串数组，[“userid0”,”userid1”…]）
     * @param meetId           会议室id
     * @param requestResult
     */
    public void deleteMeetMember(@NonNull String userId, @NonNull JSONArray meet_member_list, @NonNull String meetId, @NonNull final RequestResult requestResult) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", userId);
            jsonObject.put("meet_member_list", meet_member_list.toString());
            jsonObject.put("meetingid", meetId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startRequest("meeting/delete_meeting_member?", jsonObject, requestResult);
    }

    /**
     * 获取会议参会人员列表
     *
     * @param meetId 会议室id
     */
    public void getMeetMemberList(@NonNull String meetId, @NonNull final RequestResult requestResult) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("meetingid", meetId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startRequest("meeting/get_meeting_member_list?", jsonObject, requestResult);
    }


    private void startRequest(String url, JSONObject content, @NonNull final RequestResult requestResult) {
        final StringRequest request = new StringRequest(NetHttp.SERVER_URL + url + NetHttp.getMeetRequest(content));
        NetHttp.getInstance().request(0, request, new ResultListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                requestResult.Result(response.get());
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                requestResult.Error(response.get());
            }
        });
    }


    public void StartMeet(@NonNull Activity activity, @NonNull MeetParams params) {
        int audioPermisson = ActivityCompat.checkSelfPermission(AnyRTCMeetEngine.Inst().context(), RECORD_AUDIO);
        int videoPermission = ActivityCompat.checkSelfPermission(AnyRTCMeetEngine.Inst().context(), CAMERA);
        if (audioPermisson== PackageManager.PERMISSION_GRANTED&&videoPermission==PackageManager.PERMISSION_GRANTED){
            Intent i = new Intent(activity, lib_MeetActivity.class);
            i.putExtra(lib_Constans.ANYRTCID, params.anyRTCId);
            i.putExtra(lib_Constans.NAME, params.nickName);
            i.putExtra(lib_Constans.HEADURL, params.headUrl);
            i.putExtra(lib_Constans.USERID, params.userId);
            i.putExtra(lib_Constans.HOSTID, params.hostId);
            i.putExtra(lib_Constans.MEETMODE,params.meetMode);
            activity.startActivity(i);
        }else {
            Toast.makeText(activity,"请检查相机和录音权限是否开启",Toast.LENGTH_SHORT).show();
        }

    }






}

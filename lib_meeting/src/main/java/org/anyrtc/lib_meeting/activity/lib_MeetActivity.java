package org.anyrtc.lib_meeting.activity;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import org.anyrtc.common.enums.AnyRTCMeetMode;
import org.anyrtc.common.enums.AnyRTCVideoLayout;
import org.anyrtc.common.enums.AnyRTCVideoQualityMode;
import org.anyrtc.common.utils.AnyRTCAudioManager;
import org.anyrtc.lib_meeting.R;
import org.anyrtc.lib_meeting.bean.ChatBean;
import org.anyrtc.lib_meeting.bean.MemberBean;
import org.anyrtc.lib_meeting.bean.lib_Constans;
import org.anyrtc.lib_meeting.utils.AnimationUtil;
import org.anyrtc.lib_meeting.weight.CustomDialog;
import org.anyrtc.lib_meeting.weight.TempVideoView;
import org.anyrtc.meet_kit.AnyRTCMeetEngine;
import org.anyrtc.meet_kit.AnyRTCMeetOption;
import org.anyrtc.meet_kit.AnyRTCVideoMeetEvent;
import org.anyrtc.meet_kit.RTMeetKit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.VideoRenderer;

import java.util.ArrayList;
import java.util.List;

import it.sauronsoftware.base64.Base64;

public class lib_MeetActivity extends lib_BaseActivity implements View.OnClickListener, NotifyMessageManager.imageUploadFinishNotify {

    private ImageView ivSpeak, ivSwitch;
    private TextView tvExit, tvMeetId, tvAudio, tvVideo, tvDoc, tvPeople, tvMore;
    private RelativeLayout rl_video;
    private LinearLayout rlTopLayout;
    private LinearLayout llBottomLayout;
    private CustomDialog moreFuturesDialog;
    private FrameLayout flContent, fl_white_board;

    private libMemberFragment libMemberFragment;
    private libMessageFragment libMessageFragment;
    private libWhiteBoardFragment libWhiteBoardFragment;

    private int LayoutHiddenTime = 0;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private static RTMeetKit mMeetKit;
    private TempVideoView mVideoView;

    public static String userId = "", anyRTCId = "", nickName = "", headUrl = "", hostId = "", meetTitle = "", meetPassword = "";
    public int meetMode=0;
    public String boardFileId = "";//上传文档ID

    private AnyRTCAudioManager anyRTCAudioManager;

    private String shareScreenInfo = "";
    private boolean isHadSomeoneShare = false;
    private int shareType = -1;
    private boolean isShowScreen = false;
    private boolean isSelfShare = false;
    private List<String> imageList = new ArrayList<>();
    private List<String> memberPeerIdList = new ArrayList<>();
    private List<MemberBean> memBerList = new ArrayList<>();


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            LayoutHiddenTime++;
            mHandler.postDelayed(this, 1000);
            if (LayoutHiddenTime == 10) {
                toggleCtrlLayout();
                LayoutHiddenTime = 0;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_lib_meet;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        rl_video = findViewById(R.id.rl_video);
        ivSpeak = findViewById(R.id.iv_speak_lib);
        ivSwitch = findViewById(R.id.iv_switch_camera_lib);
        tvExit = findViewById(R.id.tv_meet_exit_lib);
        tvMeetId = findViewById(R.id.tv_meet_id_lib);
        tvAudio = findViewById(R.id.tv_audio_lib);
        tvVideo = findViewById(R.id.tv_video_lib);
        tvDoc = findViewById(R.id.tv_doc_share_lib);
        tvPeople = findViewById(R.id.tv_people_lib);
        tvMore = findViewById(R.id.tv_more_lib);
        rlTopLayout = findViewById(R.id.rl_top_futures_lib);
        llBottomLayout = findViewById(R.id.ll_bottom_futures_lib);
        flContent = findViewById(R.id.fl_content);
        fl_white_board = findViewById(R.id.fl_white_board);
        libMemberFragment = new libMemberFragment();
        libMessageFragment = new libMessageFragment();
        libWhiteBoardFragment = new libWhiteBoardFragment();
        rl_video.setOnClickListener(this);
        if (mHandler != null) {
            mHandler.postDelayed(runnable, 1000);
        }
        userId = getIntent().getStringExtra(lib_Constans.USERID);
        nickName = getIntent().getStringExtra(lib_Constans.NAME);
        headUrl = getIntent().getStringExtra(lib_Constans.HEADURL);
        anyRTCId = getIntent().getStringExtra(lib_Constans.ANYRTCID);
        hostId = getIntent().getStringExtra(lib_Constans.HOSTID);
        meetTitle = getIntent().getStringExtra(lib_Constans.MEETTHEME);
        meetPassword = getIntent().getStringExtra(lib_Constans.MEETPASSWORD);
        meetMode=getIntent().getIntExtra(lib_Constans.MEETMODE,0);
        tvMeetId.setText(anyRTCId.length() >= 8 ? anyRTCId.substring(0, 4) + " " + anyRTCId.substring(4) : anyRTCId);

        anyRTCAudioManager = AnyRTCAudioManager.create(this, new Runnable() {
            @Override
            public void run() {
                if (anyRTCAudioManager.getSelectedAudioDevice().equals(AnyRTCAudioManager.AudioDevice.SPEAKER_PHONE)){
                    if (ivSpeak!=null) {
                        ivSpeak.setSelected(false);
                    }
                }else {
                    if (ivSpeak!=null) {
                        ivSpeak.setSelected(true);
                    }
                }
            }
        });

        anyRTCAudioManager.init();
        NotifyMessageManager.getInstance().setImageUploadFinish(this);

        initSDK();

    }

    private void initSDK() {
        //获取配置类
        AnyRTCMeetOption anyRTCMeetOption = AnyRTCMeetEngine.Inst().getAnyRTCMeetOption();
        //设置默认为前置摄像头
        anyRTCMeetOption.setFrontCamera(true);
        anyRTCMeetOption.setVideoLayout(AnyRTCVideoLayout.AnyRTC_V_1X3);
        switch (meetMode){
            case 0:
                anyRTCMeetOption.setVideoMode(AnyRTCVideoQualityMode.AnyRTCVideoQuality_Low1);
                break;
            case 1:
                anyRTCMeetOption.setVideoMode(AnyRTCVideoQualityMode.AnyRTCVideoQuality_Low2);
                break;
            case 2:
                anyRTCMeetOption.setVideoMode(AnyRTCVideoQualityMode.AnyRTCVideoQuality_Low3);
                break;
            case 3:
                anyRTCMeetOption.setVideoMode(AnyRTCVideoQualityMode.AnyRTCVideoQuality_Medium1);
                break;
            case 4:
                anyRTCMeetOption.setVideoMode(AnyRTCVideoQualityMode.AnyRTCVideoQuality_Medium2);
                break;
            case 5:
                anyRTCMeetOption.setVideoMode(AnyRTCVideoQualityMode.AnyRTCVideoQuality_Medium3);
                break;
            case 6:
                anyRTCMeetOption.setVideoMode(AnyRTCVideoQualityMode.AnyRTCVideoQuality_Height1);
                break;
            case 7:
                anyRTCMeetOption.setVideoMode(AnyRTCVideoQualityMode.AnyRTCVideoQuality_Height2);
                break;
            case 8:
                anyRTCMeetOption.setVideoMode(AnyRTCVideoQualityMode.AnyRTCVideoQuality_Height3);
                break;
        }

        mMeetKit = new RTMeetKit(VideoMeetEvent, anyRTCMeetOption);
        //实例化视频窗口管理对象
        mVideoView = new TempVideoView(rl_video, this, AnyRTCMeetEngine.Inst().Egl(), true);
        //设置可以点击切换 （这个开发者可自行修改RTCVideoView类）
        mVideoView.setVideoSwitchEnable(true);
        //获取视频渲染对象
        VideoRenderer render = mVideoView.OnRtcOpenLocalRender();
        mMeetKit.setMeetMode(AnyRTCMeetMode.AnyRTC_Meet_Normal);
        //设置本地视频采集
        mMeetKit.setLocalVideoCapturer(render.GetRenderPointer());
        //加入RTC服务
        mMeetKit.joinRTC(anyRTCId, userId, getUserData());
    }

    public String getUserData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MaxJoiner", 6);
            jsonObject.put("userId", userId);
            jsonObject.put("nickName", nickName);
            jsonObject.put("devType", 0);
            jsonObject.put("headUrl", headUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private AnyRTCVideoMeetEvent VideoMeetEvent = new AnyRTCVideoMeetEvent() {

        /**
         * 加入RTC服务成功（入会成功）
         * @param strAnyRTCId 会议ID
         */
        @Override
        public void onRTCJoinMeetOK(final String strAnyRTCId) {
            lib_MeetActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("callback", "onRTCJoinMeetOK strAnyRTCId=" + strAnyRTCId);
                    MemberBean memberBean = new MemberBean();
                    memberBean.setName(nickName);
                    memberBean.setdType(0);
                    memberBean.setHost(userId.equals(hostId));
                    memberBean.setHostId(hostId);
                    memberBean.setIcon(headUrl);
                    memberBean.setId(userId);
                    memberBean.setSelf(true);
                    memBerList.add(memberBean);
                    libMemberFragment.dataNotify();
                }
            });
        }

        /**
         * 加入RTC服务失败 （入会失败）
         * @param strAnyRTCId 会议ID
         * @param nCode 状态码
         */
        @Override
        public void onRTCJoinMeetFailed(final String strAnyRTCId, final int nCode) {
            lib_MeetActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("callback", "onRTCJoinMeetFailed strAnyRTCId=" + strAnyRTCId + "Code=" + nCode);
                    if (nCode == 701) {
                    } else {
                    }
                    finishAnimActivity();

                }
            });
        }

        /**
         * 离开会议
         * @param nCode 状态码
         */
        @Override
        public void onRTCLeaveMeet(final int nCode) {
            lib_MeetActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("callback", "onRTCLeaveMeet Code=" + nCode);
                }
            });
        }

        /**
         * 其他人视频即将显示  比如你在会议中有人进来了 则会回调该方法 再次设置其他人视频窗口即可
         * @param strRTCPeerId RTC服务生成的用来标识该用户的ID
         * @param strUserId 用户ID
         * @param strUserData 用户自定义数据
         */
        @Override
        public void onRTCOpenVideoRender(final String strRTCPeerId, final String strPublishId, final String strUserId, final String strUserData) {
            lib_MeetActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("callback", "onRTCOpenVideoRender strRTCPeerId= " + strRTCPeerId + "strPublishId=" + strPublishId + "strUserId=" + strUserId + "strUserData=" + strUserData);
                    if (!strPublishId.equals(shareScreenInfo)) {
                        final VideoRenderer render = mVideoView.OnRtcOpenRemoteRender(strRTCPeerId, "");
                        if (null != render) {
                            mMeetKit.setRTCVideoRender(strPublishId, render.GetRenderPointer());
                        }
                        String userName = "";
                        String icon = "";
                        int dType = 0;
                        try {
                            JSONObject jsonObject = new JSONObject(strUserData);
                            userName = jsonObject.getString("nickName");
                            icon = jsonObject.getString("headUrl");
                            dType = jsonObject.getInt("devType");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            userName = "游客";
                        }
                        if (memberPeerIdList.contains(strRTCPeerId)) {
                            for (MemberBean memberBean : memBerList) {
                                if (memberBean.getPeerId().equals(strRTCPeerId)) {
                                    memberBean.setPeerId(strRTCPeerId);
                                    memberBean.setId(strUserId);
                                    memberBean.setHostId(hostId);
                                    memberBean.setdType(0);
                                    memberBean.setHost(userId.equals(hostId));
                                    memberBean.setName(userName);
                                    memberBean.setIcon(icon);
                                    memberBean.setSelf(false);
                                }
                            }
                        } else {
                            MemberBean memberBean = new MemberBean();
                            memberBean.setPeerId(strRTCPeerId);
                            memberBean.setId(strUserId);
                            memberBean.setHostId(hostId);
                            memberBean.setHost(userId.equals(hostId));
                            memberBean.setName(userName);
                            memberBean.setdType(dType);
                            memberBean.setIcon(icon);
                            memberBean.setSelf(false);
                            memBerList.add(memberBean);
                            memberPeerIdList.add(strRTCPeerId);
                        }
                        libMemberFragment.dataNotify();
                    }
                }

            });

        }

        /**
         * 其他人视频关闭  比如你在会议中有人离开了 则会回调该方法 将其他人视频窗口移除即可
         * @param strRTCPeerId RTC服务生成的用来标识该用户的ID
         * @param strUserId 用户ID
         */
        @Override
        public void onRTCCloseVideoRender(final String strRTCPeerId, final String strPublishId, final String strUserId) {
            lib_MeetActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("callback", "onRTCCloseVideoRender strPublishId=" + strPublishId + "strUserId=" + strUserId);
                    if (strPublishId.equals(shareScreenInfo)) {
                        return;
                    }
                    if (mMeetKit != null && mVideoView != null) {
                        mVideoView.OnRtcRemoveRemoteRender(strRTCPeerId);
                        mMeetKit.setRTCVideoRender(strPublishId, 0);
                    }
                    if (memberPeerIdList.contains(strRTCPeerId)) {
                        memberPeerIdList.remove(strRTCPeerId);
                    }
                    int index = -1;
                    for (int i = 0; i < memBerList.size(); i++) {
                        if (memBerList.get(i).getPeerId().equals(strRTCPeerId)) {
                            index = i;
                        }
                    }
                    if (index != -1) {
                        memBerList.remove(index);
                    }
                    libMemberFragment.dataNotify();

                }
            });
        }

        /**
         * 其他人对音视频操作的监听 其他人打开关闭音视频都会走该方法
         * @param strRTCPeerId RTC服务生成的用来标识该用户的ID
         * @param bAudio true 打开了音频 false 关闭了
         * @param bVideo true 打开了视频 false 关闭了
         */
        @Override
        public void onRTCAVStatus(final String strRTCPeerId, final boolean bAudio, final boolean bVideo) {
            lib_MeetActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("callback", "onRTCAVStatus strRTCPeerId=" + strRTCPeerId + "bAudio=" + bAudio + "bVideo=" + bVideo);
                    if (!memberPeerIdList.contains(strRTCPeerId)) {
                        MemberBean memberBean = new MemberBean();
                        memberBean.setPeerId(strRTCPeerId);
                        memberBean.setHost(userId.equals(hostId));
                        memberBean.setSelf(false);
                        memberBean.setOpenAudio(bAudio);
                        memberBean.setOpenVideo(bVideo);
                        memberPeerIdList.add(strRTCPeerId);
                        memBerList.add(memberBean);
                    } else {
                        for (int i = 0; i < memBerList.size(); i++) {
                            if (memBerList.get(i).getPeerId().equals(strRTCPeerId)) {
                                memBerList.get(i).setOpenAudio(bAudio);
                                memBerList.get(i).setOpenVideo(bVideo);
                                memBerList.get(i).setSelf(false);
                                memBerList.get(i).setHost(userId.equals(hostId));
                            }
                        }
                    }
                    libMemberFragment.dataNotify();
                }
            });
        }

        @Override
        public void onRTCAudioActive(final String strRTCPeerId, final String strUserId, final int nTime) {
            lib_MeetActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("callback", "onRTCAudioActive strRTCPeerId=" + strRTCPeerId + "strUserId=" + strUserId + "nTime" + nTime);
                }
            });
        }

        /**
         * 声音检测
         * @param strRTCPeerId RTC服务生成的用来标识该用户的ID
         * @param nTime 360毫秒
         */

        /**
         * 收到消息
         * @param strCustomID 用户ID
         * @param strCustomName 用户昵称
         * @param strCustomHeader 用户头像
         * @param strMessage 消息内容
         */
        @Override
        public void onRTCUserMessage(final String strCustomID, final String strCustomName, final String strCustomHeader, final String strMessage) {
            lib_MeetActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("callback", "onRTCUserMessage strCustomID=" + strCustomID + "strCustomName:" + strCustomName + "strCustomHeader:" + strCustomHeader + "strMessage:" + strMessage);

                    if (!TextUtils.isEmpty(strMessage)) {
                        try {
                            JSONObject jsonObject = new JSONObject(strMessage);
                            int type = jsonObject.getInt("mType");

                            switch (type) {
                                case 0:
                                    String message = jsonObject.getString("mContent");
                                    if (libMemberFragment.isAdded()) {
                                        libMessageFragment.addMessage(new ChatBean(Base64.decode(message), strCustomHeader, false, strCustomName));
                                    }
                                    break;
                                case 1:
                                    int isvidio = jsonObject.getInt("type");
                                    int state = jsonObject.getInt("state");
                                    String userid = jsonObject.getString("userid");
                                    if (!userId.equals(userid)) {
                                        return;
                                    }
                                    if (isvidio == 0) {//音频
                                        int audioState = tvAudio.isSelected() ? 0 : 1;//如果现在是关闭就是0  打开就是1
                                        if (state != audioState) {//如果发过来的和现在的不一样的状态  那就设置
                                            if (state == 0) {//关闭
                                                mMeetKit.setAudioEnable(false);
                                                tvAudio.setSelected(true);
                                            } else {//打开
                                                mMeetKit.setAudioEnable(true);
                                                tvAudio.setSelected(false);
                                            }
                                        }
                                        for (int i = 0; i < memBerList.size(); i++) {
                                            if (memBerList.get(i).isSelf()) {
                                                memBerList.get(i).setOpenAudio(!tvAudio.isSelected());
                                            }
                                        }
                                        libMemberFragment.dataNotify();
                                    } else {//视频
                                        int videoState = tvVideo.isSelected() ? 0 : 1;//如果现在是关闭就是0  打开就是1
                                        if (state != videoState) {//如果发过来的和现在的不一样的状态  那就设置
                                            if (state == 0) {//关闭
                                                mMeetKit.setVideoEnable(false);
                                                tvVideo.setSelected(true);
                                            } else {//打开
                                                mMeetKit.setVideoEnable(true);
                                                tvVideo.setSelected(false);
                                            }
                                        }
                                        for (int i = 0; i < memBerList.size(); i++) {
                                            if (memBerList.get(i).isSelf()) {
                                                memBerList.get(i).setOpenVideo(!tvVideo.isSelected());
                                            }
                                        }
                                        libMemberFragment.dataNotify();
                                    }
                                    break;
                                case 2:
                                    String id = jsonObject.getString("userid");
                                    if (userId.equals(id)) {
                                        Toast.makeText(lib_MeetActivity.this, R.string.u_kicked_out_room, Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    break;
                                case 4:

                                    break;
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
            });
        }

        @Override
        public void onRTCSetUserShareEnableResult(final boolean bSuccess) {
            lib_MeetActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("callback", "onRTCSetWhiteBoardEnableResult bSuccess=" + bSuccess);
                    if (bSuccess) {
                        isHadSomeoneShare = true;
                        isSelfShare = true;
                        shareType = 1;
                        tvExit.setText(R.string.close_board);
                        libWhiteBoardFragment.setImageList(imageList, true, boardFileId);
                        addFragment(R.id.fl_white_board, libWhiteBoardFragment, false, new Fragment[0]);
                        Drawable drawable = getResources().getDrawable(R.drawable.iv_switch_screen);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tvDoc.setCompoundDrawables(null, drawable, null, null);
                        tvDoc.setText(R.string.switch_screen);
                    } else {

                    }
                }
            });
        }

        @Override
        public void onRTCUserShareOpen(final int type, final String strWBInfo, final String strCustomID, final String strUserData) {
            lib_MeetActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isHadSomeoneShare = true;

                    shareType = type;
                    Log.d("callback", "onRTCWhiteBoardOpen strWBInfo=" + strWBInfo + "strCustomID:" + strCustomID + "strUserData:" + strUserData);
                    Drawable drawable = getResources().getDrawable(R.drawable.iv_switch_screen);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tvDoc.setCompoundDrawables(null, drawable, null, null);
                    tvDoc.setText(R.string.switch_screen);
                    if (type == 1) {//文档
                        isSelfShare = false;
                        try {
                            JSONObject jsonObject=new JSONObject(strWBInfo);
                            JSONArray image=jsonObject.getJSONArray("picArray");
                            String fileId=jsonObject.getString("fileid");
                            for (int i=0;i<image.length();i++){
                                imageList.add(image.get(i).toString());
                            }
                            libWhiteBoardFragment.setImageList(imageList, false, fileId);
                            addFragment(R.id.fl_white_board, libWhiteBoardFragment, false, new Fragment[0]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {//屏幕共享
                        shareScreenInfo = strWBInfo;
                    }

                }
            });
        }

        @Override
        public void onRTCUserShareClose() {
            lib_MeetActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("callback", "onRTCWhiteBoardClose ");
                    isHadSomeoneShare = false;
                    isSelfShare = false;
                    Drawable drawable = getResources().getDrawable(R.drawable.iv_share_lib);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tvDoc.setCompoundDrawables(null, drawable, null, null);
                    tvDoc.setText(R.string.doc_share);
                    tvExit.setText(R.string.close_meet);
                    if (shareType == 1) {
                        libWhiteBoardFragment.close();
                        imageList.clear();
                        addFragment(R.id.fl_white_board, null, false, libWhiteBoardFragment);
                    } else {
                        if (mVideoView != null) {
                            mVideoView.setPeopleShow(View.VISIBLE, shareScreenInfo);
                            mVideoView.removeScreenShare(shareScreenInfo);
                        }
                    }
                }
            });
        }

        @Override
        public void onRTCHosterOnline(String strRTCPeerId, String strUserId, String strUserData) {

        }

        @Override
        public void onRTCHosterOffline(String strRTCPeerId) {

        }

        @Override
        public void onRTCTalkOnlyOn(String strRTCPeerId, String strUserId, String strUserData) {

        }

        @Override
        public void onRTCTalkOnlyOff(String strRTCPeerId) {

        }

    };


    public void onBtnClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_speak_lib) {
            if (ivSpeak.isSelected()){
                if (anyRTCAudioManager!=null) {
                    anyRTCAudioManager.setAudioDevice(AnyRTCAudioManager.AudioDevice.SPEAKER_PHONE);
                }
                ivSpeak.setSelected(false);
            }else {
                if (anyRTCAudioManager!=null) {
                    anyRTCAudioManager.setAudioDevice(AnyRTCAudioManager.AudioDevice.EARPIECE);
                }
                ivSpeak.setSelected(true);
            }
        } else if (id == R.id.iv_switch_camera_lib) {
            if (mMeetKit != null) {
                mMeetKit.switchCamera();
            }
        } else if (id == R.id.tv_meet_exit_lib) {
            if (isHadSomeoneShare && shareType == 1 && isSelfShare) {
                libWhiteBoardFragment.close();
                mMeetKit.setUserShareEnable(1, false);
                addFragment(R.id.fl_white_board, null, false, libWhiteBoardFragment);
            } else {
                finishAnimActivity();
            }
        } else if (id == R.id.tv_audio_lib) {
            if (tvAudio.isSelected()) {
                tvAudio.setSelected(false);
                tvAudio.setText(R.string.open_audio);
                if (mMeetKit != null) {
                    mMeetKit.setAudioEnable(true);
                }
            } else {
                tvAudio.setSelected(true);
                tvAudio.setText(R.string.close_audio);
                if (mMeetKit != null) {
                    mMeetKit.setAudioEnable(false);
                }
            }
            for (int i = 0; i < memBerList.size(); i++) {
                if (memBerList.get(i).isSelf()) {
                    memBerList.get(i).setOpenAudio(!tvAudio.isSelected());
                }
            }
            libMemberFragment.dataNotify();

        } else if (id == R.id.tv_video_lib) {
            if (tvVideo.isSelected()) {
                tvVideo.setSelected(false);
                tvVideo.setText(R.string.open_video);
                if (mMeetKit != null) {
                    mMeetKit.setVideoEnable(true);
                }
            } else {
                tvVideo.setSelected(true);
                tvVideo.setText(R.string.close_video);
                if (mMeetKit != null) {
                    mMeetKit.setVideoEnable(false);
                }
            }
            for (int i = 0; i < memBerList.size(); i++) {
                if (memBerList.get(i).isSelf()) {
                    memBerList.get(i).setOpenVideo(!tvVideo.isSelected());
                }
            }
            libMemberFragment.dataNotify();
        } else if (id == R.id.tv_doc_share_lib) {
            if (isHadSomeoneShare) {
                if (shareType == 1) {//
                    if (libWhiteBoardFragment.isVisible()) {
                        addFragment(R.id.fl_white_board, null, false, libWhiteBoardFragment);
                    } else {
                        addFragment(R.id.fl_white_board, libWhiteBoardFragment, false, new Fragment[0]);
                    }
                } else {//屏幕共享
                    final VideoRenderer render = mVideoView.openScreenShare(shareScreenInfo);
                    if (null != render) {
                        mMeetKit.setRTCVideoRender(shareScreenInfo, render.GetRenderPointer());
                    }
                    if (isShowScreen) {
                        mVideoView.setPeopleShow(View.VISIBLE, shareScreenInfo);
                    } else {
                        mVideoView.setPeopleShow(View.GONE, shareScreenInfo);
                    }
                    isShowScreen = !isShowScreen;
                }
            } else {
                Album.image(this) // Image selection.
                        .multipleChoice()
                        .camera(false)
                        .columnCount(3)
                        .selectCount(9)
                        .onResult(new Action<ArrayList<AlbumFile>>() {
                            @Override
                            public void onAction(@NonNull ArrayList<AlbumFile> result) {
                                NotifyMessageManager.getInstance().sendImagePathMessage(new ArrayList<String>());
                            }
                        })
                        .onCancel(new Action<String>() {
                            @Override
                            public void onAction(@NonNull String result) {

                            }
                        })
                        .start();


            }
        } else if (id == R.id.tv_people_lib) {
            addFragment(R.id.fl_content, libMemberFragment, false, libMessageFragment, libWhiteBoardFragment);
        } else if (id == R.id.tv_more_lib) {
            ShowMore();
        }
    }

    private void ShowMore() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setContentView(R.layout.layout_more_futures_lib)
                .setAnimId(R.style.AnimBottom)
                .setGravity(Gravity.BOTTOM)
                .setLayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                .setBackgroundDrawable(true)
                .build();
        moreFuturesDialog = builder.show(new CustomDialog.Builder.onInitListener() {
            @Override
            public void init(CustomDialog view) {
                Switch lock = view.findViewById(R.id.switch_lock);
                Switch play = view.findViewById(R.id.switch_play_music);
                lock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });
    }

    public void onClickMessage() {
        addFragment(R.id.fl_content, libMessageFragment, false, libMemberFragment, libWhiteBoardFragment);
    }

    public List<MemberBean> getMemBerList() {
        return memBerList;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (libMessageFragment != null && libMessageFragment.isVisible()) {
                addFragment(R.id.fl_content, null, false, libMessageFragment);
                return false;
            }
            if (libMemberFragment != null && libMemberFragment.isVisible()) {
                addFragment(R.id.fl_content, null, false, libMemberFragment);
                return false;
            }
            if (libWhiteBoardFragment != null && libWhiteBoardFragment.isVisible()) {
                addFragment(R.id.fl_white_board, null, false, libWhiteBoardFragment);
                return false;
            }
            finishAnimActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mVideoView.updateVideoView();
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        }
    }

    protected void toggleCtrlLayout() {
        if (rlTopLayout != null && llBottomLayout != null) {
            int isVisit = rlTopLayout.getVisibility();
            if (isVisit == View.VISIBLE) {
                rlTopLayout.setVisibility(View.GONE);
                llBottomLayout.setVisibility(View.GONE);
                rlTopLayout.setAnimation(AnimationUtil.TopmoveToViewBottom());
                llBottomLayout.setAnimation(AnimationUtil.moveToViewBottom());
                if (mHandler != null) {
                    mHandler.removeCallbacks(runnable);
                    LayoutHiddenTime = 0;
                }
            } else {
                rlTopLayout.setVisibility(View.VISIBLE);
                llBottomLayout.setVisibility(View.VISIBLE);
                rlTopLayout.setAnimation(AnimationUtil.TopmoveToViewLocation());
                llBottomLayout.setAnimation(AnimationUtil.moveToViewLocation());
                if (mHandler != null) {
                    mHandler.removeCallbacks(runnable);
                    mHandler.postDelayed(runnable, 1000);
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_video) {
            toggleCtrlLayout();
        }
    }

    public void setVideoLayoutEnable(boolean canTouch){
        int visibale=rlTopLayout.getVisibility();
            if (canTouch){
                rl_video.setClickable(true);
                rlTopLayout.setVisibility(View.VISIBLE);
                llBottomLayout.setVisibility(View.VISIBLE);
                rlTopLayout.setAnimation(AnimationUtil.TopmoveToViewLocation());
                llBottomLayout.setAnimation(AnimationUtil.moveToViewLocation());
                if (mHandler != null) {
                    mHandler.removeCallbacks(runnable);
                    mHandler.postDelayed(runnable, 1000);
                }
            }else {
                rl_video.setClickable(false);
                if (visibale==View.GONE){
                    return;
                }
                rlTopLayout.setVisibility(View.GONE);
                llBottomLayout.setVisibility(View.GONE);
                rlTopLayout.setAnimation(AnimationUtil.TopmoveToViewBottom());
                llBottomLayout.setAnimation(AnimationUtil.moveToViewBottom());
                if (mHandler != null) {
                    mHandler.removeCallbacks(runnable);
                    LayoutHiddenTime = 0;
                }
            }
    }

    public boolean isHost() {
        return userId.equals(hostId);
    }

    public RTMeetKit getMeetKit() {
        return mMeetKit;
    }

    public void openWhiteBoard(List<String> docImageList, String fileId) {
        this.imageList = docImageList;
        this.boardFileId = fileId;
        JSONObject jsonObject = new JSONObject();
        JSONArray pic = new JSONArray();
        try {
            jsonObject.put("fileid", fileId);
            jsonObject.put("meetid", anyRTCId);
            if (docImageList.size() > 0) {
                for (int i = 0; i < docImageList.size(); i++) {
                    pic.put(docImageList.get(i));
                }
            }
            jsonObject.put("picArray", pic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mMeetKit.setUserShareEnable(1, true);
        mMeetKit.setUserShareInfo(jsonObject.toString());


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMeetKit != null) {
            mMeetKit.clear();
            mMeetKit = null;
        }
        if (anyRTCAudioManager!=null){
            anyRTCAudioManager.close();
            anyRTCAudioManager=null;
        }
    }

    @Override
    public void imageUrlList(List<String> imagePath, String fileId) {
        openWhiteBoard(imagePath, fileId);
    }
}

package org.anyrtc.meeting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import org.anyrtc.lib_meeting.activity.NotifyMessageManager;
import org.anyrtc.lib_meeting.meet.MeetParams;
import org.anyrtc.lib_meeting.meet.MeetSDK;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeetingActivity extends AppCompatActivity implements NotifyMessageManager.imageChoosedNotify {
    @BindView(R.id.et_anyRTC)
    EditText etAnyRTC;
    @BindView(R.id.et_userid)
    EditText etUserid;
    @BindView(R.id.et_theme)
    EditText etTheme;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.start)
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        ButterKnife.bind(this);
        NotifyMessageManager.getInstance().setImageChoosedNotify(this);
        etAnyRTC.setText((int)((Math.random()*9+1)*10000000)+"");
        etUserid.setText((int)((Math.random()*9+1)*100000)+"");

        etTheme.setText("默认主题");
        etNickname.setText("测试1号");

    }


    /**
     * 该方法为进入图片选择后 选择的图片地址回调  开发者再此将图片上传到图片服务器 再将通知sdk  sendImageUploadFinishMessage
     * @param imagePath
     */
    @Override
    public void imageList(List<String> imagePath) {
        List<String> list = new ArrayList<>();
        list.add("http://img1.imgtn.bdimg.com/it/u=3378476916,3056761097&fm=214&gp=0.jpg");
        list.add("http://img1.imgtn.bdimg.com/it/u=3378476916,3056761097&fm=214&gp=0.jpg");
        list.add("http://img1.imgtn.bdimg.com/it/u=3378476916,3056761097&fm=214&gp=0.jpg");
        NotifyMessageManager.getInstance().sendImageUploadFinishMessage(list, "1000000");
    }


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
    @OnClick(R.id.start)
    public void onViewClicked() {
        MeetSDK.getInstance().StartMeet(MeetingActivity.this, new MeetParams(etAnyRTC.getText().toString(), etUserid.getText().toString(), "385520219", "", etNickname.getText().toString(), etTheme.getText().toString(), etPassword.getText().toString(),MeetParams.MeetMode.AnyRTCVideoQuality_Height1.level));
    }
}

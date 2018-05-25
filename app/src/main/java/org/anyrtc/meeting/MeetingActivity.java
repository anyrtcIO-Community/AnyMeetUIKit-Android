package org.anyrtc.meeting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.anyrtc.lib_meeting.activity.NotifyMessageManager;
import org.anyrtc.lib_meeting.meet.MeetParams;
import org.anyrtc.lib_meeting.meet.MeetSDK;
import org.anyrtc.meeting.bean.Meet;
import org.anyrtc.meeting.bean.MeetInfo;
import org.anyrtc.meeting.bean.MeetList;
import org.anyrtc.meeting.bean.User;
import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeetingActivity extends AppCompatActivity implements NotifyMessageManager.imageChoosedNotify {

    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.tv_userId)
    TextView tvUserId;
    @BindView(R.id.et_theme)
    EditText etTheme;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_creat_meet)
    Button btnCreatMeet;
    @BindView(R.id.et_meetId)
    EditText etMeetId;
    @BindView(R.id.btn_join_meet)
    Button btnJoinMeet;
    @BindView(R.id.rv_meet_list)
    RecyclerView rvMeetList;
    private String userId;
    private String nickName;

    private boolean hadRegister = false;
    private User user;
    private Gson gson;
    private long oneDay = 1000 * 60 * 60 * 24;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private MeetListAdapter meetListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        ButterKnife.bind(this);
        meetListAdapter=new MeetListAdapter();
        rvMeetList.setLayoutManager(new LinearLayoutManager(this));
        rvMeetList.setAdapter(meetListAdapter);
        gson = new Gson();
        NotifyMessageManager.getInstance().setImageChoosedNotify(this);
        userId = (int) ((Math.random() * 9 + 1) * 100000) + "";
        nickName = NameUtils.getNickName();
    }


    /**
     * 该方法为进入图片选择后 选择的图片地址回调  开发者再此将图片上传到图片服务器 再将通知sdk  sendImageUploadFinishMessage
     *
     * @param imagePath
     */
    @Override
    public void imageList(List<String> imagePath) {
        List<String> list = new ArrayList<>();
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527246715175&di=6af4fc09ee94758368924c26ac163955&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F04ca1c568326e10000016d346e9e09.jpg");
        list.add("http://img1.imgtn.bdimg.com/it/u=3378476916,3056761097&fm=214&gp=0.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527246715175&di=6af4fc09ee94758368924c26ac163955&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F04ca1c568326e10000016d346e9e09.jpg");
        NotifyMessageManager.getInstance().sendImageUploadFinishMessage(list, "1000000");
    }

    @OnClick({R.id.btn_register, R.id.btn_creat_meet, R.id.btn_join_meet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                MeetSDK.getInstance().initTeameeting(userId, nickName, "", new MeetSDK.RequestResult() {
                    @Override
                    public void Result(String result) {
                        user = gson.fromJson(result, User.class);
                        if (user.getCode() == 200) {
                            hadRegister = true;
                            tvUserId.setText(userId);
                            Toast.makeText(MeetingActivity.this, "对接成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void Error(String message) {

                    }
                });
                break;
            case R.id.btn_creat_meet:
                if (etTheme.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MeetingActivity.this, "请输入会议主题", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (user == null) {
                    Toast.makeText(MeetingActivity.this, "请先对接Teameeting服务", Toast.LENGTH_SHORT).show();
                    return;
                }
                MeetSDK.getInstance().creatMeetRoom(user.getUserinfo().getU_anyrtc_openid(), nickName, "1", getDateToString(format, System.currentTimeMillis() + oneDay), etPassword.getText().toString(), "0", new JSONArray(), new MeetSDK.RequestResult() {
                    @Override
                    public void Result(String result) {
                        Meet meet = gson.fromJson(result, Meet.class);
                        if (meet.getCode() == 200) {
                            Toast.makeText(MeetingActivity.this, "创建会议成功", Toast.LENGTH_SHORT).show();
                        }
                        getMeetList();
                    }

                    @Override
                    public void Error(String message) {

                    }
                });
                break;
            case R.id.btn_join_meet:
                if (etMeetId.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MeetingActivity.this, "请输入会议ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (user == null) {
                    Toast.makeText(MeetingActivity.this, "请先对接Teameeting服务", Toast.LENGTH_SHORT).show();
                    return;
                }

                MeetSDK.getInstance().getMeetInfo(etMeetId.getText().toString().trim(), new MeetSDK.RequestResult() {
                    @Override
                    public void Result(String result) {
                        MeetInfo meetInfo = gson.fromJson(result, MeetInfo.class);
                        if (meetInfo.getCode() == 200) {
                            MeetParams meetParams = new MeetParams(meetInfo.getMeetinginfo().getM_anyrtcid(), userId, meetInfo.getMeetinginfo().getM_userid(), "", nickName, meetInfo.getMeetinginfo().getM_name(), meetInfo.getMeetinginfo().getM_password(), meetInfo.getMeetinginfo().getM_quality());
                            MeetSDK.getInstance().StartMeet(MeetingActivity.this, meetParams);
                        } else {
                            Toast.makeText(MeetingActivity.this, meetInfo.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void Error(String message) {

                    }
                });
                break;
        }
    }

    public String getDateToString(SimpleDateFormat sf, long time) {
        Date d = new Date(time);
        return sf.format(d);
    }



    public void getMeetList(){
        MeetSDK.getInstance().getMeetList(user.getUserinfo().getU_anyrtc_openid(), 1+"", 10086+"", new MeetSDK.RequestResult() {
            @Override
            public void Result(String result) {
                MeetList meetList=gson.fromJson(result,MeetList.class);
                if (meetList.getCode()==200){
                    if (meetList.getMeetinglist().size()>0){
                        meetListAdapter.setNewData(meetList.getMeetinglist());
                    }
                }else {
                    Toast.makeText(MeetingActivity.this, meetList.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void Error(String message) {

            }
        });
    }
}

package org.anyrtc.meeting.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.anyrtc.lib_meeting.meet.MeetParams;
import org.anyrtc.lib_meeting.meet.MeetSDK;
import org.anyrtc.lib_meeting.meet.UserParams;
import org.anyrtc.meeting.Constans;
import org.anyrtc.meeting.R;
import org.anyrtc.meeting.bean.MeetInfo;
import org.anyrtc.meeting.utils.SpUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class JoinActivity extends BaseActivity {


    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_meet_id)
    EditText etMeetId;
    @BindView(R.id.btn_join)
    Button btnJoin;

    @Override
    public int getLayoutId() {
        return R.layout.activity_join;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tvTitle.setText(R.string.join_meet);
        String id=getIntent().getStringExtra("meetid");
        if (!TextUtils.isEmpty(id)){
            etMeetId.setText(id);
        }
    }


    @OnClick({R.id.tv_left, R.id.btn_join})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finishAnimActivity();
                break;
            case R.id.btn_join:
                if (etMeetId.getText().toString().isEmpty()) {
                    return;
                }
                MeetSDK.getInstance().getMeetInfo(etMeetId.getText().toString().trim(), new MeetSDK.RequestResult() {
                    @Override
                    public void Result(String result) {
                        MeetInfo meetInfo = gson.fromJson(result, MeetInfo.class);
                        if (meetInfo.getCode() == 200) {
                            MeetParams meetParams = new MeetParams(meetInfo.getMeetinginfo().getMeetingid(), SpUtil.getString(Constans.ANYRTC_OPENID), meetInfo.getMeetinginfo().getM_userid(), meetInfo.getMeetinginfo().getM_name(), meetInfo.getMeetinginfo().getM_password(), meetInfo.getMeetinginfo().getM_quality(), meetInfo.getMeetinginfo().getM_is_lock());
                            UserParams userParams = new UserParams(SpUtil.getString(Constans.USERID), SpUtil.getString(Constans.HEADURL), SpUtil.getString(Constans.NICKNAME));
                            MeetSDK.getInstance().StartMeet(JoinActivity.this, meetParams, userParams);
                        } else {
                            Toast.makeText(JoinActivity.this, meetInfo.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void Error(String message) {

                    }
                });
                break;
        }
    }
}

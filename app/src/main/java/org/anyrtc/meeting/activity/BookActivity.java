package org.anyrtc.meeting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.anyrtc.lib_meeting.meet.MeetSDK;
import org.anyrtc.meeting.Constans;
import org.anyrtc.meeting.R;
import org.anyrtc.meeting.bean.Meet;
import org.anyrtc.meeting.utils.SpUtil;
import org.json.JSONArray;
import org.jzxiang.pickerview.TimePickerDialog;
import org.jzxiang.pickerview.data.Type;
import org.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class BookActivity extends BaseActivity implements OnDateSetListener {


    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_meet_name)
    EditText etMeetName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    long oneMounth = 1000 * 60 * 60 * 24L * 30;
    long oneHour = 1000 * 60 * 60;
    String time;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private TimePickerDialog timePickerDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_book;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tvTitle.setText(R.string.book_meet);
    }


    @OnClick({R.id.tv_left, R.id.fl_meet_time, R.id.btn_book})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finishAnimActivity();
                break;
            case R.id.fl_meet_time:
                showTimePicker();
                break;
            case R.id.btn_book:
                creatMeet();
                break;
        }
    }


    private void showTimePicker() {
        timePickerDialog = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId(getString(R.string.cancle))
                .setSureStringId(getString(R.string.confirm))
                .setTitleStringId("")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis() + oneHour)
                .setMaxMillseconds(System.currentTimeMillis() + oneMounth)
                .setType(Type.MONTH_DAY_HOUR_MIN)
                .build();
        timePickerDialog.show(getSupportFragmentManager(), "time");
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        time = format.format(new Date(millseconds));
        tvTime.setText(time);
    }

    @Override
    public void OnDissmiss() {

    }


    public void creatMeet() {
        if (TextUtils.isEmpty(etMeetName.getText().toString().trim())) {
            Toast.makeText(BookActivity.this, "请输入会议标题", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(time)) {
            Toast.makeText(BookActivity.this, "请选择会议开始时间", Toast.LENGTH_SHORT).show();
            return;
        }
        MeetSDK.getInstance().creatMeetRoom(SpUtil.getString(Constans.ANYRTC_OPENID), etMeetName.getText().toString(), "1", time, "", "0", new JSONArray(), new MeetSDK.RequestResult() {
            @Override
            public void Result(String result) {
                Meet meet = gson.fromJson(result, Meet.class);
                if (meet.getCode() == 200) {
                    Toast.makeText(BookActivity.this, "创建会议成功", Toast.LENGTH_SHORT).show();
                    Intent mIntent = new Intent();
                    mIntent.putExtra("needUpdata", true);
                    BookActivity.this.setResult(8, mIntent);
                    finishAnimActivity();
                } else {
                    Toast.makeText(BookActivity.this, meet.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void Error(String message) {

            }
        });
    }
}

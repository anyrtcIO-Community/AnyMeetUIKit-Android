package org.anyrtc.meeting;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.anyrtc.meeting.bean.MeetList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liuxiaozhong on 2018/5/25.
 */
public class MeetListAdapter extends BaseQuickAdapter<MeetList.MeetinglistBean,BaseViewHolder> {

    Calendar mCalendar;
    private SimpleDateFormat time = new SimpleDateFormat("HH:mm");

    public MeetListAdapter() {
        super(R.layout.item_meet_list);
        mCalendar= Calendar.getInstance();
    }


    @Override
    protected void convert(BaseViewHolder helper, MeetList.MeetinglistBean item) {
        mCalendar.setTimeInMillis(item.getM_start_time()*1000);
        int apm = mCalendar.get(Calendar.AM_PM);
        helper.setText(R.id.tv_pm , apm == 0 ? "AM" : "PM");
        helper.setText(R.id.tv_id, "会议ID:"+String.valueOf(item.getMeetingid()));
        helper.setText(R.id.tv_time, getDateToString(time,item.getM_start_time()*1000));
        helper.setText(R.id.tv_name,item.getM_name());
        helper.addOnClickListener(R.id.tv_del);
        helper.addOnClickListener(R.id.swipe);
        helper.addOnClickListener(R.id.tv_share);
    }

    public String getDateToString(SimpleDateFormat sf, long time) {
        Date d = new Date(time);
        return sf.format(d);
    }
}

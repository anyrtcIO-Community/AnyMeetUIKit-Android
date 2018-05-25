package org.anyrtc.meeting;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.anyrtc.meeting.bean.MeetList;

/**
 * Created by liuxiaozhong on 2018/5/25.
 */
public class MeetListAdapter extends BaseQuickAdapter<MeetList.MeetinglistBean,BaseViewHolder> {

    public MeetListAdapter() {
        super(R.layout.item_meet_list);
    }


    @Override
    protected void convert(BaseViewHolder helper, MeetList.MeetinglistBean item) {
        helper.setText(R.id.tv_name,"会议名称："+item.getM_name());
        helper.setText(R.id.tv_id,"会议ID："+item.getMeetingid());
    }
}

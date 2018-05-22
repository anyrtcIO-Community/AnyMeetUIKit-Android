package org.anyrtc.lib_meeting.activity;

import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.anyrtc.lib_meeting.R;
import org.anyrtc.lib_meeting.bean.MemberBean;

/**
 * Created by liuxiaozhong on 2017/12/21.
 */

public class libMemberAdapter extends BaseQuickAdapter<MemberBean, BaseViewHolder> {


    public libMemberAdapter() {
        super(R.layout.item_member_list);
    }


    @Override
    protected void convert(BaseViewHolder helper, MemberBean item) {
        helper.setText(R.id.tv_name, item.getName());
        ImageButton audio = helper.getView(R.id.ib_audio);
        ImageButton video = helper.getView(R.id.ib_video);
        if (item.getId().equals(item.getHostId())){
            helper.setVisible(R.id.tv_host_tip,true);
        }else {
            helper.setVisible(R.id.tv_host_tip,false);
        }

        ImageView icon = helper.getView(R.id.iv_icon);
        if (!item.getIcon().isEmpty()) {
            Glide.with(helper.itemView.getContext())
                    .load(item.getIcon())
                    .into(icon);
        } else {
            icon.setImageResource(R.drawable.img_doc_icon);
        }

        if (item.isOpenAudio()) {
            audio.setSelected(false);
        } else {
            audio.setSelected(true);
        }
        if (item.isOpenVideo()) {
            video.setSelected(false);
        } else {
            video.setSelected(true);
        }


    }

}

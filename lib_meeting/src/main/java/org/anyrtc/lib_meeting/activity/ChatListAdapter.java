package org.anyrtc.lib_meeting.activity;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.anyrtc.lib_meeting.R;
import org.anyrtc.lib_meeting.bean.ChatBean;

/**
 * Created by liuxiaozhong on 2017/12/21.
 */

public class ChatListAdapter extends BaseQuickAdapter<ChatBean,BaseViewHolder> {
    public ChatListAdapter() {
        super(R.layout.item_chat_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatBean item) {
        ImageView self_icon=helper.getView(R.id.iv_self_icon);
        ImageView other_icon=helper.getView(R.id.iv_icon);

        if (item.isSelf){
            helper.setVisible(R.id.ll_self,true);
            helper.setVisible(R.id.ll_other,false);
            helper.setText(R.id.tv_self_content,item.content);
            helper.setText(R.id.tv_self_name,item.name);
            if (!item.icon.isEmpty()) {
                Glide.with(helper.itemView.getContext())
                        .load(item.icon)
                        .into(self_icon);
            }
        }else {
            helper.setVisible(R.id.ll_self,false);
            helper.setVisible(R.id.ll_other,true);
            helper.setText(R.id.tv_other_content,item.content);
            helper.setText(R.id.tv_name,item.name);
            if (!item.icon.isEmpty()) {
                Glide.with(helper.itemView.getContext())
                        .load(item.icon)
                        .into(other_icon);
            }
        }
    }
}
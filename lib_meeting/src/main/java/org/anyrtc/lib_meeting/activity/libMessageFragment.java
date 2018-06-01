package org.anyrtc.lib_meeting.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.anyrtc.lib_meeting.R;
import org.anyrtc.lib_meeting.bean.ChatBean;
import org.anyrtc.meet_kit.RTMeetKit;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by liuxiaozhong on 2017/12/18.
 */

public class libMessageFragment extends lib_BaseFragment {

    TextView tvBack;
    EditText etContent;
    TextView btnSend;
    RecyclerView rv_message;
    ChatListAdapter chatListAdapter;
    RTMeetKit rtMeetKit;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    protected int getContentViewID() {
        return R.layout.fragment_message_lib;
    }

    @Override
    protected void initViewsAndEvents(View rootView, Bundle savedInstanceState) {
        tvBack=rootView.findViewById(R.id.tv_back);
        etContent=rootView.findViewById(R.id.et_message_lib);
        btnSend=rootView.findViewById(R.id.tv_send_lib);
        rv_message=rootView.findViewById(R.id.rv_message);
        chatListAdapter=new ChatListAdapter();
        rv_message.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_message.setAdapter(chatListAdapter);
        lib_MeetActivity activity= (lib_MeetActivity) getActivity();
        rtMeetKit=activity.getMeetKit();
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().hide(libMessageFragment.this).commit();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etContent.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), R.string.please_input_content,Toast.LENGTH_LONG).show();
                }else {
                    if (rtMeetKit != null) {
                        rtMeetKit.sendUserMessage(lib_MeetActivity.userParams.nickName, lib_MeetActivity.userParams.headUrl, setJsonMessage(etContent.getText().toString(), System.currentTimeMillis()));
                    }
                    chatListAdapter.addData(new ChatBean(etContent.getText().toString(), lib_MeetActivity.userParams.headUrl, true, lib_MeetActivity.userParams.nickName));
                    etContent.setText("");
                    if (chatListAdapter != null && chatListAdapter.getData().size() > 0) {
                        if (rv_message != null) {
                            rv_message.smoothScrollToPosition(chatListAdapter.getItemCount() - 1);
                        }
                    }
                }

            }
        });

    }
    private String setJsonMessage(String content, long currenttime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mType", 0);
            jsonObject.put("mTime", currenttime);
            jsonObject.put("mContent", it.sauronsoftware.base64.Base64.encode(content));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public void addMessage(ChatBean chatBean) {
        chatListAdapter.addData(chatBean);
        if (chatListAdapter != null && chatListAdapter.getData().size() > 0) {
            if (rv_message != null) {
                rv_message.smoothScrollToPosition(chatListAdapter.getItemCount() - 1);
            }
        }
    }
}

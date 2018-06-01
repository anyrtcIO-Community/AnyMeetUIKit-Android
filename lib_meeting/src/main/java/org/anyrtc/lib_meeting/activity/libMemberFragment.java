package org.anyrtc.lib_meeting.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.anyrtc.lib_meeting.MeetApplication;
import org.anyrtc.lib_meeting.R;
import org.anyrtc.lib_meeting.bean.MemberBean;
import org.anyrtc.lib_meeting.weight.CustomDialog;
import org.anyrtc.meet_kit.RTMeetKit;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by liuxiaozhong on 2017/12/18.
 */

public class libMemberFragment extends lib_BaseFragment implements BaseQuickAdapter.OnItemClickListener {

    TextView tvBack;
    TextView tvMessage;
    TextView tvInvatia;
    RecyclerView recyclerView;
    libMemberAdapter memberAdapter;
    private CustomDialog customDialog;
    private CustomDialog invitaDialog;
    private RTMeetKit meetKit;
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
        return R.layout.fragment_member_lib;
    }

    @Override
    protected void initViewsAndEvents(View rootView, Bundle savedInstanceState) {
        recyclerView = rootView.findViewById(R.id.rv_member_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        memberAdapter = new libMemberAdapter();
        memberAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(memberAdapter);
        tvBack = rootView.findViewById(R.id.tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().hide(libMemberFragment.this).commit();
            }
        });
        tvMessage = rootView.findViewById(R.id.tv_message_lib);
        tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lib_MeetActivity meetActivity = (lib_MeetActivity) getActivity();
                meetActivity.onClickMessage();
            }
        });
        tvInvatia=rootView.findViewById(R.id.tv_invatia);
        tvInvatia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInvitaDialog();
            }
        });
        lib_MeetActivity activity= (lib_MeetActivity) getActivity();
        meetKit=activity.getMeetKit();

        dataNotify();
    }

    public void dataNotify() {
        if (getActivity() instanceof lib_MeetActivity) {
            lib_MeetActivity activity = (lib_MeetActivity) getActivity();
            memberAdapter.setNewData(activity.getMemBerList());
            if (customDialog!=null&&customDialog.isShowing()){
                customDialog.dismiss();
            }
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            if (!memberAdapter.getItem(position).isHost()) {
                return;
            }
            if (memberAdapter.getItem(position).isSelf()){
                return;
            }
            showMemberDo(position);

    }

    private void showMemberDo(final int position) {
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setContentView(R.layout.dialog_member_state)
                .setAnimId(R.style.AnimBottom)
                .setGravity(Gravity.BOTTOM)
                .setLayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                .setBackgroundDrawable(true)
                .build();
        customDialog = builder.show(new CustomDialog.Builder.onInitListener() {
            @Override
            public void init(CustomDialog view) {
                MemberBean item = memberAdapter.getItem(position);
                if (item==null){
                    return;
                }
                TextView name = view.findViewById(R.id.txt_title);
                name.setText(item.name);
                Button video=view.findViewById(R.id.btn_video);
                video.setText(item.isOpenVideo() ? "禁止视频" : "打开视频");
                Button audio=view.findViewById(R.id.btn_audio);
                audio.setText(item.isOpenAudio() ? "禁止音频" : "打开音频");
                Button go=view.findViewById(R.id.btn_go_out);

                Button cancelBtn=view.findViewById(R.id.cancelBtn);
                audio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (memberAdapter.getItem(position)==null){
                            customDialog.dismiss();
                            return;
                        }
                        if (memberAdapter.getItem(position).isOpenAudio()){
                            memberAdapter.getItem(position).setOpenAudio(false);
                            if (meetKit!=null){
                                meetKit.sendUserMessage("android","",getCMD(0,0,memberAdapter.getItem(position).getId()));
                            }
                        }else {
                            memberAdapter.getItem(position).setOpenAudio(true);
                            if (meetKit!=null){
                                meetKit.sendUserMessage("android","",getCMD(0,1,memberAdapter.getItem(position).getId()));
                            }
                        }
                        memberAdapter.notifyItemChanged(position);
                        customDialog.dismiss();
                    }
                });

                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (memberAdapter.getItem(position)==null){
                            customDialog.dismiss();
                            return;
                        }
                        if (memberAdapter.getItem(position).isOpenVideo()){
                            memberAdapter.getItem(position).setOpenVideo(false);
                            if (meetKit!=null){
                                meetKit.sendUserMessage("android","",getCMD(1,0,memberAdapter.getItem(position).getId()));
                            }
                        }else {
                            memberAdapter.getItem(position).setOpenVideo(true);
                            if (meetKit!=null){
                                meetKit.sendUserMessage("android","",getCMD(1,1,memberAdapter.getItem(position).getId()));
                            }
                        }
                        memberAdapter.notifyItemChanged(position);
                        customDialog.dismiss();
                    }
                });
                go.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (meetKit != null) {
                            meetKit.sendUserMessage("android", "", getLeaveCMD(2, memberAdapter.getItem(position).getId()));
                        }
                        customDialog.dismiss();
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customDialog.dismiss();
                    }
                });

            }
        });
    }

    private void showInvitaDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setContentView(R.layout.dialog_member_invita)
                .setAnimId(R.style.AnimBottom)
                .setGravity(Gravity.BOTTOM)
                .setLayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                .setBackgroundDrawable(true)
                .build();
        invitaDialog = builder.show(new CustomDialog.Builder.onInitListener() {
            @Override
            public void init(CustomDialog view) {
                final String shareContent = getActivity().getString(R.string.let_us_meet_in_team) + "\n" +
                        getActivity().getString(R.string.meet_title) + lib_MeetActivity.meetParams.meetTheme + "\n" +
                        getActivity().getString(R.string.meet_url) + String.format(MeetApplication.SHARE_URL, lib_MeetActivity.meetParams.meetId) + "\n" +
                        getActivity().getString(R.string.meet_ids) + lib_MeetActivity.meetParams.meetId + "\n" +
                        getActivity().getString(R.string.meet_pwd) + (TextUtils.isEmpty(lib_MeetActivity.meetParams.meetPassword) ? getActivity().getString(R.string.no) : lib_MeetActivity.meetParams.meetPassword);
                Button Email=view.findViewById(R.id.btn_email);
                Button SMS=view.findViewById(R.id.btn_sms);
                Button URL=view.findViewById(R.id.btn_url);

                Button cancelBtn=view.findViewById(R.id.cancelBtn);
                Email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("mailto:");
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Teameeting"); // 主题
                        intent.putExtra(Intent.EXTRA_TEXT, shareContent);
                        getActivity().startActivity(intent);
                        invitaDialog.dismiss();
                    }
                });

                SMS.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Uri smsToUri = Uri.parse("smsto:");
                            Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                            intent.putExtra("sms_body", shareContent);
                            getActivity().startActivity(intent);
                        }catch (Exception e){
                            Toast.makeText(getActivity(),getActivity().getString(R.string.can_not_sms_share),Toast.LENGTH_SHORT).show();
                        }
                        invitaDialog.dismiss();
                    }
                });
                URL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager copy = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        // 将文本内容放到系统剪贴板里。
                        copy.setText(shareContent);
                        Toast.makeText(getActivity(),getActivity().getString(R.string.had_copy_link),Toast.LENGTH_SHORT).show();
                        invitaDialog.dismiss();
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        invitaDialog.dismiss();
                    }
                });

            }
        });
    }

    public String getCMD(int type,int state,String userid){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("mType",1);
            jsonObject.put("type",type);
            jsonObject.put("state",state);
            jsonObject.put("userid",userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String getLeaveCMD(int type,String userid){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("mType",2);
            jsonObject.put("userid",userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}

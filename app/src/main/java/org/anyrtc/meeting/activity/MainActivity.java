package org.anyrtc.meeting.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gavin.com.library.StickyDecoration;
import com.gavin.com.library.listener.GroupListener;

import org.anyrtc.lib_meeting.activity.NotifyMessageManager;
import org.anyrtc.lib_meeting.meet.MeetParams;
import org.anyrtc.lib_meeting.meet.MeetSDK;
import org.anyrtc.lib_meeting.meet.UserParams;
import org.anyrtc.meeting.Constans;
import org.anyrtc.meeting.MeetListAdapter;
import org.anyrtc.meeting.R;
import org.anyrtc.meeting.bean.MeetInfo;
import org.anyrtc.meeting.bean.MeetList;
import org.anyrtc.meeting.utils.ScreenUtils;
import org.anyrtc.meeting.utils.SpUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements NotifyMessageManager.imageChoosedNotify,SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.OnItemChildClickListener{
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_join)
    FrameLayout flJoin;
    @BindView(R.id.fl_book)
    FrameLayout flBook;
    @BindView(R.id.rv_meet_list)
    RecyclerView rvMeetList;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private String userOpenId="";

    private StickyDecoration decoration;
    private SimpleDateFormat year = new SimpleDateFormat("yyyy-MM-dd");
    //
//    @BindView(R.id.btn_register)
//    Button btnRegister;
//    @BindView(R.id.tv_userId)
//    TextView tvUserId;
//    @BindView(R.id.et_theme)
//    EditText etTheme;
//    @BindView(R.id.et_password)
//    EditText etPassword;
//    @BindView(R.id.btn_creat_meet)
//    Button btnCreatMeet;
//    @BindView(R.id.et_meetId)
//    EditText etMeetId;
//    @BindView(R.id.btn_join_meet)
//    Button btnJoinMeet;
//    @BindView(R.id.rv_meet_list)
//    RecyclerView rvMeetList;
//    private String userId;
//    private String nickName;
//
//    private boolean hadRegister = false;
//    private User user;
//    private Gson gson;
//    private long oneDay = 1000 * 60 * 60 * 24;
//    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private MeetListAdapter meetListAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_meeting;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tvTitle.setText(R.string.anyrtc_meet);
        tvLeft.setVisibility(View.GONE);
        NotifyMessageManager.getInstance().setImageChoosedNotify(this);
        userOpenId= SpUtil.getString(Constans.ANYRTC_OPENID);
        meetListAdapter = new MeetListAdapter();
        meetListAdapter.setOnItemChildClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        rvMeetList.setLayoutManager(new LinearLayoutManager(this));
        decoration = StickyDecoration.Builder
                .init(new GroupListener() {
                    @Override
                    public String getGroupName(int position) {
                        if (meetListAdapter.getData() == null || meetListAdapter.getData().size() == 0) {
                            return null;
                        }
                        if (position > meetListAdapter.getData().size() - 1) {
                            return null;
                        }
                        return meetListAdapter.getData().get(position).getText();
                    }
                })
                .setGroupBackground(Color.parseColor("#EBEBEB"))  //背景色（默认 #48BDFF）
                .setGroupHeight(ScreenUtils.dip2px(this, 28))     //高度 (默认120px)
                .setGroupTextColor(Color.parseColor("#999999"))                   //字体颜色 （默认 Color.WHITE）
                .setGroupTextSize(ScreenUtils.sp2px(this, 12))    //字体大小 （默认 50px）
                .setDivideColor(Color.parseColor("#eeeeee"))      //分割线颜色（默认 #CCCCCC）
                .setDivideHeight(ScreenUtils.dip2px(this, 1))
                .setTextSideMargin(ScreenUtils.dip2px(this, 12))  //边距   靠左时为左边距  靠右时为右边距（默认 10）
                .isAlignLeft(true)                               //靠右显示  （默认 靠左）
                .build();
        rvMeetList.addItemDecoration(decoration);
        rvMeetList.setAdapter(meetListAdapter);
        getMeetList();
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

    public String getDateToString(SimpleDateFormat sf, long time) {
        Date d = new Date(time);
        return sf.format(d);
    }


    @OnClick({R.id.fl_join, R.id.fl_book})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_join:
                startAnimActivity(JoinActivity.class);
                break;
            case R.id.fl_book:
                Intent i=new Intent(MainActivity.this,BookActivity.class);
                startActivityForResult(i,8);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null) {
            boolean needUpdata = data.getBooleanExtra("needUpdata", false);
            if (resultCode == 8 && requestCode == 8 && needUpdata) {
                getMeetList();
            }
        }
    }

    public void getMeetList(){
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
        MeetSDK.getInstance().getMeetList(userOpenId, 1+"", 10086+"", new MeetSDK.RequestResult() {
            @Override
            public void Result(String result) {
                swipeRefreshLayout.setRefreshing(false);
                MeetList meetList=gson.fromJson(result,MeetList.class);
                if (meetList.getCode()==200){
                    if (meetList.getMeetinglist().size()>0){

                        for (MeetList.MeetinglistBean meet:meetList.getMeetinglist()){
                            meet.text=getDateToString(year, meet.getM_start_time() * 1000);
                        }
                        Collections.sort(meetList.getMeetinglist(), new Comparator<MeetList.MeetinglistBean>() {
                            @Override
                            public int compare(MeetList.MeetinglistBean o1, MeetList.MeetinglistBean o2) {
                                Long a = o1.getM_start_time();
                                Long b = o2.getM_start_time();
                                return a.compareTo(b);
                            }
                        });
                        meetListAdapter.setNewData(meetList.getMeetinglist());
                    }
                }else {
                    Toast.makeText(MainActivity.this, meetList.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void Error(String message) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        getMeetList();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
        switch (view.getId()) {
            case R.id.tv_del:
               MeetSDK.getInstance().deleteMeetRoom(SpUtil.getString(Constans.ANYRTC_OPENID), meetListAdapter.getItem(position).getMeetingid(), new MeetSDK.RequestResult() {
                   @Override
                   public void Result(String result) {
                            if (!result.isEmpty()){
                               try {
                                   int code=new JSONObject(result).getInt("code");
                                   if (code==200){
                                       meetListAdapter.remove(position);
                                   }else {
                                       Toast.makeText(MainActivity.this,new JSONObject(result).getString("message"), Toast.LENGTH_SHORT).show();
                                   }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                   }

                   @Override
                   public void Error(String message) {
                   }
               });
                break;
            case R.id.swipe:
                MeetSDK.getInstance().getMeetInfo(meetListAdapter.getItem(position).getMeetingid(), new MeetSDK.RequestResult() {
                    @Override
                    public void Result(String result) {
                        MeetInfo meetInfo = gson.fromJson(result, MeetInfo.class);
                        if (meetInfo.getCode() == 200) {
                            MeetParams meetParams = new MeetParams(meetInfo.getMeetinginfo().getMeetingid(), SpUtil.getString(Constans.ANYRTC_OPENID),meetInfo.getMeetinginfo().getM_userid(), meetInfo.getMeetinginfo().getM_name(), meetInfo.getMeetinginfo().getM_password(), meetInfo.getMeetinginfo().getM_quality(),meetInfo.getMeetinginfo().getM_is_lock());
                            UserParams userParams=new UserParams(SpUtil.getString(Constans.USERID),SpUtil.getString(Constans.HEADURL),SpUtil.getString(Constans.NICKNAME));
                            MeetSDK.getInstance().StartMeet(MainActivity.this, meetParams,userParams);
                        } else {
                            Toast.makeText(MainActivity.this, meetInfo.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void Error(String message) {

                    }
                });
                break;
            case R.id.tv_share:
                break;
        }
    }
}

package org.anyrtc.lib_meeting.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.example.zhouwei.library.CustomPopWindow;

import org.anyrtc.common.utils.ScreenUtils;
import org.anyrtc.lib_meeting.R;
import org.anyrtc.lib_meeting.utils.DocImageLoader;
import org.anyrtc.whiteboard.http.WhiteBoard;
import org.anyrtc.whiteboard.http.WhiteboardConfig;
import org.anyrtc.whiteboard.http.WihteBoardListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liuxiaozhong on 2017/12/18.
 */

public class libWhiteBoardFragment extends lib_BaseFragment implements WihteBoardListener, View.OnClickListener {

    WhiteBoard whiteBoard;
    private CustomPopWindow popWindowColor;
    private ImageView ivLeft, ivRight, ivMoreFutures;
    private ImageButton ibPaint, ibJiantou, ibColor, ibUndo, ibClean;
    private LinearLayout llMoreFutures;
    private FrameLayout fl_more_futures;
    private RelativeLayout rl_rootView;
    List<String> imageList = new ArrayList<>();
    private boolean isSelfOpenBoard = false;
    private String fileId = "";
    private lib_MeetActivity meetActivity;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void setImageList(List<String> imageList, boolean isSelf, String fileId) {
        this.imageList = imageList;
        this.isSelfOpenBoard = isSelf;
        this.fileId = fileId;
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_whiteboard_lib;
    }

    @Override
    protected void initViewsAndEvents(View rootView, Bundle savedInstanceState) {
        whiteBoard = rootView.findViewById(R.id.wb_board);
        ibColor = rootView.findViewById(R.id.ib_color);
        ivMoreFutures = rootView.findViewById(R.id.iv_more_future);
        ivLeft = rootView.findViewById(R.id.iv_left_lib);
        ivRight = rootView.findViewById(R.id.iv_right_lib);
        ibPaint = rootView.findViewById(R.id.ib_paint);
        ibJiantou = rootView.findViewById(R.id.ib_jiantou);
        ibUndo = rootView.findViewById(R.id.ib_undo);
        ibClean = rootView.findViewById(R.id.ib_clean);
        llMoreFutures = rootView.findViewById(R.id.ll_more_futures);
        rl_rootView = rootView.findViewById(R.id.rl_rootView);
        fl_more_futures = rootView.findViewById(R.id.fl_more_futures);
        ibJiantou.setOnClickListener(this);
        ibClean.setOnClickListener(this);
        ibPaint.setOnClickListener(this);
        ibUndo.setOnClickListener(this);
        ibColor.setOnClickListener(this);
        ivLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        ivMoreFutures.setOnClickListener(this);
        whiteBoard.setWhiteBoardListener(this);
        meetActivity = (lib_MeetActivity) getActivity();
        whiteBoard.setImageLoader(new DocImageLoader());
        if (imageList.size() > 0) {//显示
            whiteBoard.displayImageList(imageList);
        }
        if (WhiteboardConfig.getInstance().getDrawType() == 0) {
            ibPaint.setSelected(true);
            ibJiantou.setSelected(false);
        } else if (WhiteboardConfig.getInstance().getDrawType() == 1) {
            ibJiantou.setSelected(true);
            ibPaint.setSelected(false);
        }


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onConfigurationPortrait();

    }

    private void onConfigurationPortrait() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) whiteBoard.getLayoutParams(); //取控件mRlVideoViewLayout当前的布局参数
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            final float height = this.getResources().getDisplayMetrics().heightPixels;
            final double width = height * 1.3333;//16:9
            params.height = (int) height;// 强制设置控件的大小
            params.width = (int) width;
        } else {
            final float width = this.getResources().getDisplayMetrics().widthPixels;
            final double height = width / 1.3333;//16:9   薯片
            params.height = (int) height;// 强制设置控件的大小
            params.width = (int) width;
        }
        whiteBoard.setLayoutParams(params); //使设置好的布局参数应用到控件
        whiteBoard.requestLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("fragment", "onresume");
        onConfigurationPortrait();
    }


    @Override
    public void initAnyRTCSuccess() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("white", "initAnyRTCSuccess");
                JSONArray boardArray = new JSONArray();
                for (int i = 0; i < imageList.size(); i++) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("board_number", i + 1);
                        jsonObject.put("board_background", imageList.get(i));
                        boardArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                whiteBoard.initWhiteBoardWithPic(fileId, lib_MeetActivity.anyRTCId, boardArray, isSelfOpenBoard, lib_MeetActivity.userId);
            }
        });
    }

    @Override
    public void initBoardSuccess() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("white", "initBoardSuccess");
                WhiteboardConfig.getInstance().setBoardEditable(true);
            }
        });
    }

    @Override
    public void initBoardFaild(int code) {

    }

    @Override
    public void onBoardEditable(boolean editable) {

    }

    @Override
    public void onHostAddedBoard() {

    }

    @Override
    public void onPageSelect(final int currentPage, final String url, final int totlePage) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (totlePage <= 1) {
                    ivLeft.setVisibility(View.GONE);
                    ivRight.setVisibility(View.GONE);
                } else {
                    if (currentPage == 1) {
                        ivLeft.setVisibility(View.GONE);
                        ivRight.setVisibility(View.VISIBLE);
                    } else {
                        if (currentPage == imageList.size()) {
                            ivLeft.setVisibility(View.VISIBLE);
                            ivRight.setVisibility(View.GONE);
                        } else {
                            ivLeft.setVisibility(View.VISIBLE);
                            ivRight.setVisibility(View.VISIBLE);
                        }

                    }
                }

            }
        });
    }

    @Override
    public void onBoardDestroy() {

    }

    public void close() {
        whiteBoard.leaveBoard();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ib_color) {
            ibColor.setSelected(true);
            View colorcontentView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_white_board_futures, null);
            handleColorOnclick(colorcontentView);
            popWindowColor = new CustomPopWindow.PopupWindowBuilder(getActivity())
                    .setView(colorcontentView)
                    .size(ScreenUtils.getScreenWidth(getActivity()) - 60, 260)
                    .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            ibColor.setSelected(false);
                        }
                    })
                    .create();
            popWindowColor.showAsDropDown(ivMoreFutures, 0, -(ivMoreFutures.getHeight() + popWindowColor.getHeight() + 20), Gravity.CENTER);
        }
        if (id == R.id.iv_left_lib) {
            int last = whiteBoard.lastPage();
            if (last == 0) {
                ivLeft.setVisibility(View.GONE);
                ivRight.setVisibility(View.VISIBLE);
            } else {
                ivRight.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.iv_right_lib) {
            int next = whiteBoard.nextPage();
            if (next == 0) {
                ivRight.setVisibility(View.GONE);
                ivLeft.setVisibility(View.VISIBLE);
            } else {
                ivLeft.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.ib_paint) {
            WhiteboardConfig.getInstance().setDrawType(WhiteboardConfig.Shape.Point);
            updataPaintType();
        } else if (id == R.id.ib_jiantou) {
            WhiteboardConfig.getInstance().setDrawType(WhiteboardConfig.Shape.JianTou);
            updataPaintType();

        } else if (id == R.id.ib_undo) {
//            if (WhiteboardConfig.getInstance().isBoardEnable()) {
                whiteBoard.removeLastStroke();
//            } else {
//                Toast.makeText(getActivity(), "当前文档不可编辑", Toast.LENGTH_SHORT).show();
//            }

        } else if (id == R.id.ib_clean) {
//            if (WhiteboardConfig.getInstance().isBoardEnable()) {
                whiteBoard.cleanBoard();
//            } else {
//                Toast.makeText(getActivity(), "当前文档不可编辑", Toast.LENGTH_SHORT).show();
//            }

        } else if (id == R.id.iv_more_future) {
            if (llMoreFutures.getVisibility() == View.VISIBLE) {
                llMoreFutures.setVisibility(View.GONE);
                if (meetActivity != null) {
                    meetActivity.setVideoLayoutEnable(true);
                }
            } else {
                llMoreFutures.setVisibility(View.VISIBLE);
                if (meetActivity != null) {
                    meetActivity.setVideoLayoutEnable(false);
                }
            }
        }

    }


    private void updataPaintType() {
        switch (WhiteboardConfig.getInstance().getDrawType()) {
            case 0://涂鸦
                ibPaint.setSelected(true);
                ibJiantou.setSelected(false);
                break;
            case 1://箭头
                ibJiantou.setSelected(true);
                ibPaint.setSelected(false);
                break;
        }
    }

    private void handleColorOnclick(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if (id == R.id.ib_red) {
                    WhiteboardConfig.getInstance().setDrawColor("#ff5050");
                } else if (id == R.id.ib_green) {
                    WhiteboardConfig.getInstance().setDrawColor("#31ff7a");
                } else if (id == R.id.ib_blue) {
                    WhiteboardConfig.getInstance().setDrawColor("#4e91ff");
                } else if (id == R.id.ib_yellow) {
                    WhiteboardConfig.getInstance().setDrawColor("#ffd133");
                } else if (id == R.id.ib_black) {
                    WhiteboardConfig.getInstance().setDrawColor("#000000");
                } else if (id == R.id.ib_white) {
                    WhiteboardConfig.getInstance().setDrawColor("#ffffff");
                }
                if (popWindowColor != null) {
                    popWindowColor.dissmiss();
                }
            }
        };
        ImageButton red = contentView.findViewById(R.id.ib_red);
        ImageButton black = contentView.findViewById(R.id.ib_black);
        ImageButton yellow = contentView.findViewById(R.id.ib_yellow);
        ImageButton green = contentView.findViewById(R.id.ib_green);
        ImageButton blue = contentView.findViewById(R.id.ib_blue);
        ImageButton white = contentView.findViewById(R.id.ib_white);
        SeekBar seekBar = contentView.findViewById(R.id.sk_seekbar);

        seekBar.setProgress((int) WhiteboardConfig.getInstance().getLineWidth());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int width, boolean b) {
                WhiteboardConfig.getInstance().setLineWidth((float) width);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        green.setOnClickListener(listener);
        blue.setOnClickListener(listener);
        white.setOnClickListener(listener);
        red.setOnClickListener(listener);
        black.setOnClickListener(listener);
        yellow.setOnClickListener(listener);

        switch (WhiteboardConfig.getInstance().getDrawColor().toUpperCase()) {
            case "#FF5050":
                green.setSelected(false);
                black.setSelected(false);
                blue.setSelected(false);
                red.setSelected(true);
                yellow.setSelected(false);
                white.setSelected(false);
                break;
            case "#4E91FF":
                green.setSelected(false);
                black.setSelected(false);
                blue.setSelected(true);
                red.setSelected(false);
                yellow.setSelected(false);
                white.setSelected(false);
                break;
            case "#FFFFFF":
                green.setSelected(false);
                black.setSelected(false);
                blue.setSelected(false);
                red.setSelected(false);
                yellow.setSelected(false);
                white.setSelected(true);
                break;
            case "#000000":
                green.setSelected(false);
                black.setSelected(true);
                blue.setSelected(false);
                red.setSelected(false);
                yellow.setSelected(false);
                white.setSelected(false);
                break;
            case "#FFD133":
                green.setSelected(false);
                black.setSelected(false);
                blue.setSelected(false);
                red.setSelected(false);
                yellow.setSelected(true);
                white.setSelected(false);
                break;
            default:
                green.setSelected(true);
                black.setSelected(false);
                blue.setSelected(false);
                red.setSelected(false);
                yellow.setSelected(false);
                white.setSelected(false);
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (whiteBoard==null){
            return;
        }
        if (hidden) {
            whiteBoard.getPaintView().setVisibility(View.GONE);
        } else {
            whiteBoard.getPaintView().setVisibility(View.VISIBLE);
        }
    }
}

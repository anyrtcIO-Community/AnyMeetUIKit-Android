package org.anyrtc.lib_meeting.weight;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.anyrtc.common.utils.ScreenUtils;
import org.anyrtc.lib_meeting.R;
import org.webrtc.EglBase;
import org.webrtc.PercentFrameLayout;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoRenderer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.view.View.VISIBLE;


/**
 * Created by Eric on 2016/7/26.
 */
public class TempVideoView implements RTCViewHelper, View.OnTouchListener {
    private static int SUB_X = 2;
    private static int SUB_Y = 72;
    private static int SUB_WIDTH = 15;
    private static int SUB_HEIGHT = 15;
    private static int mScreenWidth;
    private static int mScreenHeight;
    private BtnVideoCloseEvent mVideoCloseEvent;
    private boolean isHost;

    /**
     * 视频切换后更新视频的布局
     *
     * @param view1
     * @param view2
     */
    private void updateVideoLayout(TempVideoView.VideoView view1, TempVideoView.VideoView view2) {
        if (view1.Fullscreen()) {
            view1.mView.setZOrderMediaOverlay(false);
            view2.mView.setZOrderMediaOverlay(true);
            mVideoView.removeView(view1.mLayout);
            mVideoView.removeView(view2.mLayout);
            mVideoView.addView(view1.mLayout);
            mVideoView.addView(view2.mLayout);
            view1.mLayout.requestLayout();
            view2.mLayout.requestLayout();
        } else if (view2.Fullscreen()) {
            view1.mView.setZOrderMediaOverlay(false);
            view2.mView.setZOrderMediaOverlay(true);
            mVideoView.removeView(view1.mLayout);
            mVideoView.removeView(view2.mLayout);
            mVideoView.addView(view1.mLayout);
            mVideoView.addView(view2.mLayout);
            view2.mLayout.requestLayout();
            view1.mLayout.requestLayout();
        } else {
            view1.mLayout.requestLayout();
            view2.mLayout.requestLayout();
            mVideoView.removeView(view1.mLayout);
            mVideoView.removeView(view2.mLayout);
            mVideoView.addView(view1.mLayout, 0);
            mVideoView.addView(view2.mLayout, 0);
        }
        Iterator<Map.Entry<String, VideoView>> iter = mRemoteRenders.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, VideoView> entry = iter.next();
            TempVideoView.VideoView render = entry.getValue();
            if (render != view1 && render != view2) {
                render.mView.setZOrderMediaOverlay(true);
                mVideoView.removeView(render.mLayout);
                mVideoView.addView(render.mLayout);
                render.mLayout.requestLayout();
            }
        }
        if (!mLocalRender.Fullscreen()) {
            mVideoView.removeView(mLocalRender.mLayout);
            mVideoView.addView(mLocalRender.mLayout);
            mLocalRender.mLayout.requestLayout();
        }
    }


    private void SwitchViewToFullscreen(TempVideoView.VideoView view1, TempVideoView.VideoView fullscrnView) {
        int index, x, y, w, h;
//        String peerid;
//        ImageView mAudioImageView;
//        ImageView mVideoImageView;
//        ImageView mLocalCamera;

        index = view1.index;
        x = view1.x;
        y = view1.y;
        w = view1.w;
        h = view1.h;
//        peerid = view1.strPeerId;
//        mAudioImageView = view1.mAudioImageView;
//        mVideoImageView = view1.mVideoImageView;
//        mLocalCamera = view1.mLocalCamera;

        view1.index = fullscrnView.index;
        view1.x = fullscrnView.x;
        view1.y = fullscrnView.y;
        view1.w = fullscrnView.w;
        view1.h = fullscrnView.h;
//        view1.strPeerId = fullscrnView.strPeerId;
//        view1.mAudioImageView = fullscrnView.mAudioImageView;
//        view1.mVideoImageView = fullscrnView.mVideoImageView;
//        view1.mLocalCamera = fullscrnView.mLocalCamera;

        fullscrnView.index = index;
        fullscrnView.x = x;
        fullscrnView.y = y;
        fullscrnView.w = w;
        fullscrnView.h = h;
//        fullscrnView.strPeerId = peerid;
//        fullscrnView.mAudioImageView = mAudioImageView;
//        fullscrnView.mVideoImageView = mVideoImageView;
//        fullscrnView.mLocalCamera = mLocalCamera;

        fullscrnView.mLayout.setPosition(fullscrnView.x, fullscrnView.y, fullscrnView.w, fullscrnView.h);
        view1.mLayout.setPosition(view1.x, view1.y, view1.w, view1.h);

        updateVideoLayout(view1, fullscrnView);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int startX = (int) event.getX();
            int startY = (int) event.getY();
            if (mLocalRender.Hited(startX, startY)) {
                return true;
            } else {
                Iterator<Map.Entry<String, VideoView>> iter = mRemoteRenders.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, VideoView> entry = iter.next();
                    String peerId = entry.getKey();
                    TempVideoView.VideoView render = entry.getValue();
                    if (render.Hited(startX, startY)) {
                        return true;
                    }
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            int startX = (int) event.getX();
            int startY = (int) event.getY();
            if (mLocalRender.Hited(startX, startY)) {
//                mVideoClickEvent.onVideoTouch("localRender");
                SwitchViewToFullscreen(mLocalRender, GetFullScreen());
                return true;
            } else {
                Iterator<Map.Entry<String, VideoView>> iter = mRemoteRenders.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, VideoView> entry = iter.next();
                    String peerId = entry.getKey();
                    TempVideoView.VideoView render = entry.getValue();
                    if (render.Hited(startX, startY)) {
//                        mVideoClickEvent.onVideoTouch(peerId);
                        SwitchViewToFullscreen(render, GetFullScreen());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取全屏的界面
     *
     * @return
     */
    private TempVideoView.VideoView GetFullScreen() {
        if (mLocalRender.Fullscreen()) {
            return mLocalRender;
        }
        Iterator<Map.Entry<String, VideoView>> iter = mRemoteRenders.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, VideoView> entry = iter.next();
            String peerId = entry.getKey();
            TempVideoView.VideoView render = entry.getValue();
            if (render.Fullscreen()) {
                return render;
            }
        }
        return null;
    }

    public interface BtnVideoCloseEvent {
        void CloseVideoRender(View view, String strPeerId);

        void OnSwitchCamera(View view);
    }


    /**
     * 设置1x3模式下点击小图像切换至全屏
     *
     * @param enable
     */
    public void setVideoSwitchEnable(boolean enable) {
        mVideoView.setOnTouchListener(this);
    }

    protected static class VideoView {
        public String strPeerId;
        public int index;
        public int x;
        public int y;
        public int w;
        public int h;
        public PercentFrameLayout mLayout = null;
        public SurfaceViewRenderer mView = null;
        public VideoRenderer mRenderer = null;
        public TextView tv_name = null;
        private RelativeLayout layoutCamera = null;
        public VideoView(String strPeerId, Context ctx, EglBase eglBase, int index, int x, int y, int w, int h) {
            this.strPeerId = strPeerId;
            this.index = index;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;

            mLayout = new PercentFrameLayout(ctx);
            mLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            View view = View.inflate(ctx, R.layout.layout_top_right, null);

            mView = (SurfaceViewRenderer) view.findViewById(R.id.suface_view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_name.setText(strPeerId);
            layoutCamera = (RelativeLayout) view.findViewById(R.id.layout_camera);
            mView.init(eglBase.getEglBaseContext(), null);
            mView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            mLayout.addView(view);
        }

        public Boolean Fullscreen() {
            return w == 100 || h == 100;
        }

        public Boolean Hited(int px, int py) {
            mScreenWidth = ScreenUtils.getScreenWidth(mLayout.getContext());
            mScreenHeight = ScreenUtils.getScreenHeight(mLayout.getContext()) ;
            if (!Fullscreen()) {
                int left = x * mScreenWidth / 100;
                int top = y * mScreenHeight / 100;
                int right = (x + w) * mScreenWidth / 100;
                int bottom = (y + h) * mScreenHeight / 100;
                if ((px >= left && px <= right) && (py >= top && py <= bottom)) {
                    return true;
                }
            }
            return false;
        }

        public void close() {
            mLayout.removeView(mView);
            mView.release();
            mView = null;
            mRenderer = null;
        }
    }

    public VideoRenderer openScreenShare(final String strRtcPeerId) {
        TempVideoView.VideoView remoteRender = screenRender.get(strRtcPeerId);
        if (remoteRender == null) {
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                remoteRender = new VideoView(strRtcPeerId, mVideoView.getContext(), mRootEglBase, 0, 0, 0, 100, 100);
            } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                remoteRender = new VideoView(strRtcPeerId, mVideoView.getContext(), mRootEglBase, 0, 0, 36, 100, 27);
            }

            mVideoView.addView(remoteRender.mLayout, mVideoView.getChildCount());
            remoteRender.mLayout.setPosition(
                    remoteRender.x, remoteRender.y, remoteRender.w, remoteRender.h);
            remoteRender.mView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
            remoteRender.mView.setZOrderMediaOverlay(true);
            remoteRender.mRenderer = new VideoRenderer(remoteRender.mView);
            screenRender.put(strRtcPeerId, remoteRender);
        }
        return remoteRender.mRenderer;
    }

    public void removeScreenShare(String publishId) {
        TempVideoView.VideoView videoView = screenRender.get(publishId);
        if (videoView != null) {
            videoView.mView.setVisibility(View.GONE);
            mVideoView.removeView(videoView.mLayout);
            screenRender.remove(publishId);
        }
    }

    public void setPeopleShow(int show, String publishId) {
        if (VISIBLE == show) {
            TempVideoView.VideoView videoView = screenRender.get(publishId);
            if (videoView != null) {
                videoView.mView.setVisibility(View.GONE);
//                videoView.tv_name.setVisibility(View.GONE);
                videoView.mLayout.setVisibility(View.GONE);
            }
            mLocalRender.mView.setVisibility(VISIBLE);
            mLocalRender.mLayout.setVisibility(VISIBLE);
            Iterator<Map.Entry<String, VideoView>> iter = mRemoteRenders.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, VideoView> entry = iter.next();
                TempVideoView.VideoView render = entry.getValue();
                render.mView.setVisibility(VISIBLE);
//                render.tv_name.setVisibility(View.VISIBLE);
                render.mLayout.setVisibility(VISIBLE);
            }
        } else {
            mLocalRender.mView.setVisibility(View.GONE);
            mLocalRender.mLayout.setVisibility(View.GONE);
            Iterator<Map.Entry<String, VideoView>> iter = mRemoteRenders.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, VideoView> entry = iter.next();
                TempVideoView.VideoView render = entry.getValue();
                render.mView.setVisibility(View.GONE);
//                render.tv_name.setVisibility(View.GONE);
                render.mLayout.setVisibility(View.GONE);

            }
            TempVideoView.VideoView videoView = screenRender.get(publishId);
            if (videoView != null) {
                videoView.mView.setVisibility(View.VISIBLE);
                videoView.mView.setZOrderMediaOverlay(true);
//                videoView.tv_name.setVisibility(View.VISIBLE);
                videoView.mLayout.setVisibility(View.VISIBLE);
//                mVideoView.removeView(videoView.mLayout);
//                screenRender.remove(publishId);
            }
//            Iterator<Map.Entry<String, VideoView>> screen = screenRender.entrySet().iterator();
//            while (screen.hasNext()) {
//                Map.Entry<String, VideoView> entry = screen.next();
//                TempVideoView.VideoView render = entry.getValue();
//                render.mLayout.setVisibility(VISIBLE);
//            }
        }


    }

    private boolean mAutoLayout;
    private EglBase mRootEglBase;
    private static RelativeLayout mVideoView;
    private VideoView mLocalRender;
    public HashMap<String, VideoView> mRemoteRenders;
    private HashMap<String, VideoView> screenRender;
    private Context context;
    public TempVideoView(RelativeLayout videoView, Context context, EglBase eglBase, boolean isHost) {
        mAutoLayout = false;
        mVideoView = videoView;
        mRootEglBase = eglBase;
        mLocalRender = null;
        mRemoteRenders = new HashMap<>();
        screenRender = new HashMap<>();
        this.isHost = isHost;
        this.context=context;
    }

    public int GetVideoRenderSize() {
        int size = mRemoteRenders.size();
        if (mLocalRender != null) {
            size += 1;
        }
        return size;
    }

    public int GetPeopleSize() {
        int size = mRemoteRenders.size();
        return size;
    }

    private void SwitchViewPosition(VideoView view1, VideoView view2) {
        int index, x, y, w, h;
        index = view1.index;
        x = view1.x;
        y = view1.y;
        w = view1.w;
        h = view1.h;

        view1.index = view2.index;
        view1.x = view2.x;
        view1.y = view2.y;
        view1.w = view2.w;
        view1.h = view2.h;

        view2.index = index;
        view2.x = x;
        view2.y = y;
        view2.w = w;
        view2.h = h;

        if (view1.Fullscreen()) {
//            view1.layoutCamera.setPadding(0, 0, 0, 0);
            view1.mView.setZOrderMediaOverlay(true);
        } else {
//            view1.layoutCamera.setPadding(6, 6, 6, 6);
            view1.mView.setZOrderMediaOverlay(true);
        }

        if (view2.Fullscreen()) {
//            view2.layoutCamera.setPadding(0, 0, 0, 0);
            view2.mView.setZOrderMediaOverlay(true);
        } else {
//            view2.layoutCamera.setPadding(6, 6, 6, 6);
            view2.mView.setZOrderMediaOverlay(true);
        }
        view1.mLayout.setPosition(view1.x, view1.y, view1.w, view1.h);
        view2.mLayout.setPosition(view2.x, view2.y, view2.w, view2.h);
        mVideoView.updateViewLayout(view1.mLayout, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mVideoView.updateViewLayout(view2.mLayout, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    }

    public void BubbleSortSubView(VideoView view) {
        if (mLocalRender != null && view.index + 1 == mLocalRender.index) {
            SwitchViewPosition(mLocalRender, view);
        } else {
            Iterator<Map.Entry<String, VideoView>> iter = mRemoteRenders.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, VideoView> entry = iter.next();
                VideoView render = entry.getValue();
                if (view.index + 1 == render.index) {
                    SwitchViewPosition(render, view);
                    break;
                }
            }
        }
        if (view.index < mRemoteRenders.size()) {
            BubbleSortSubView(view);
        }
    }

    /**
     * Implements for AnyRTCViewEvents.
     */

    @Override
    public VideoRenderer OnRtcOpenLocalRender() {
        int size = GetVideoRenderSize();

        if (size == 0) {
            mLocalRender = new VideoView("localRender", mVideoView.getContext(), mRootEglBase, 0, 0, 0, 100, 100);
        } else {
            mLocalRender = new VideoView("localRender", mVideoView.getContext(), mRootEglBase, size, SUB_X, (100 - size * (SUB_HEIGHT + SUB_Y)), SUB_WIDTH, SUB_HEIGHT);
//            mLocalRender.mView.setZOrderOnTop(true);
            mLocalRender.mView.setZOrderMediaOverlay(true);
        }
        mVideoView.addView(mLocalRender.mLayout, -1);
        mLocalRender.mLayout.setPosition(mLocalRender.x, mLocalRender.y, mLocalRender.w, mLocalRender.h);
        mLocalRender.mView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
        mLocalRender.mRenderer = new VideoRenderer(mLocalRender.mView);
//        mLocalRender.tv_name.setVisibility(View.GONE);
        return mLocalRender.mRenderer;
    }

    @Override
    public void OnRtcRemoveLocalRender() {
        if (mLocalRender != null) {
            mLocalRender.close();
            mLocalRender.mRenderer = null;
            mVideoView.removeView(mLocalRender.mLayout);
            mLocalRender = null;
        }
    }


    /**
     * 根据模板更新视频界面的布局
     */
    private void updateVideoViewWith() {
            //平均大小模式
            int size = mRemoteRenders.size();
            if (size == 0) {
                mLocalRender.mLayout.setPosition(0, 0, 100, 100);
                mLocalRender.mView.requestLayout();
            } else if (size == 1) {
                int X = 50;
                int Y = 30;
                int WIDTH = 50;
                int HEIGHT = 50;
                Iterator<Map.Entry<String, VideoView>> iter = mRemoteRenders.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, VideoView> entry = iter.next();

                    VideoView render = entry.getValue();
                    mLocalRender.mLayout.setPosition(0, Y, WIDTH, HEIGHT);
                    mLocalRender.mView.requestLayout();
                    if (render.index == 1) {
                        render.mLayout.setPosition(X, Y, WIDTH, HEIGHT);
                        render.mView.requestLayout();
                    }
                }
            } else if (size == 2) {
                int X = 100/3;
                int Y = 35;
                int WIDTH = 100/3;
                int HEIGHT =30 ;

                Iterator<Map.Entry<String, VideoView>> iter = mRemoteRenders.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, VideoView> entry = iter.next();

                    VideoView render = entry.getValue();
                    mLocalRender.mLayout.setPosition(0, Y, WIDTH, HEIGHT);
                    mLocalRender.mView.requestLayout();
                    if (render.index == 1) {
                        render.mLayout.setPosition(X, Y, WIDTH, HEIGHT);
                        render.mView.requestLayout();
                    } else if (render.index == 2) {
                        render.mLayout.setPosition(X * (render.index % 3), Y, WIDTH + 1, HEIGHT);
                        render.mView.requestLayout();
                    }
                }
            } else if (size == 3) {
                int X = 50;
                int Y = 0;
                int WIDTH = 50;
                int HEIGHT =50 ;
                ;
                Iterator<Map.Entry<String, VideoView>> iter = mRemoteRenders.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, VideoView> entry = iter.next();

                    VideoView render = entry.getValue();
                    mLocalRender.mLayout.setPosition(0, Y, WIDTH, HEIGHT);
                    mLocalRender.mView.requestLayout();
                    if (render.index == 1) {
                        render.mLayout.setPosition(X, Y, WIDTH, HEIGHT);
                        render.mView.requestLayout();
                    } else if (render.index == 2) {
                        render.mLayout.setPosition(0, Y + HEIGHT, WIDTH, HEIGHT);
                        render.mView.requestLayout();
                    } else if (render.index == 3) {
                        render.mLayout.setPosition(X, Y + HEIGHT, WIDTH, HEIGHT);
                        render.mView.requestLayout();
                    }
                }
            } else if (size >=4) {
                int X = 100 / 3;
                int Y = 20;
                int WIDTH = 100 / 3;
                int HEIGHT =  30 ;
                Iterator<Map.Entry<String, VideoView>> iter = mRemoteRenders.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, VideoView> entry = iter.next();
                    VideoView render = entry.getValue();
                    mLocalRender.mLayout.setPosition(0, Y, WIDTH, HEIGHT);
                    mLocalRender.mView.requestLayout();
                    if (render.index % 3 == 2) {
                        render.mLayout.setPosition(X * (render.index % 3), Y + (HEIGHT * (render.index / 3)), WIDTH, HEIGHT);
                        render.mView.requestLayout();
                    } else {
                        render.mLayout.setPosition(X * (render.index % 3), Y + (HEIGHT * (render.index / 3)), WIDTH, HEIGHT);
                        render.mView.requestLayout();
                    }
                }
            }
    }

    private void screenChange() {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Iterator<Map.Entry<String, VideoView>> iter = screenRender.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, VideoView> entry = iter.next();
                TempVideoView.VideoView render = entry.getValue();
                render.mLayout.setPosition(0, 0, 100, 100);
                render.mView.requestLayout();
            }
        } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Iterator<Map.Entry<String, VideoView>> iter = screenRender.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, VideoView> entry = iter.next();
                TempVideoView.VideoView render = entry.getValue();
                render.mLayout.setPosition(0, 36, 100, 27);
                render.mView.requestLayout();
            }
        }
    }

    public void updateVideoView() {

        screenChange();

        /**
         * 1  ===42
         * 2 ===35
         *
         *3 ===27
         *
         * 4 === 20
         *
         *
         * 5 ===12
         *
         *
         * 6====5
         */
        int startPosition = (100 - SUB_WIDTH * mRemoteRenders.size()) / 2; //42  35  27  20  12  5
        int remotePosition;
        int index;
        Iterator<Map.Entry<String, VideoView>> iter = mRemoteRenders.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, VideoView> entry = iter.next();
            TempVideoView.VideoView render = entry.getValue();
            if (render.Fullscreen()) {
                render = mLocalRender;
                index = mLocalRender.index;
//                render.tv_name.setVisibility(View.GONE);
            } else {
//                render.tv_name.setVisibility(View.VISIBLE);
                index = render.index;
            }
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                render.y = 70;
            }else {
                render.y = 75;
            }
            if (index > 1) {
                remotePosition = (startPosition + (index - 1) * SUB_WIDTH) + ((index - 1) * 1);
            } else {
                remotePosition = startPosition + (index - 1) * SUB_WIDTH;
            }
            render.x = remotePosition;

            if (!render.Fullscreen()) {
                render.x = remotePosition;
            } else {
                mLocalRender.x = remotePosition;
            }
            render.mLayout.setPosition(remotePosition, render.y, SUB_WIDTH, SUB_HEIGHT);
            Log.d("小视频"+render.strPeerId,"x:"+remotePosition+"--y:"+render.y+"--w:"+render.w+"--h:"+render.h);
            render.mView.requestLayout();
        }
    }

    @Override
    public VideoRenderer OnRtcOpenRemoteRender(final String strRtcPeerId) {
//        VideoView remoteRender = mRemoteRenders.get(strRtcPeerId);
//        if (remoteRender == null) {
//            int size = GetVideoRenderSize();
//            if (size == 0) {
//                remoteRender = new VideoView(strRtcPeerId, mVideoView.getContext(), mRootEglBase, 0, 0, 0, 100, 100);
//            } else {
//                remoteRender = new VideoView(strRtcPeerId, mVideoView.getContext(), mRootEglBase, size, SUB_X, (100 - size * (SUB_HEIGHT + SUB_Y)), SUB_WIDTH, SUB_HEIGHT);
//                remoteRender.mView.setZOrderMediaOverlay(true);
//            }
//
//            mVideoView.addView(remoteRender.mLayout, 0);
//            remoteRender.mLayout.setPosition(remoteRender.x, remoteRender.y, remoteRender.w, remoteRender.h);
//            remoteRender.mView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
//            remoteRender.mRenderer = new VideoRenderer(remoteRender.mView);
//            mRemoteRenders.put(strRtcPeerId, remoteRender);
//            updateVideoView();
//        }
        return null;
    }

    public VideoRenderer OnRtcOpenRemoteRender(final String strRtcPeerId, String name) {
        VideoView remoteRender = mRemoteRenders.get(strRtcPeerId);
        if (remoteRender == null) {
            int size = GetVideoRenderSize();
            if (size == 0) {
                remoteRender = new VideoView(strRtcPeerId, mVideoView.getContext(), mRootEglBase, 0, 0, 0, 100, 100);
            } else {
                remoteRender = new VideoView(strRtcPeerId, mVideoView.getContext(), mRootEglBase, size, SUB_X, (100 - size * (SUB_HEIGHT + SUB_Y)), SUB_WIDTH, SUB_HEIGHT);
                remoteRender.mView.setZOrderMediaOverlay(true);
            }
            mVideoView.addView(remoteRender.mLayout, -1);
            remoteRender.mLayout.setPosition(remoteRender.x, remoteRender.y, remoteRender.w, remoteRender.h);
            remoteRender.mView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
            remoteRender.mRenderer = new VideoRenderer(remoteRender.mView);
//            remoteRender.tv_name.setText(name);
            mRemoteRenders.put(strRtcPeerId, remoteRender);
            updateVideoView();
//            updateVideoViewWith();
        }
        return remoteRender.mRenderer;
    }


    @Override
    public void OnRtcRemoveRemoteRender(String peerId) {
        VideoView remoteRender = mRemoteRenders.get(peerId);
        if (remoteRender != null) {

            if (remoteRender.Fullscreen()) {
                SwitchViewToFullscreen(mLocalRender, remoteRender);
            }
            if (mRemoteRenders.size() > 1 && remoteRender.index <= mRemoteRenders.size()) {
                BubbleSortSubView(remoteRender);
            }
            remoteRender.close();
            mVideoView.removeView(remoteRender.mLayout);
            mRemoteRenders.remove(peerId);
            updateVideoView();
//            updateVideoViewWith();
        }


    }



}

package org.anyrtc.lib_meeting.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.anyrtc.whiteboard.imageloader.ImageLoader;

/**
 * Created by liuxiaozhong on 2017-06-09.
 */

public class DocImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        RequestManager requestManager= Glide.with(context);
        requestManager.load(path).
               into(imageView);
    }
}

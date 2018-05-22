package org.anyrtc.lib_meeting.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.yanzhenjie.album.AlbumFile;

/**
 * Created by liuxiaozhong on 2017-06-09.
 */

public class AlbumLoader implements com.yanzhenjie.album.AlbumLoader {

    @Override
    public void load(ImageView imageView, AlbumFile albumFile) {
            load(imageView,albumFile.getPath());
    }

    @Override
    public void load(ImageView imageView, String url) {
        RequestManager requestManager= Glide.with(imageView.getContext());
        requestManager.load(url).
                into(imageView);
    }
}

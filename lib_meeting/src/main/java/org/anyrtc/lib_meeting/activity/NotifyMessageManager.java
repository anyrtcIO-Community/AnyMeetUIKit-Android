package org.anyrtc.lib_meeting.activity;

import java.util.List;

/**
 * Created by liuxiaozhong on 2018/5/11.
 */
public class NotifyMessageManager {
    private static NotifyMessageManager instance;

    public static NotifyMessageManager getInstance() {
        if (instance == null)
            synchronized (NotifyMessageManager.class) {
                if (instance == null)
                    instance = new NotifyMessageManager();
            }
        return instance;
    }

    private NotifyMessageManager() {
    }

    private imageChoosedNotify imageChoosedNotify;

    private imageUploadFinishNotify imageUploadFinishNotify;

    public void setImageChoosedNotify(imageChoosedNotify imageChoosedNotify) {
        this.imageChoosedNotify = imageChoosedNotify;
    }


    public void sendImagePathMessage(List<String> imagePath) {
        imageChoosedNotify.imageList(imagePath);
    }

    public void setImageUploadFinish(imageUploadFinishNotify imageUploadFinish){
        this.imageUploadFinishNotify=imageUploadFinish;
    }
    public void sendImageUploadFinishMessage(List<String> imageUrl,String fileId){
        imageUploadFinishNotify.imageUrlList(imageUrl,fileId);
    }



    public interface imageChoosedNotify {
        public void imageList(List<String> imagePath);
    }

    public interface imageUploadFinishNotify {
        public void imageUrlList(List<String> imagePath,String fileId);
    }
}

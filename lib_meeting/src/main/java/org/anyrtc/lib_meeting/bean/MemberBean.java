package org.anyrtc.lib_meeting.bean;

/**
 * Created by liuxiaozhong on 2017/12/21.
 */

public class MemberBean {
    public String Id="";
    public String name="";
    public String peerId="";
    public String HostId="";
    public String icon="";
    public boolean isOpenAudio=true;
    public boolean isOpenVideo=true;
    public boolean isHost;
    public boolean isSelf=false;
    public int  dType;

    public int getdType() {
        return dType;
    }

    public void setdType(int dType) {
        this.dType = dType;
    }

    public MemberBean() {
    }

    public String getHostId() {
        return HostId;
    }

    public void setHostId(String hostId) {
        HostId = hostId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeerId() {
        return peerId;
    }

    public void setPeerId(String peerId) {
        this.peerId = peerId;
    }

    public boolean isOpenAudio() {
        return isOpenAudio;
    }

    public void setOpenAudio(boolean openAudio) {
        isOpenAudio = openAudio;
    }

    public boolean isOpenVideo() {
        return isOpenVideo;
    }

    public void setOpenVideo(boolean openVideo) {
        isOpenVideo = openVideo;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

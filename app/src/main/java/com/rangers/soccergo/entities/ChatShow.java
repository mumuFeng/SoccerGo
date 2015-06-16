package com.rangers.soccergo.entities;

import java.sql.Time;

/**
 * 会话列表显示类
 * Created by Infocenter on 2015/5/22.
 */
public class ChatShow {
    private int c_id;
    private String chatTargetName ;         //对象昵称
    private String recentlyMessage ;
    private String recentlyTime;
    private int unreadMegs ;

    public ChatShow(int c_id, String chatTargetName, String recentlyMessage, String recentlyTime, int unreadMegs) {
        this.c_id = c_id;
        this.chatTargetName = chatTargetName;
        this.recentlyMessage = recentlyMessage;
        this.recentlyTime = recentlyTime;
        this.unreadMegs = unreadMegs;
    }

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public String getChatTargetName() {
        return chatTargetName;
    }

    public void setChatTargetName(String chatTargetName) {
        this.chatTargetName = chatTargetName;
    }

    public String getRecentlyMessage() {
        return recentlyMessage;
    }

    public void setRecentlyMessage(String recentlyMessage) {
        this.recentlyMessage = recentlyMessage;
    }

    public String getRecentlyTime() {
        return recentlyTime;
    }

    public void setRecentlyTime(String recentlyTime) {
        this.recentlyTime = recentlyTime;
    }

    public int getUnreadMegs() {
        return unreadMegs;
    }

    public void setUnreadMegs(int unreadMegs) {
        this.unreadMegs = unreadMegs;
    }
}

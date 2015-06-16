package com.rangers.soccergo.entities;

import java.sql.Time;

/**
 * 本地存放的聊天实体类
 * @param _id ：外键-对应chatlist列表中的会话id
 * @param UserId ：用户id
 * @param message：消息内容
 * @param time：消息发送时间
 * Created by Infocenter on 2015/5/21.
 */
public class ChatMessage {
    private int m_id ;
    private int c_id;
    private String userId;
    private String message;
    private String time;
    private static final int READ = 0;
    private static final int NOTREAD = 1;
    private int isRead ;//该消息是否已读，用于统计所属会话未读数 1-未读 0-已读

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public ChatMessage(int c_id, String userId, String message, String time, int isRead) {
        this.c_id = c_id;
        this.userId = userId;
        this.message = message;
        this.time = time;
        this.isRead = isRead;
    }
}

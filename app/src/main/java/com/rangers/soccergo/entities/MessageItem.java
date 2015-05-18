package com.rangers.soccergo.entities;

/**
 * Created by Infocenter on 2015/5/13.
 * Desc: 会话列表实体类
 * Team: Rangers
 */
public class MessageItem extends Base {
    //private String uerid;
    private int account;    //未读消息数
    private String name;    //会话对方的名称或者群聊名称
    private String content; //最近的一条message
    private String time;    //最近的消息时间
    private boolean isRead; //是否已读

    public MessageItem(int account, String name, String content, String time, boolean isRead) {
        this.account = account;
        this.name = name;
        this.content = content;
        this.time = time;
        this.isRead = isRead;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
}

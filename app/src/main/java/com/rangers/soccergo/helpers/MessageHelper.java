package com.rangers.soccergo.helpers;


/**
 * 消息帮助类,单例模式(饿汉模式)
 *  @param tag:消息是否已经接受的标志，false-此条消息未接收处理  ture-消息已经处理
 *  @param msg:接受到的消息
 *  @param id :判断所属会话的唯一id
 *  @param sender:消息发送者
 *  @param room:群聊的会话名称（只有群聊是才有用）
 *  @param time:消息发送时间
 * Created by Infocenter on 2015/5/18.
 */
public class MessageHelper {
    private boolean Received = true; //
    private String msg = null;
    private String id = null;
    private String sender = null;
    private String room = null;
    private long time = 0;

    public boolean isReceived() {
        return Received;
    }
    public void setReceived(boolean received) {
        Received = received;
    }
    public String getSender() {  return sender;  }

    public void setSender(String sender) {  this.sender = sender;  }

    public String getMsg() { return msg;   }

    public void setMsg(String msg) { this.msg = msg;  }

    public String getId() { return id;   }

    public void setId(String id) {  this.id = id;   }

    public String getRoom() {  return room;  }

    public void setRoom(String room) {   this.room = room;    }

    public long getTime() {  return time;  }

    public void setTime(long time) { this.time = time;   }

    private static  MessageHelper messageHelper = new MessageHelper();
    private MessageHelper(){}
    public synchronized static MessageHelper getInstance(){
        return messageHelper;
    }
}

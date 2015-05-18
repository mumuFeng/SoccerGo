package com.rangers.soccergo.helpers;

/**
 * 消息帮助类,单例模式
 *  @param msg:接受到的消息
 *  @param id :判断所属会话的唯一id
 *  @param name:消息发送者
 *  @param room:群聊的会话名称（只有群聊是才有用）
 *  @param time:消息发送时间
 * Created by Infocenter on 2015/5/18.
 */
public class MessageHelper {
    private String msg = null;
    private String id = null;
    private String name = null;
    private String room = null;
    private long time = 0;

    public String getMsg() { return msg;   }

    public void setMsg(String msg) { this.msg = msg;  }

    public String getId() { return id;   }

    public void setId(String id) {  this.id = id;   }

    public String getName() {  return name;   }

    public void setName(String name) {  this.name = name;    }

    public String getRoom() {  return room;  }

    public void setRoom(String room) {   this.room = room;    }

    public long getTime() {  return time;  }

    public void setTime(long time) { this.time = time;   }

    private static  MessageHelper messageHelper = null;
    private MessageHelper(){}
    public static MessageHelper getInstance(){
        if(messageHelper == null){
            messageHelper = new MessageHelper();
        }
        return messageHelper;
    }
}

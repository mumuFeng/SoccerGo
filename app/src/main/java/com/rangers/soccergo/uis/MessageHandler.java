package com.rangers.soccergo.uis;

import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.rangers.soccergo.helpers.MessageHelper;

/**
 *  消息接收处理
 *  @param msg:接受到的消息
 *  @param id :判断所属会话的唯一id
 *  @param name:消息发送者
 *  @param room:群聊的会话名称（只有群聊是才有用）
 *  @param time:消息发送时间
 * Created by Infocenter on 2015/5/6.
 */
public class MessageHandler extends AVIMMessageHandler{
    private String msg = null;
    private String id = null;
    private String name = null;
    private String room = null;
    private long time = 0;
/*    public String getMsg() {
        return msg;
    }
    public String getId() {
        return id;
    }*/

    @Override
    public void onMessage(AVIMMessage messagetest, AVIMConversation conversationtest, AVIMClient clienttest) {
        super.onMessage(messagetest, conversationtest, clienttest);
        //接收到消息并进行相关处理
        //判断当前会话窗口，根据用户，更新当前会话窗口还是会话列表fragment
        id = conversationtest.getConversationId();
        room = conversationtest.getName();
        msg = messagetest.getContent();
        name = messagetest.getFrom();
        time = messagetest.getTimestamp();
        inject(id,msg,name,room,time);
        update();
        Log.d("这里消息的message是*****",msg);
        Log.d("这里消息的client是******",clienttest.getClientId());
        Log.d("这里消息的conversation**",conversationtest.getConversationId()+conversationtest.getCreator()+conversationtest.getMembers().get(0)+conversationtest.getMembers().get(1));
        Log.d("*************************","");
    }

    private void inject(String id, String msg, String name, String room, long time) {
        //将获取到的消息传入messagehelper类
        MessageHelper msghelper = MessageHelper.getInstance();  //这个帮助类可以考虑用数组处理？？？
        msghelper.setId(id);
        msghelper.setName(name);
        msghelper.setMsg(msg);
        msghelper.setTime(time);
        msghelper.setRoom(room);
    }


    private void update() {
        //判断更新聊天
        if(true){
            //此消息来自于当前活动聊天会话并显示到当前activity
        }else{
            //更新messagelist这个fragment
        }
    }
}

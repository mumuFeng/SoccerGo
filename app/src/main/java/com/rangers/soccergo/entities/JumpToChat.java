package com.rangers.soccergo.entities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import com.rangers.soccergo.activities.ChatActivity;
import com.rangers.soccergo.activities.MainActivity;
import com.rangers.soccergo.dao.ChatItemDao;

/**
 * 跳转到聊天界面接口
 * Created by Infocenter on 2015/5/21.
 */
public class JumpToChat {
    public final static int NO_CHATID=0;
    public JumpToChat() {
    }
    public static void jump(Activity activity,int chatId){
        /**
         * 通过chatList会话列表跳转到聊天窗口
         */
        ChatItemDao chatItemDao = new ChatItemDao(activity);
        String chatTargetId = chatItemDao.getChatTargetIdById(chatId);
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra("chatId",chatId);
        intent.putExtra("chatTargetId",chatTargetId);

        activity.startActivity(intent);
    }

    public static void jump(Activity activity,String userId,String chatTargetId){
        /**
         通过点击用户名等外部路径进入聊天界面接口
         */
        ChatItemDao chatItemDao = new ChatItemDao(activity);
        int chatId = chatItemDao.getID(userId,chatTargetId);
        if(chatId==NO_CHATID){
            //会话列表中不存在该对象的会话
            //添加新会话
            String chatName = "聊天";
            ChatItem chatItem = new ChatItem(chatName,userId,chatTargetId,ChatItem.CHAT_SINGLE,0);
            chatItemDao.add(chatItem);
            chatId = chatItemDao.getID(userId,chatTargetId);
        }
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra("chatId",chatId);
        intent.putExtra("chatTargetId",chatTargetId);
        activity.startActivity(intent);
    }

    public static void jump(Activity activity,String userId,String[] users[]){
        //群聊接口
    }
}

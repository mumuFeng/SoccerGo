package com.rangers.soccergo.helpers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.rangers.soccergo.App;
import com.rangers.soccergo.R;
import com.rangers.soccergo.activities.ChatActivity;
import com.rangers.soccergo.activities.MainActivity;
import com.rangers.soccergo.dao.ChatItemDao;
import com.rangers.soccergo.dao.ChatMessageDao;
import com.rangers.soccergo.entities.ChatItem;
import com.rangers.soccergo.entities.ChatMessage;
import com.rangers.soccergo.entities.JumpToChat;
import com.rangers.soccergo.entities.User;
import com.rangers.soccergo.fragments.MessageListFragment;
import com.rangers.soccergo.uis.MessageHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rangers.soccergo.activities.ChatActivity.*;

/**
 * @param isOpen：是否连接聊天服务器
 * Created by Infocenter on 2015/5/29.
 */
public class ChatConversationHelper {
    private static AVIMClient avimClient;
    private static AVIMConversation avimConversation;
    public final static String USERID = App.getCurrentUser().getMobilePhoneNumber();
    public final static int CHAT_SINGLE=0;                  //单聊
    public final static int CHAT_GROUP=1;                   //群聊
    public final static int CLIENT_OPEN = 1;                //成功连接聊天服务器
    public final static int CLIENT_NOTOPEN = 0;             //连接失败
    public static int isOpen = CLIENT_NOTOPEN;
    public static int nofyID = 0;
    public static Context context;
    private ChatConversationHelper(){}

    public static void open(String UserID, final Context Newcontext){
        /**
         *登陆初始化之后调用此方法，用于开始接受消息
         *网络连接之后也应该立即调用此方法
         *用户登录服务器，此处之后便可以接收消息，成功之后创建会话可以聊天发送消息
         */
        avimClient = AVIMClient.getInstance(UserID);
        AVIMMessageManager.registerDefaultMessageHandler(new MessageHandler());
        avimClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVException e) {
                if (null != e) {
                    // 出错了，可能是网络问题无法连接 LeanCloud 云端，请检查网络之后重试。
                    // 此时聊天服务不可用。
                    e.printStackTrace();
                    Toast.makeText(context, "打开会话出错", Toast.LENGTH_SHORT).show();
                    isOpen = CLIENT_NOTOPEN;
                }else {
                    context = Newcontext;
                    isOpen = CLIENT_OPEN;
                    BeginReceiveMessages();
                }
            }
        });
    }

    private static void BeginReceiveMessages() {
        Thread t = new Thread(new Runnable(){
            public void run(){
                while(true) {
                    while (true) {
                        if (!MessageHelper.getInstance().isReceived()) {
                            break;
                        }
                    }
                        MessageHelper messageHelper = MessageHelper.getInstance();
                        //消息未接收则进行消息接收处理
                        String message = messageHelper.getMsg();
                        String chatTargetId = messageHelper.getSender();
                        String roomName = messageHelper.getRoom();
                        Long time = messageHelper.getTime();
                        //通知处理部分
                        //添加到数据库部分
                        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
                        if(am.getRunningTasks(1).get(0)!=null){
                            String  activityFullName = am.getRunningTasks(1).get(0).topActivity.getShortClassName();
                            String activityName = activityFullName.substring(activityFullName.lastIndexOf(".")+1);
                            if(activityName.equals("ChatActivity")&&chatTargetId.equals(ChatActivity.chatTargetId)){
                                addTextToList(message, OTHER);
                                //同时存入本地数据库
                                Date now = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
                                String dateAdd = dateFormat.format(now);
                                ChatItemDao chatItemDao = new ChatItemDao(context);
                                int chatId= chatItemDao.getID(USERID,chatTargetId);
                                ChatMessage chatMessageAdd = new ChatMessage(chatId, chatTargetId, message, dateAdd, READ);
                                ChatMessageDao chatMessageDao = new ChatMessageDao(context);
                                chatMessageDao.add(chatMessageAdd);
                                ChatActivity.reFresh();
                                messageHelper.setReceived(true);
                                continue;
                            }
                        }

                        showNotification(context, chatTargetId, message);
                        MessageListFragment.reFresh();
                        messageHelper.setReceived(true);
                    }
                }
            });
        t.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void showNotification(Context context,String chatTargetID,String message) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        Intent intent = new Intent(context,ChatActivity.class);
        ChatItemDao chatItemDao = new ChatItemDao(context);
        int chatId = chatItemDao.getID(USERID,chatTargetID);
        if(chatId== JumpToChat.NO_CHATID){
            //会话列表中不存在该对象的会话
            //添加新会话
            String chatName = "聊天";
            ChatItem chatItem = new ChatItem(chatName,USERID,chatTargetID,ChatItem.CHAT_SINGLE,0);
            chatItemDao.add(chatItem);
            chatId= chatItemDao.getID(USERID,chatTargetID);
        }
        intent.putExtra("chatId",chatId);
        intent.putExtra("chatTargetId",chatTargetID);
        /**
         * 将此条消息存入本地消息数据库
         */
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
        String dateAdd = dateFormat.format(now);
        ChatMessage chatMessageAdd = new ChatMessage(chatId,chatTargetID,message,dateAdd, READ);
        ChatMessageDao chatMessageDao = new ChatMessageDao(context);
        chatMessageDao.add(chatMessageAdd);


        PendingIntent pendingIntent= PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(context);
        mbuilder.setContentTitle(chatTargetID)//设置通知栏标题
                .setContentText(message)
                .setContentIntent(pendingIntent) //设置通知栏点击意图
                .setTicker(chatTargetID+" 发来了一条消息") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
        Notification notification = mbuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        mbuilder.setContentIntent(pendingIntent);
        notificationManager.notify(chatId,notification);
    }

    public static AVIMConversation createConversation(String UserId,String chatTargetId){
        //根据用户名和聊天对象创建会话
        if(isOpen==CLIENT_NOTOPEN){
            return null;
        }
        List<String> clientIds = new ArrayList<String>();
        clientIds.add(UserId);
        clientIds.add(chatTargetId);
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put("type", CHAT_SINGLE);//单聊
        avimClient.createConversation(clientIds,attr,new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation conversation, AVException e) {
                if (null != conversation) {
                    avimConversation = conversation;
                }
            }
        });
        return avimConversation;
    }
}

package com.rangers.soccergo.fragments;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.rangers.soccergo.R;
import com.rangers.soccergo.activities.ChatActivity;
import com.rangers.soccergo.activities.MainActivity;
import com.rangers.soccergo.dao.ChatItemDao;
import com.rangers.soccergo.entities.ChatItem;
import com.rangers.soccergo.entities.ChatMessage;
import com.rangers.soccergo.entities.ChatShow;
import com.rangers.soccergo.entities.JumpToChat;
import com.rangers.soccergo.helpers.ChatConversationHelper;
import com.rangers.soccergo.helpers.MessageHelper;
import com.rangers.soccergo.uis.ChatListAdapter;
import com.rangers.soccergo.uis.MessageHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ChatListFragment
 * Desc: 会话列表Fragment
 * Team: Rangers
 * Date: 2015/4/18
 * Time: 16:08
 * Created by: Wooxxx
 */
public class MessageListFragment extends BaseFragment {
    private static int[] chatIds;
    static ListView listView;  //会话列表listview
    private static List<ChatShow> chatShowList= new ArrayList<ChatShow>();        //会话实体类list
    private  Context context;
    private static ChatItemDao chatItemDao  ;
    private static ChatListAdapter adapter;
    private static Handler handler = new Handler();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = this.getActivity();
        chatItemDao = new ChatItemDao(context);
    }

    @Nullable
    @Override
    //初始化fragment视图
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.fragment_message_list,
                container,
                false
        );
        return super.onCreateView(inflater, container, savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //chatShowList = new ArrayList<ChatShow>();
        onloadlist();//刷新会话列表
        listView = (ListView) this.getActivity().findViewById(R.id.chat_list);
        adapter = new ChatListAdapter(this.getActivity(), chatShowList);
        listView.setAdapter(adapter);
        //unregisterReceiver(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                //打开聊天页面
                ChatShow chatShow = chatShowList.get(position);
                int chatId = chatShow.getC_id();
                JumpToChat.jump(getActivity(), chatId);
            }
        });
    }

    private static void onloadlist() {
        //加载会话列表
        //chatItemDao.add(new ChatItem("",ChatConversationHelper.USERID,"13558970390",1,0));
        chatIds = chatItemDao.getChatIds(ChatConversationHelper.USERID);//获取本地存储的会话ids
        if(chatIds!=null){
            chatShowList.clear();
            for(int i=0;i<chatIds.length;i++){
                chatShowList.add(chatItemDao.getChatShowById(chatIds[i]));
            }
        }
    }

    public static void reFresh(){
        handler.post(refresh);
    }

    static final Runnable refresh = new Runnable() {
        public void run() {
            onloadlist();
            adapter.notifyDataSetChanged();
        }
    };

}

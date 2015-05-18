package com.rangers.soccergo.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.rangers.soccergo.R;
import com.rangers.soccergo.entities.MessageItem;
import com.rangers.soccergo.uis.MessageListAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * MessageListFragment
 * Desc: 消息列表Fragment
 * Team: Rangers
 * Date: 2015/4/18
 * Time: 16:08
 * Created by: Wooxxx
 */
public class MessageListFragment extends BaseFragment {
    ListView listView;  //会话列表listview
    List<MessageItem> MessageList= new ArrayList<MessageItem>();        //会话实体类list
    MessageItem ms1 = new MessageItem(1,"mumu","hello","12:22",true);   //测试数据
    MessageItem ms2 = new MessageItem(2,"messi","hi","9:55",true);      //测试数据
    MessageItem ms3 = new MessageItem(0,"kobe","你好","23:55",true);     //测试数据
    String[] mes;
    @Nullable
    @Override
    //初始化fragment视图
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.fragment_message_list,
                container,
                false
        );
        MessageList.add(ms1);//测试数据
        MessageList.add(ms2);//测试数据
        MessageList.add(ms3);//测试数据
        onloadlist();//加载会话列表

        Toast.makeText(getActivity(),"fragment初始化",Toast.LENGTH_SHORT).show();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //适配器
        Iterator it=MessageList.iterator();
        /*if(MessageList!=null && MessageList.size()!=0){
            while(it.hasNext()){
                MessageItem re=(MessageItem) it.next();
                if(re.getAccount()==Integer.parseInt(mes[0])){
                    MessageList.remove(re);
                }
            }
        }*/
        //MessageList.add(new RecentEntity(5, Integer.parseInt(mes[0]), mes[0]+"", mes[1],mes[2], false));
        listView = (ListView) this.getActivity().findViewById(R.id.chat_list);
        listView.setAdapter(new MessageListAdapter(this.getActivity(), MessageList));
        //unregisterReceiver(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                //打开聊天页面
                Toast.makeText(getActivity(),"创建成功",Toast.LENGTH_SHORT).show();
                gochating();//准备进入聊天界面

            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    private void gochating() {
        //进入聊天界面
        /*Intent intent=new Intent(getActivity(),ChatActivity.class);
        intent.putExtra("converstationId", Integer.parseInt(mes[0]));
        intent.putExtra("nick", "");
        startActivity(intent);*/
    }

    private void onloadlist() {
        //加载会话列表
    }
}

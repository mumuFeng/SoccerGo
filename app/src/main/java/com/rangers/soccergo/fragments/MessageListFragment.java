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
import com.rangers.soccergo.entities.MessageShow;
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
    List<MessageShow> MessageList= new ArrayList<MessageShow>();   //会话实体类list
    MessageShow ms1 = new MessageShow(1,"mumu","hello","12:22",true);
    MessageShow ms2 = new MessageShow(2,"messi","hi","9:55",true);
    MessageShow ms3 = new MessageShow(0,"kobe","你好","23:55",true);
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
        MessageList.add(ms1);
        MessageList.add(ms2);
        MessageList.add(ms3);
        Toast.makeText(getActivity(),"fragment初始化",Toast.LENGTH_SHORT).show();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //适配器
        Iterator it=MessageList.iterator();
        /*if(MessageList!=null && MessageList.size()!=0){
            while(it.hasNext()){
                MessageShow re=(MessageShow) it.next();
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
                /*Intent intent=new Intent(RecentActivity.this,ChatActivity.class);
                intent.putExtra("account", Integer.parseInt(mes[0]));
                intent.putExtra("nick", "");
                startActivity(intent);*/
            }
        });
        super.onActivityCreated(savedInstanceState);
    }
}

package com.rangers.soccergo.uis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rangers.soccergo.R;
import com.rangers.soccergo.entities.ChatItem;
import com.rangers.soccergo.entities.ChatShow;

import java.util.List;

/**
 * 会话列表适配器
 * Created by Infocenter on 2015/5/13.
 */
public class ChatListAdapter extends BaseAdapter {
    private Context context;
    private List<ChatShow> list;
    LayoutInflater inflater;

    public ChatListAdapter(Context context, List<ChatShow> list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup root) {
        convertView = inflater.inflate(R.layout.item_chat_listview, null);
        ImageView avatar=(ImageView) convertView.findViewById(R.id.iv_avatar_r);//头像
        TextView nick=(TextView) convertView.findViewById(R.id.tv_nick_r);//用户昵称
        TextView content=(TextView) convertView.findViewById(R.id.tv_chat_content_r);//消息
        //ImageView isRead=(ImageView) convertView.findViewById(R.id.iv_tip_mes_r);//未读消息
        TextView time=(TextView) convertView.findViewById(R.id.tv_time_r);//消息时间

        ChatShow re=list.get(position);
        nick.setText(re.getChatTargetName().toString());
        content.setText(re.getRecentlyMessage().toString());
        avatar.setImageResource(R.drawable.user1);
        //isRead.setImageResource(R.drawable.tips_message);
        time.setText(re.getRecentlyTime());

        return convertView;
    }
    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
}

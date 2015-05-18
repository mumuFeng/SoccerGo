package com.rangers.soccergo.uis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rangers.soccergo.R;
import com.rangers.soccergo.entities.MessageItem;

import java.util.List;

/**
 * 会话列表适配器
 * Created by Infocenter on 2015/5/13.
 */
public class MessageListAdapter extends BaseAdapter {
    private Context context;
    private List<MessageItem> list;
    LayoutInflater inflater;

    public MessageListAdapter(Context context, List<MessageItem> list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup root) {
        convertView = inflater.inflate(R.layout.item_chat_listview, null);

        ImageView avatar=(ImageView) convertView.findViewById(R.id.iv_avatar_r);
        TextView nick=(TextView) convertView.findViewById(R.id.tv_nick_r);
        TextView content=(TextView) convertView.findViewById(R.id.tv_chat_content_r);
        ImageView isRead=(ImageView) convertView.findViewById(R.id.iv_tip_mes_r);
        TextView time=(TextView) convertView.findViewById(R.id.tv_time_r);

        MessageItem re=list.get(position);
        nick.setText(re.getName().toString());
        content.setText(re.getContent().toString());
        //isRead.setImageResource(R.drawable.tips_message);
        time.setText(re.getTime());

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

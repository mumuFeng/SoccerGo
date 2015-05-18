package com.rangers.soccergo.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.rangers.soccergo.R;
import com.rangers.soccergo.helpers.MessageHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天会话主页面
 * @param avimConversation：leancloud会话类
 * @param message:leancloud消息类
 * @param chatListView:聊天列表视图
 * @param mywords:我需要发送的消息
 * @param MyChatAdapter:自定义的聊天视图容器
 * @param adapterControlIds[]:adapter布局文件中每个控件的id
 * @param adapterLayouts[]:adapter的ListView中每一项的布局文件
 * @author  fj
 * @time   2015-5-18 修改
 */
public class ChatActivity extends ActionBarActivity {

    ArrayList<HashMap<String,Object>> chatList=null;
    AVIMMessage message = new AVIMMessage();
    String[] adapterMapKeys={"image","text"};//adapter列表里每一个Map的键
    int[] adapterControlIds={R.id.chatlist_image_me,R.id.chatlist_text_me,R.id.chatlist_image_other,R.id.chatlist_text_other};//adapter布局文件中每个控件的id
    int[] adapterLayouts={R.layout.chat_me,R.layout.chat_other};//adapter的ListView中每一项的布局文件
    public final static int OTHER=1;    //消息来自其他人
    public final static int ME=0;       //消息来自我自己
    protected ListView chatListView=null;
    protected Button chatSendButton=null;
    protected EditText editText=null;
    protected String mywords = null;
    protected AVIMConversation avimConversation = null;
    protected MyChatAdapter adapter=null;

    private int chatType = 0;//0:一对一单聊 1：群聊
    private String chatTargetId = null;//聊天对象id
    private String userId = null;//用户自身id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        chatType = intent.getExtras().getInt("chatType");
        chatTargetId = intent.getExtras().getString("chatTargetId");

        GetMessage getMessage = new GetMessage();
        getMessage.start();//执行线程，接收消息

        /**test
        利用json的方法传递没有继承序列化接口的对象，这里是接受端*
        String msg = getIntent().getExtras().getString("as");
        User u = JSON.parseObject(msg,User.class);
        Toast.makeText(ChatMain.this,u.getName(),Toast.LENGTH_LONG).show();
        String name = getIntent().getExtras().getString("conversation");
        String name  = JSON.parseObject(msg,AVIMClient.class);*/
        chatList=new ArrayList<HashMap<String,Object>>();
        String conid = null;
        conid = findconverstationId(userId,chatTargetId,chatType);
        if(conid != null){
            //从服务器上获取此会话对象converstation
            avimConversation = getConverstation(conid);
            //聊天记录，初始化
            initChatList(conid);
        }else{

            OncreateConverstation();
        }
        addTextToList("你是哪个", OTHER);
        addTextToList("你猜呢？\n  ^_^", ME);
        addTextToList("爱说不说", OTHER);
        addTextToList("那就不说，拜拜！", ME);

        initChatList(conid);//初始化本地聊天记录

        chatSendButton=(Button)findViewById(R.id.chat_bottom_sendbutton);//发送消息按钮
        editText=(EditText)findViewById(R.id.chat_bottom_edittext);//消息编辑框
        chatListView=(ListView)findViewById(R.id.chat_list);//聊天记录显示界面（正中间最大哪一块）
        adapter=new MyChatAdapter(this,chatList,adapterLayouts,adapterMapKeys,adapterControlIds);//界面适配器

        chatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /**
                 * 这是一个发送消息的监听器，注意如果文本框中没有内容，那么getText()的返回值可能为
                 * null，这时调用toString()会有异常！所以这里必须在后面加上一个""隐式转换成String实例
                 * ，并且不能发送空消息。
                 */
                mywords=(editText.getText()+"").toString();

                if(mywords.length()==0)
                    return;
                editText.setText("");
                message.setContent(mywords);//发送消息到服务器
                sendmessage(mywords);//发送消息更新在聊天界面并存入本地手机
                /**
                 * 更新数据列表，并且通过setSelection方法使ListView始终滚动在最底端
                 */
                adapter.notifyDataSetChanged();//
                chatListView.setSelection(chatList.size()-1);//???????
            }
        });

        chatListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private AVIMConversation getConverstation(String conid) {
        //从服务器上查询返回该AVIMConversation对象
        return null;
    }

    private String findconverstationId(String userId, String chatTargetId, int chatType) {
        //查询服务器是否已经存在该会话，有则返回会话id，否则返回null
        return null;
    }

    private void initChatList(String conid) {
        //读取本地聊天记录，更新聊天activity
    }

    private void sendmessage(String mywords) {
            AVIMMessage message = new AVIMMessage();
            message.setContent(mywords);
            avimConversation.sendMessage(message,new AVIMConversationCallback() {
                @Override
                public void done(AVException e) {
                    if (null != e) {
                        // 出错了。。。
                        e.printStackTrace();
                        Toast.makeText(ChatActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChatActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                        addTextToList(mywords,ME);
                    }
                }
            });
        }

    protected void OncreateConverstation() {
            final AVIMClient client = AVIMClient.getInstance(userId);
            /*//显示接收到的消息
            TakeMessages takeMessages = TakeMessages.getInstance();
            takeWords = takeMessages.getMessage();
            addTextToList(takeWords,ME);*/
                client.open(new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient avimClient, AVException e) {
                        if (null != e) {
                            // 出错了，可能是网络问题无法连接 LeanCloud 云端，请检查网络之后重试。
                            // 此时聊天服务不可用。错误处理
                            e.printStackTrace();
                            Toast.makeText(ChatActivity.this,"网络无法连接~",Toast.LENGTH_LONG).show();

                        } else {
                            // 成功登录，可以开始进行聊天了（假设为 MainActivity）。
                            List<String> clientIds = new ArrayList<String>();
                            clientIds.add(chatTargetId);
                            Map<String, Object> attr = new HashMap<String, Object>();
                            attr.put("type", chatType);
                            client.createConversation(clientIds, attr, new AVIMConversationCreatedCallback() {
                                @Override
                                public void done(AVIMConversation conversation, AVException e) {
                                    if (null != conversation) {
                                        avimConversation = conversation;
                                        //上传avimConverstation到服务器
                                        //本地保存一份会话记录
                                    }
                                    else{
                                        Toast.makeText(ChatActivity.this,"聊天连接失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
        }

    protected void addTextToList(String text, int who) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("person", who);
            map.put("image", who == ME ? R.drawable.user2 : R.drawable.user1);
            map.put("text", text);
            chatList.add(map);
            //添加存入本地的方法
        }

    private class MyChatAdapter extends BaseAdapter {

            Context context = null;
            ArrayList<HashMap<String, Object>> chatList = null;
            int[] layout;
            String[] from;
            int[] to;

            public MyChatAdapter(Context context,
                                 ArrayList<HashMap<String, Object>> chatList, int[] layout,
                                 String[] from, int[] to) {
                super();
                this.context = context;
                this.chatList = chatList;
                this.layout = layout;
                this.from = from;
                this.to = to;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return chatList.size();
            }

            @Override
            public Object getItem(int arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(this.context,"++++++",Toast.LENGTH_LONG).show();
                return null;
            }

            @Override
            public long getItemId(int position) {
                // TODO Auto-generated method stub
                return position;
            }

            class ViewHolder {
                public ImageView imageView = null;
                public TextView textView = null;

            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                ViewHolder holder = null;
                int who = (Integer) chatList.get(position).get("person");
                convertView = LayoutInflater.from(context).inflate(
                        layout[who == ME ? 0 : 1], null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(to[who * 2 + 0]);
                holder.textView = (TextView) convertView.findViewById(to[who * 2 + 1]);
                holder.imageView.setBackgroundResource((Integer) chatList.get(position).get(from[0]));
                holder.textView.setText(chatList.get(position).get(from[1]).toString());
                return convertView;
            }
        }

    private class GetMessage extends Thread{
        public GetMessage(){
        }
        @Override
        public void run() {
            while(true){
                MessageHelper messageHelper = MessageHelper.getInstance();
                String message = messageHelper.getMsg();
            }
        }
    }
}

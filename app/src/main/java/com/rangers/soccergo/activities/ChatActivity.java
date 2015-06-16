package com.rangers.soccergo.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
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
import com.rangers.soccergo.dao.ChatMessageDao;
import com.rangers.soccergo.entities.ChatMessage;
import com.rangers.soccergo.helpers.ChatConversationHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class ChatActivity extends Activity {

    public static  boolean HAS_NEWMESSAGE = false;//默认没有新信息到来
    public  static  ArrayList<HashMap<String,Object>> chatList=null;
    String[] adapterMapKeys={"image","text"};               //adapter列表里每一个Map的键
    int[] adapterControlIds={R.id.chatlist_image_me,R.id.chatlist_text_me,R.id.chatlist_image_other,R.id.chatlist_text_other};//adapter布局文件中每个控件的id
    int[] adapterLayouts={R.layout.chat_me,R.layout.chat_other};//adapter的ListView中每一项的布局文件
    public final static int OTHER=1;                        //消息来自其他人
    public final static int ME=0;                           //消息来自我自己
    public final static int CHAT_SINGLE=0;                  //单聊
    public final static int CHAT_GROUP=1;                   //群聊
    public final static int READ = 0;
    public final static int UNREAD = 1;
    private final Context context = this;
    public static ListView chatListView;
    private Button chatSendButton=null;
    private Button BackToLists = null;
    private EditText editText=null;
    private AVIMClient avimClient = null;
    private AVIMConversation avimConversation = null;
    public static MyChatAdapter adapter ;
    private ChatMessageDao chatMessageDao = new ChatMessageDao(context);
    private int chatId ;
    public static String chatTargetId;                //聊天对象id
    private String userId = ChatConversationHelper.USERID;                       //用户自身id
    private static Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        chatId = intent.getExtras().getInt("chatId");//获取传过来的会话id
        //chatType = intent.getExtras().getInt("ChatType");
        //userId = intent.getExtras().getString("UserId");
        chatTargetId = intent.getStringExtra("chatTargetId");
        chatList=new ArrayList<HashMap<String,Object>>();
        //根据chatId初始化本地记录
        initMessageListById(chatId);
        chatSendButton=(Button)findViewById(R.id.chat_bottom_sendbutton);//发送消息按钮
        BackToLists = (Button)findViewById(R.id.chat_back_button);//返回聊天列表
        editText=(EditText)findViewById(R.id.chat_bottom_edittext);//消息编辑框
        chatListView=(ListView)findViewById(R.id.chat_list);//聊天记录显示界面（正中间最大哪一块）
        adapter=new MyChatAdapter(this,chatList,adapterLayouts,adapterMapKeys,adapterControlIds);//界面适配器
        initAvimconversation();
        chatListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        chatListView.setSelection(chatList.size()-1);
        BackToLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onDestroy();
               finish();
            }
        });
        chatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /**
                 * 这是一个发送消息的监听器，注意如果文本框中没有内容，那么getText()的返回值可能为
                 * null，这时调用toString()会有异常！所以这里必须在后面加上一个""隐式转换成String实例
                 * ，并且不能发送空消息。
                 */
                String mywords=(editText.getText()+"").toString();

                if(mywords.length()==0)
                    return;
                editText.setText("");
                sendmessage(mywords);//发送消息更新在聊天界面并存入本地手机
                /**
                 * 更新数据列表，并且通过setSelection方法使ListView始终滚动在最底端
                 */
                adapter.notifyDataSetChanged();//刷新
                chatListView.setSelection(chatList.size());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    private void initAvimconversation() {
        avimClient = AVIMClient.getInstance(userId);
        avimClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClientT, AVException e) {
                if (null != e) {
                    // 出错了，可能是网络问题无法连接 LeanCloud 云端，请检查网络之后重试。
                    // 此时聊天服务不可用。
                    e.printStackTrace();
                    Toast.makeText(context,"打开会话出错",Toast.LENGTH_SHORT).show();
                } else {
                    List<String> clientIds = new ArrayList<String>();
                    clientIds.add(userId);
                    clientIds.add(chatTargetId);
                    Map<String, Object> attr = new HashMap<String, Object>();
                    attr.put("type", CHAT_SINGLE);//单聊
                    Toast.makeText(context,"打开会话成功1",Toast.LENGTH_SHORT).show();
                    avimClientT.createConversation(clientIds,attr,new AVIMConversationCreatedCallback() {
                        @Override
                        public void done(AVIMConversation conversation, AVException e) {
                            if (null != conversation) {
                                Toast.makeText(context,"建立会话成功2",Toast.LENGTH_SHORT).show();
                                avimConversation = conversation;
                            }
                        }
                    });
                }
            }
        });
    }

    private void initMessageListById(int chatId) {
        /**
        根据会话id,获取聊天记录加载到chatlist表中
         */
        ChatMessageDao chatMessageDao = new ChatMessageDao(context);
        List<ChatMessage> list = chatMessageDao.findById(chatId);
        if(list.size()!=0){
            for(int i=0;i<list.size();i++){
                ChatMessage chatMessage = list.get(i);
                String message = chatMessage.getMessage();
                if (chatMessage.getUserId().equals(userId)){
                    addTextToList(message,ME);
                }else{
                    addTextToList(message,OTHER);
                }
            }
        }
    }

/*    private String findconverstationId(String userId, String chatTargetId, int chatType) {
        //查询服务器是否已经存在该会话，有则返回会话id，否则返回null
        return null;
    }*/

    private void sendmessage(final String words) {
            AVIMMessage avimMessage = new AVIMMessage();
            avimMessage.setContent(words);
            if(avimConversation==null){
                Toast.makeText(ChatActivity.this,"无法连接网络，发送失败！",Toast.LENGTH_LONG).show();
                return;
            }
            avimConversation.sendMessage(avimMessage,new AVIMConversationCallback() {
                @Override
                public void done(AVException e) {
                    if (null != e) {
                        // 出错了。。。
                        e.printStackTrace();
                        Toast.makeText(ChatActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChatActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                        addTextToList(words,ME);
                        //同时存入本地数据库
                        Date now = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
                        String dateAdd = dateFormat.format(now);
                        ChatMessage chatMessageAdd = new ChatMessage(chatId,userId,words,dateAdd,READ);
                        chatMessageDao.add(chatMessageAdd);
                        adapter.notifyDataSetChanged();//刷新
                        chatListView.setSelection(chatList.size());//
                    }
                }
            });
        }

    public static void addTextToList(String text, int who) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("person", who);
            map.put("image", who == ME ? R.drawable.user2 : R.drawable.user1);
            map.put("text", text);
            chatList.add(map);
        }

    public class MyChatAdapter extends BaseAdapter {
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

    public static void reFresh() {
        //静态方法，外部调用刷新聊天页面
        handler.post(refresh);
    }

    static final Runnable refresh = new Runnable() {
        public void run() {
            adapter.notifyDataSetChanged();//刷新
            chatListView.setSelection(chatList.size());//
        }
    };
}

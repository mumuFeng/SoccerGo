package com.rangers.soccergo.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rangers.soccergo.entities.ChatItem;
import com.rangers.soccergo.entities.ChatShow;
import com.rangers.soccergo.helpers.ChatDBHelper;

import java.util.List;

/**
 * 会话类Dao
 * Created by Infocenter on 2015/5/21.
 */
public class ChatItemDao {

    private  ChatDBHelper chatDBHelper;
    private  SQLiteDatabase db ;

    public  ChatItemDao(Context context){
        //构造方法中链接数据库MyChat.db
         chatDBHelper = new ChatDBHelper(context,"MyChat.db");
    }

    public static void main(String[] args){
    }

    public int[] getChatIds(String userId){
        db = chatDBHelper.getWritableDatabase();
        int[] ids = null;
        int index = 0;//数组内序号
        int count = 0;//统计会话数目
        String sql = "select C_ID from t_chatUsers where USERID='"+userId+"'";
        String sql_count = "select count(*) as count from  t_chatUsers   where USERID='"+userId+"'";
        /*
        改进思路，这里查询出来的按最近的消息时间排列
         */
        Cursor cursor_count = db.rawQuery(sql_count,null);
        while (cursor_count.moveToNext()){
            count = cursor_count.getInt(cursor_count.getColumnIndex("count"));
        }
        if(count==0){
            return ids;
        }
        ids = new int[count];
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("C_ID"));
            ids[index] = id;
            index++;
        }
        cursor_count.close();
        cursor.close();
        return ids;
    }

    public  void add(ChatItem chatItem){
        //添加新的会话类
        db = chatDBHelper.getWritableDatabase();
        String sql = "insert into t_chatUsers(USERID,CHATTARGETID,CHATTYPE,CHATNAME,COUNT) values (?,?,?,?,?)";
        db.execSQL(sql,new Object[]{chatItem.getUserId(),chatItem.getChatTargetId(),chatItem.getChatType(),chatItem.getChatName(),chatItem.getCount()});
    }

    public  void delete(ChatItem chatItem){
        //删除一个会话，同时应该链带删除会话对应的聊天记录message表中数据
        db = chatDBHelper.getWritableDatabase();
        int id = chatItem.getChat_id();
        String sql_chat="delete from t_chatUsers where C_ID = "+id;
        String sql_message = "delete from t_messageList where C_ID = "+id;
        db.execSQL(sql_chat);
        db.execSQL(sql_message);
    }

    public  int getID(String UserId,String targetID){
        //单聊时获取存储在本地的会话id
        //通过两个聊天用户id
        /*
        改进思路，每个用户建立一张表，届时只需查询对象id一种记录即可
         */
        db = chatDBHelper.getWritableDatabase();
        String sql = "select C_ID from t_chatUsers where USERID='"+UserId+"' and CHATTARGETID='"+targetID+"'";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToNext()){
            return  cursor.getInt(0);
        }
        cursor.close();
        return 0;//没有记录则返回null
    }

    public  void update(ChatItem chatItem){
        //更新(包括群聊会话名称，会话类别，会话未读记录数)
        db = chatDBHelper.getWritableDatabase();
        String sql = "update t_chatUsers set CHATTYPE ="+chatItem.getChatType()+" and COUNT = "+chatItem.getCount()+" " +
                " and CHATNAME='"+chatItem.getChatName()+"' where C_ID="+chatItem.getChat_id();
        db.execSQL(sql);
    }

    public  ChatItem getChatItemById(int id){
        //通过会话id获取会话类
        db = chatDBHelper.getWritableDatabase();
        String sql = "select * from t_chatUsers where C_ID="+id;
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToNext()){
            String userId = cursor.getString(cursor.getColumnIndex("USERID"));
            String chatTargetId = cursor.getString(cursor.getColumnIndex("CHATTARGETID"));
            String chatName = cursor.getString(cursor.getColumnIndex("CHATNAME"));
            int ChatType = cursor.getInt(cursor.getColumnIndex("CHATTYPE"));
            int count = cursor.getInt(cursor.getColumnIndex("COUNT"));
            cursor.close();
            return  new ChatItem(id,chatName,userId,chatTargetId,ChatType,count);
        }
        cursor.close();
        return null;
    }

    public String getChatTargetIdById(int chatId){
        //获取会话的对象id
        db = chatDBHelper.getWritableDatabase();
        String sql = "select * from t_chatUsers where C_ID="+chatId;
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToNext()){
            String chatTargetId = cursor.getString(cursor.getColumnIndex("CHATTARGETID"));
            return  chatTargetId;
        }
        cursor.close();
        return null;
    }

    public   ChatShow getChatShowById(int ChatId){
        //获取页面会话显示类
        db = chatDBHelper.getWritableDatabase();
        ChatShow chatShow = null;
        String sql_chat = "select * from t_chatUsers where C_ID="+ChatId+"";
        String sql_message = "select * from t_messageList where C_ID ="+ChatId+" order by TIME desc";//按时间倒序，获取最近一条消息
        Cursor cursor_chat = db.rawQuery(sql_chat,null);
        Cursor cursor = db.rawQuery(sql_message,null);
        String recentlyMessage ="";
        String recentTime = "";
        String chatTargetName ="";
        if(cursor.moveToNext()){
            //获取时间最大即最近一条消息记录
            recentlyMessage = cursor.getString(cursor.getColumnIndex("MESSAGE"));
            recentTime = cursor.getString(cursor.getColumnIndex("TIME"));
        }
        if(cursor_chat.moveToNext()){
            int count = cursor_chat.getInt(cursor_chat.getColumnIndex("COUNT"));
            chatTargetName = cursor_chat.getString(cursor_chat.getColumnIndex("CHATTARGETID"));
            cursor.close();
            cursor_chat.close();
            chatShow = new ChatShow(ChatId,chatTargetName,recentlyMessage,recentTime,count);
        }
        cursor.close();
        cursor_chat.close();
        return chatShow;
    }
}

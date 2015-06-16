package com.rangers.soccergo.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rangers.soccergo.entities.ChatMessage;
import com.rangers.soccergo.helpers.ChatDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天内容的数据库存取过程
 * Created by Infocenter on 2015/5/21.
 */
public class ChatMessageDao {
    //private ChatMessage chatMessage;
    private SQLiteDatabase db;
    private ChatDBHelper chatDBHelper ;

    public ChatMessageDao(Context context) {
        //构造方法链接数据库
        chatDBHelper = new ChatDBHelper(context,"MyChat.db");
    }

    public void  add(ChatMessage chatMessage){
        //添加聊天消息
        db = chatDBHelper.getWritableDatabase();
        String sql = "insert into t_messageList(C_ID,USERID,MESSAGE,TIME,ISREAD) values (?,?,?,?,?)";
        db.execSQL(sql,new Object[]{chatMessage.getC_id(),chatMessage.getUserId(),chatMessage.getMessage(),chatMessage.getTime(),chatMessage.getIsRead()});
    }

    public void  delete(ChatMessage chatMessage){
        //删除某一条消息
        db = chatDBHelper.getWritableDatabase();
        int id = chatMessage.getM_id();
        String sql="delete from t_messageList where M_ID = "+id;
        db.execSQL(sql);
    }

    public List<ChatMessage> findById(int chatId){
        //查询属于该会话id的消息记录
        /*
        改进：不用一次返回所有记录，利用数据库关键字limit分页技术返回指定条数
        先暂时返回20条
         */
        List<ChatMessage> list = new ArrayList();
        db = chatDBHelper.getWritableDatabase();
        String sql = "select * from t_messageList where C_ID ="+chatId+" order by TIME";//  limit 20";//返回最近的20条聊天数据
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            //实体化构造聊天消息对象
            int m_id = cursor.getInt(cursor.getColumnIndex("M_ID"));
            String userId = cursor.getString(cursor.getColumnIndex("USERID"));
            String message = cursor.getString(cursor.getColumnIndex("MESSAGE"));
            String time = cursor.getString(cursor.getColumnIndex("TIME"));
            int isRead = cursor.getInt(cursor.getColumnIndex("ISREAD"));
            ChatMessage chatMessage = new ChatMessage(m_id,userId,message,time,isRead);
            list.add(chatMessage);
        }
        cursor.close();
        return  list;//list中数据按时间顺序排列
    }
}

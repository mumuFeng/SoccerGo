package com.rangers.soccergo.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 聊天模块的sqlite数据库帮助类
 * Created by Infocenter on 2015/5/21.
 */
public class ChatDBHelper extends SQLiteOpenHelper {
    private static final int version = 1; //数据库版本

    public ChatDBHelper(Context context,String DbName) {
        super(context, DbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlChatUsers = "CREATE TABLE t_chatUsers(C_ID integer primary key autoincrement not null, USERID varchar(60) not null," +
                " CHATTARGETID varchar(60) not null,CHATTYPE integer not null,CHATNAME varchar,COUNT integer not null)";
        String sqlMessage = "CREATE TABLE t_messageList(M_ID integer primary key autoincrement not null,C_ID integer not null," +
                " USERID varchar(60) not null,MESSAGE varchar(200) not null,TIME datetime,ISREAD integer not null)";
        db.execSQL(sqlChatUsers);//建聊天用户表
        db.execSQL(sqlMessage);//建聊天信息表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

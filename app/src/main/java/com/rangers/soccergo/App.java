package com.rangers.soccergo;

import android.app.Application;

import com.avos.avoscloud.AVUser;
import com.rangers.soccergo.entities.User;
import com.rangers.soccergo.helpers.LeanCloudHelper;

/**
 * App
 * Desc:自定义应用环境
 * Team: Rangers
 * Date: 2015/4/8
 * Time: 21:09
 * Created by: Wooxxx
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化LeanCloud
        LeanCloudHelper.initLeanCloud(this);
    }

    /**
     * 获得当前用户
     *
     * @return 当前用户
     */
    public static User getCurrentUser() {
        return AVUser.cast(AVUser.getCurrentUser(),
                User.class);
    }


}

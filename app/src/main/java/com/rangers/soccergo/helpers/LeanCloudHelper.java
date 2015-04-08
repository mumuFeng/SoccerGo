package com.rangers.soccergo.helpers;

import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.rangers.soccergo.entities.Team;

/**
 * LeanCloudHelper
 * Desc:LeanCloud帮助类
 * Team: Rangers
 * Date: 2015/4/8
 * Time: 21:08
 * Created by: Wooxxx
 */
public class LeanCloudHelper {

    private static final String APP_ID = "z9d925kj7ws3ypepkr1cqa42h00iw22spmc8lik28w7cokx5";
    private static final String APP_KEY = "o6ltk67iv7cxqntxf6xpp91jgjj60qpgxl7qushxl1lxxst3";

    /**
     * 初始化Leancloud
     *
     * @param ctx
     */
    public static void initLeanCloud(Context ctx) {
        //注册子类
        registerSubclass();
        AVOSCloud.initialize(ctx, APP_ID, APP_KEY);
        AVOSCloud.setDebugLogEnabled(true);
    }

    /**
     * 注册实体子类
     */
    private static void registerSubclass() {
        AVObject.registerSubclass(Team.class);
    }

}

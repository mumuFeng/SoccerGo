package com.rangers.soccergo.activities;


import android.content.Intent;
import android.os.Bundle;

/**
 * LaunchActivity
 * Desc:启动Activity，在展示启动画面的同时可以做一些策略
 * Team: Rangers
 * Date: 2015/4/8
 * Time: 20:59
 * Created by: Wooxxx
 */
public class LaunchActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(getApplication(), ElephantActivity.class));
    }
}

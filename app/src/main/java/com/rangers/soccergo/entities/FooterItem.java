package com.rangers.soccergo.entities;

/**
 * FooterItem
 * Desc: 底部栏菜单项
 * Team: Rangers
 * Date: 2015/4/14
 * Time: 10:07
 * Created by: Wooxxx
 */
public class FooterItem extends ConfigableMenuItem {
    //待跳转fragment名称
    private String fragment;

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }
}

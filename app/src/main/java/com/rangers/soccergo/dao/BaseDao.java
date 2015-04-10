package com.rangers.soccergo.dao;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.rangers.soccergo.entities.Base;

import java.util.List;

/**
 * BaseDao
 * Desc:Dao基类
 * Team: Rangers
 * Date: 2015/4/9
 * Time: 7:50
 * Created by: Wooxxx
 */
public class BaseDao {

    /**
     * 异步地从云端获得表中所有数据或者部分数据
     *
     * @param limit    取出数目的限制，若limit<=0，则代表取出所有
     * @param callback 回调接口
     */
    public <T extends Base> void findAllOrLimit(
            final FindCallback<T> callback, int limit) {
        AVQuery<T> query = new AVQuery<T>(this.getClassName());
        query.findInBackground(callback);
    }

    /**
     * 同步地从云端获得表中所有数据或者部分数据
     *
     * @param limit 取出数目的限制，若limit<=0，则代表取出所有
     */
    public <T extends Base> List<T> findAllOrLimit(int limit) {
        AVQuery<T> query = new AVQuery<T>(this.getClassName());
        try {
            return query.find();
        } catch (AVException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得云端Class名称，需要由子类重载
     *
     * @return 云端Class名称
     */
    public String getClassName() {
        return null;
    }


}

package com.rangers.soccergo.dao;

import com.rangers.soccergo.entities.User;

/**
 * UserDao
 * Desc:用户Dao
 * Team: Rangers
 * Date: 2015/4/9
 * Time: 7:51
 * Created by: Wooxxx
 */
public class UserDao extends BaseDao {
    @Override
    public String getClassName() {
        return User.CLASS_NAME;
    }
}

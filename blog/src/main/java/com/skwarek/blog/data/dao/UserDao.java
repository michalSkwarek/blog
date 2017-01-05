package com.skwarek.blog.data.dao;

import com.skwarek.blog.data.dao.generic.GenericDao;
import com.skwarek.blog.data.entity.User;

/**
 * Created by Michal on 05/01/2017.
 */
public interface UserDao extends GenericDao<User, Long> {

    User findUserByUsername(String username);
}

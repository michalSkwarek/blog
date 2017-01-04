package com.skwarek.blog.data.dao;

import com.skwarek.blog.data.dao.generic.GenericDao;
import com.skwarek.blog.data.entity.Comment;

/**
 * Created by Michal on 04/01/2017.
 */
public interface CommentDao extends GenericDao<Comment, Long> {

    boolean removeComment(long commentId);
}

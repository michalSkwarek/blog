package com.skwarek.blog.data.dao.impl;

import com.skwarek.blog.data.dao.CommentDao;
import com.skwarek.blog.data.dao.generic.GenericDaoImpl;
import com.skwarek.blog.data.entity.Comment;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Michal on 04/01/2017.
 */
@Repository
public class CommentDaoImpl extends GenericDaoImpl<Comment, Long> implements CommentDao {

    @Override
    public boolean removeComment(long commentId) {
        Query removeComment = getSession().createQuery("delete from Comment c where c.id = :id");
        removeComment.setParameter("id", commentId);
        return removeComment.executeUpdate() > 0;
    }
}

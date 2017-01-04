package com.skwarek.blog.service.impl;

import com.skwarek.blog.data.dao.CommentDao;
import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.service.CommentService;
import com.skwarek.blog.service.generic.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Michal on 04/01/2017.
 */
@Service
public class CommentServiceImpl extends GenericServiceImpl<Comment, Long> implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public void approve(Comment comment) {
        comment.setApprovedComment(true);
        commentDao.update(comment);
    }

    @Override
    public boolean removeComment(long commentId) {
        return commentDao.removeComment(commentId);
    }
}
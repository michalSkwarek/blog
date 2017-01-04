package com.skwarek.blog.service.impl;

import com.skwarek.blog.data.dao.CommentDao;
import com.skwarek.blog.data.dao.PostDao;
import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.service.PostService;
import com.skwarek.blog.service.generic.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by Michal on 02/01/2017.
 */
@Service
public class PostServiceImpl extends GenericServiceImpl<Post, Long> implements PostService {

    @Autowired
    private PostDao postDao;

    @Autowired
    private CommentDao commentDao;

    @Override
    @SuppressWarnings("unchecked")
    public List<Post> findAllPublishedPosts() {
        return postDao.findAllPublishedPosts();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Post> findAllDrafts() {
        return postDao.findAllDrafts();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Comment> findAllComments(Post post) {
        return postDao.findAllComments(post);
    }

    @Override
    public int getApprovedCommentsCounter(Post post) {
        return postDao.getApprovedCommentsCounter(post);
    }

    @Override
    public void createPost(Post post) {
        post.setCreatedDate(new Date());
        postDao.create(post);
    }

    @Override
    public void publishPost(Post post) {
        post.setPublishedDate(new Date());
        postDao.update(post);
    }

    @Override
    public void addCommentToPost(Comment comment, long postId) {
        comment.setCreatedDate(new Date());
        comment.setApprovedComment(false);
        comment.setPost(postDao.read(postId));
        commentDao.create(comment);
    }

    @Override
    public boolean removePost(long postId) {
        return postDao.removePost(postId);
    }
}

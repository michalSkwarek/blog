package com.skwarek.blog.service.impl;

import com.skwarek.blog.data.dao.CommentDao;
import com.skwarek.blog.data.dao.PostDao;
import com.skwarek.blog.data.dao.UserDao;
import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.service.PostService;
import com.skwarek.blog.service.generic.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Michal on 02/01/2017.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class PostServiceImpl extends GenericServiceImpl<Post, Long> implements PostService {

    private final PostDao postDao;
    private final CommentDao commentDao;
    private final UserDao userDao;

    @Autowired
    public PostServiceImpl(PostDao postDao, CommentDao commentDao, UserDao userDao) {
        this.postDao = postDao;
        this.commentDao = commentDao;
        this.userDao = userDao;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Post> findAllPublishedPosts() {
        return postDao.findAllPublishedPosts();
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Post> findAllDrafts() {
        return postDao.findAllDrafts();
    }

    @Override
    public void createPost(Post post) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        post.setAuthor(userDao.findUserByUsername(auth.getName()));
        post.setCreatedDate(new Date());
        postDao.create(post);
    }

    @Override
    public void updatePost(Post post) {
        Post oldPost = postDao.read(post.getId());
        oldPost.setTitle(post.getTitle());
        oldPost.setText(post.getText());
        postDao.update(oldPost);
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

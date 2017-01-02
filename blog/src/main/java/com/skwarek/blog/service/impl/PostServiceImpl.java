package com.skwarek.blog.service.impl;

import com.skwarek.blog.data.dao.PostDao;
import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.service.PostService;
import com.skwarek.blog.service.generic.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Michal on 02/01/2017.
 */
@Service
public class PostServiceImpl extends GenericServiceImpl<Post, Long> implements PostService {

    @Autowired
    private PostDao postDao;

    @Override
    public void publish(Post post) {
        post.setPublishedDate(new Date());
        postDao.create(post);
    }

    @Override
    public List<Post> findAllPublishedPosts() {
        return postDao.findAllPublishedPosts();
    }
}
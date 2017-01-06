package com.skwarek.blog.data.dao;

import com.skwarek.blog.data.dao.generic.GenericDao;
import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.data.entity.Post;

import java.util.List;

/**
 * Created by Michal on 02/01/2017.
 */
public interface PostDao extends GenericDao<Post, Long> {

    List findAllPublishedPosts();

    List findAllDrafts();

    boolean removePost(long postId);
}

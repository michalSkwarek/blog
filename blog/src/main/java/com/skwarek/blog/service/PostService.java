package com.skwarek.blog.service;

import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.service.generic.GenericService;

import java.util.List;

/**
 * Created by Michal on 02/01/2017.
 */
public interface PostService extends GenericService<Post, Long> {

    void createPost(Post post);

    void publishPost(Post post);

    List<Post> findAllPublishedPosts();

    void updatePost(Post post);

    List<Post> findAllDrafts();
}

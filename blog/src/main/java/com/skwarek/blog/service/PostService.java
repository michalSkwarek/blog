package com.skwarek.blog.service;

import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.service.generic.GenericService;

import java.util.List;

/**
 * Created by Michal on 02/01/2017.
 */
public interface PostService extends GenericService<Post, Long> {

    List<Post> findAllPublishedPosts();

    List<Post> findAllDrafts();

    void createPost(Post post);

    void updatePost(Post post);

    void publishPost(Post post);

    void addCommentToPost(Comment comment, long postId);

    boolean removePost(long postId);
}

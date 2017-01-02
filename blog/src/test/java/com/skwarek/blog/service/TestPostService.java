package com.skwarek.blog.service;

import com.skwarek.blog.data.dao.PostDao;
import com.skwarek.blog.data.entity.Post;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Created by Michal on 02/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestPostService {

    private List<Post> listOfPosts;

    private PostDao postDao;

    @Before
    public void setUp() {
        Post newPost = new Post();
        newPost.setId(1L);
        newPost.setAuthor("author1");
        newPost.setTitle("title1");
        newPost.setText("text1");
        newPost.setCreatedDate(new Date());
        newPost.setPublishedDate(new Date());

        Post otherPost = new Post();
        otherPost.setId(2L);
        otherPost.setAuthor("author2");
        otherPost.setTitle("title2");
        otherPost.setText("text2");
        otherPost.setCreatedDate(new Date());
        otherPost.setPublishedDate(new Date());

        this.listOfPosts = new ArrayList<>();
        this.listOfPosts.add(newPost);
        this.listOfPosts.add(otherPost);

        this.postDao = mock(PostDao.class);

        given(this.postDao.findAllPublishedPosts()).willReturn(listOfPosts);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSendingPostsFromDaoToService() throws Exception {
        verify(postDao, times(0)).findAllPublishedPosts();
        List<Post> found = postDao.findAllPublishedPosts();
        verify(postDao, times(1)).findAllPublishedPosts();
        assertEquals(listOfPosts.size(), found.size());
    }
}

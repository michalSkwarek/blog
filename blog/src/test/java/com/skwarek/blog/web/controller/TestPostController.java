package com.skwarek.blog.web.controller;

import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.service.PostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Michal on 02/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestPostController {

    private List<Post> listOfPosts;

    private PostService postService;
    private PostController postController;

    private MockMvc mockMvc;

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

        this.postService = mock(PostService.class);
        this.postController = new PostController(postService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.postController)
                .build();

        given(this.postService.findAllPublishedPosts()).willReturn(listOfPosts);
    }

    @Test
    public void testSendingPostsToView() throws Exception {
        verify(postService, times(0)).findAllPublishedPosts();

        mockMvc.perform(get("/posts/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("posts", listOfPosts))
                .andExpect(view().name("posts_list"));

        verify(postService, times(1)).findAllPublishedPosts();
    }
}

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

    private Post newPost;
    private Post otherPost;
    private PostService postService;
    private PostController postController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        newPost = new Post();
        newPost.setId(1L);
        newPost.setAuthor("author1");
        newPost.setTitle("title1");
        newPost.setText("text1");
        newPost.setCreatedDate(new Date());
        newPost.setPublishedDate(new Date());

        otherPost = new Post();
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
        given(this.postService.read(1L)).willReturn(newPost);
    }

    @Test
    public void testSendingPostsToView() throws Exception {
        verify(postService, times(0)).findAllPublishedPosts();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("posts", listOfPosts))
                .andExpect(view().name("posts_list"));

        verify(postService, times(1)).findAllPublishedPosts();
    }

    @Test
    public void testSendingPostToView() throws Exception {
        verify(postService, times(0)).read(1L);

        mockMvc.perform(get("/post/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", newPost))
                .andExpect(view().name("post_detail"));

        verify(postService, times(1)).read(1L);
    }

    @Test
    public void testInitCreatePostForm() throws Exception {
        mockMvc.perform(get("/post/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(view().name("post_edit"));
    }

    @Test
    public void testProcessCreatePostFormSuccess() throws Exception {
        mockMvc.perform(post("/post/new")
                .param("title", "title1")
                .param("text", "text1")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    public void testProcessCreatePostFormHasErrors() throws Exception {
        mockMvc.perform(post("/post/new")
                .param("title", "")
                .param("text", "")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("post"))
                .andExpect(model().attributeHasFieldErrors("post", "title"))
                .andExpect(model().attributeHasFieldErrors("post", "text"))
                .andExpect(view().name("post_edit"));
    }

    @Test
    public void testInitEditPostForm() throws Exception {
        verify(postService, times(0)).read(1L);

        mockMvc.perform(get("/post/1/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", newPost))
                .andExpect(view().name("post_edit"));

        verify(postService, times(1)).read(1L);
    }

    @Test
    public void testProcessEditPostFormSuccess() throws Exception {
        mockMvc.perform(post("/post/1/edit")
                .param("title", "title1")
                .param("text", "text1")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    public void testProcessEditPostFormHasErrors() throws Exception {
        mockMvc.perform(post("/post/1/edit")
                .param("title", "")
                .param("text", "")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("post"))
                .andExpect(model().attributeHasFieldErrors("post", "title"))
                .andExpect(model().attributeHasFieldErrors("post", "text"))
                .andExpect(view().name("post_edit"));
    }
}

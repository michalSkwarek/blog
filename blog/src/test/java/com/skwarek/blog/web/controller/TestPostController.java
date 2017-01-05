package com.skwarek.blog.web.controller;

import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.data.entity.User;
import com.skwarek.blog.service.PostService;
import javafx.geometry.Pos;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static junit.framework.TestCase.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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

    private final static String VIEWS_POST_LIST = "blog/post_list";
    private final static String VIEWS_DRAFTS = "blog/post_draft_list";
    private final static String VIEWS_POST_DETAIL = "blog/post_detail";
    private final static String VIEWS_POST_FORM = "blog/post_edit";
    private final static String VIEWS_COMMENT_FORM = "blog/add_comment_to_post";
    private final static String HOME_PAGE = "/";
    private final static String REDIRECT_TO_HOME_PAGE = "redirect:" + HOME_PAGE;

    private final static Date createdDate = new GregorianCalendar(2000, Calendar.JANUARY, 11, 11, 22, 33).getTime();
    private final static Date publishedDate = new GregorianCalendar(2000, Calendar.JANUARY, 12, 22, 33, 44).getTime();

    private User firstUser;
    private User secondUser;

    private Post firstPublishedPost;
    private Post secondPublishedPost;
    private Post firstDraftPost;

    private List<Post> listOfPublishedPosts;
    private List<Post> listOfDraftPosts;

    private Comment firstApprovedComment;
    private Comment firstNotApprovedComment;

    private PostService postService;
    private PostController postController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.firstUser = new User();
        this.firstUser.setUsername("user1");

        this.secondUser = new User();
        this.secondUser.setUsername("user2");

        this.firstPublishedPost = new Post();
        this.firstPublishedPost.setId(1L);
        this.firstPublishedPost.setAuthor(firstUser);
        this.firstPublishedPost.setTitle("title1");
        this.firstPublishedPost.setText("text1");
        this.firstPublishedPost.setCreatedDate(createdDate);
        this.firstPublishedPost.setPublishedDate(publishedDate);

        this.secondPublishedPost = new Post();
        this.secondPublishedPost.setId(2L);
        this.secondPublishedPost.setAuthor(secondUser);
        this.secondPublishedPost.setTitle("title2");
        this.secondPublishedPost.setText("text2");
        this.secondPublishedPost.setCreatedDate(createdDate);
        this.secondPublishedPost.setPublishedDate(publishedDate);

        this.listOfPublishedPosts = new ArrayList<>();
        this.listOfPublishedPosts.add(firstPublishedPost);
        this.listOfPublishedPosts.add(secondPublishedPost);

        this.firstDraftPost = new Post();
        this.firstDraftPost.setId(3L);
        this.firstDraftPost.setAuthor(firstUser);
        this.firstDraftPost.setTitle("title3");
        this.firstDraftPost.setText("text3");
        this.firstDraftPost.setCreatedDate(createdDate);

        this.listOfDraftPosts = new ArrayList<>();
        this.listOfDraftPosts.add(firstDraftPost);

        this.firstApprovedComment = new Comment();
        this.firstApprovedComment.setId(1L);
        this.firstApprovedComment.setAuthor("author1");
        this.firstApprovedComment.setText("commentText1");
        this.firstApprovedComment.setCreatedDate(createdDate);
        this.firstApprovedComment.setApprovedComment(true);
        this.firstApprovedComment.setPost(firstPublishedPost);

        this.firstNotApprovedComment = new Comment();
        this.firstNotApprovedComment.setId(2L);
        this.firstNotApprovedComment.setAuthor("author2");
        this.firstNotApprovedComment.setText("commentText2");
        this.firstNotApprovedComment.setCreatedDate(createdDate);
        this.firstNotApprovedComment.setApprovedComment(false);
        this.firstNotApprovedComment.setPost(firstPublishedPost);

        this.postService = mock(PostService.class);
        this.postController = new PostController(this.postService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.postController)
                .build();

        given(this.postService.findAllPublishedPosts()).willReturn(listOfPublishedPosts);
        given(this.postService.read(1L)).willReturn(firstPublishedPost);

        given(this.postService.findAllDrafts()).willReturn(listOfDraftPosts);
        given(this.postService.read(3L)).willReturn(firstDraftPost);
    }

    @Test
    public void testSendingListOfPublishedPostsToView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("posts", listOfPublishedPosts))
                .andExpect(model().attribute("posts", hasSize(2)))
                .andExpect(model().attribute("posts", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("author", is(firstUser)),
                                hasProperty("title", is("title1")),
                                hasProperty("text", is("text1")),
                                hasProperty("createdDate", is(createdDate)),
                                hasProperty("publishedDate", is(publishedDate))
                        )
                )))
                .andExpect(model().attribute("posts", hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("author", is(secondUser)),
                                hasProperty("title", is("title2")),
                                hasProperty("text", is("text2")),
                                hasProperty("createdDate", is(createdDate)),
                                hasProperty("publishedDate", is(publishedDate))
                        )
                )))
                .andExpect(forwardedUrl(VIEWS_POST_LIST))
                .andExpect(view().name(VIEWS_POST_LIST));

        verify(postService, times(1)).findAllPublishedPosts();
//        verifyNoMoreInteractions(postService);
    }

    @Test
    public void testSendingApprovedCommentsCounterInPostToView() throws Exception {
        mockMvc.perform(get("/"));


        // TODO: 05/01/2017
    }

    @Test
    public void showDrafts_ShouldAddDraftEntriesToModelAndRenderDraftListView() throws Exception {
        mockMvc.perform(get("/drafts"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("posts", listOfDraftPosts))
                .andExpect(model().attribute("posts", hasSize(1)))
                .andExpect(model().attribute("posts", hasItem(
                        allOf(
                                hasProperty("id", is(3L)),
                                hasProperty("author", is(firstUser)),
                                hasProperty("title", is("title3")),
                                hasProperty("text", is("text3")),
                                hasProperty("createdDate", is(createdDate)),
                                hasProperty("publishedDate", nullValue())
                        )
                )))
                .andExpect(forwardedUrl(VIEWS_DRAFTS))
                .andExpect(view().name(VIEWS_DRAFTS));

        verify(postService, times(1)).findAllDrafts();
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void showPost_ShouldAddTodoEntryToModelAndRenderViewTodoEntryView() throws Exception {
        mockMvc.perform(get("/post/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", firstPublishedPost))
                .andExpect(model().attribute("post", hasProperty("id", is(1L))))
                .andExpect(model().attribute("post", hasProperty("author", is(firstUser))))
                .andExpect(model().attribute("post", hasProperty("title", is("title1"))))
                .andExpect(model().attribute("post", hasProperty("text", is("text1"))))
                .andExpect(model().attribute("post", hasProperty("createdDate", is(createdDate))))
                .andExpect(model().attribute("post", hasProperty("publishedDate", is(publishedDate))))
                .andExpect(forwardedUrl(VIEWS_POST_DETAIL))
                .andExpect(view().name(VIEWS_POST_DETAIL));

        verify(postService, times(1)).read(1L);
//        verifyZeroInteractions(postService);
    }

    @Test
    public void testSendingCommentsPostToView() throws Exception {

        // TODO: 05/01/2017
    }

    @Test
    public void testInitCreatePostForm() throws Exception {
        mockMvc.perform(get("/post/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", hasProperty("id", is(0L))))
                .andExpect(model().attribute("post", hasProperty("author", nullValue())))
                .andExpect(model().attribute("post", hasProperty("title", nullValue())))
                .andExpect(model().attribute("post", hasProperty("text", nullValue())))
                .andExpect(model().attribute("post", hasProperty("createdDate", nullValue())))
                .andExpect(model().attribute("post", hasProperty("publishedDate", nullValue())))
                .andExpect(forwardedUrl(VIEWS_POST_FORM))
                .andExpect(view().name(VIEWS_POST_FORM));

        verifyZeroInteractions(postService);
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
                .andExpect(model().attribute("post", hasProperty("id", is(0L))))
                .andExpect(model().attribute("post", hasProperty("author", nullValue())))
                .andExpect(model().attribute("post", hasProperty("title", isEmptyString())))
                .andExpect(model().attribute("post", hasProperty("text", isEmptyString())))
                .andExpect(model().attribute("post", hasProperty("createdDate", nullValue())))
                .andExpect(model().attribute("post", hasProperty("publishedDate", nullValue())))
                .andExpect(forwardedUrl(VIEWS_POST_FORM))
                .andExpect(view().name(VIEWS_POST_FORM));

        verifyZeroInteractions(postService);
    }

    @Test
    public void testProcessCreatePostFormSuccess() throws Exception {
        mockMvc.perform(post("/post/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "sampleTitle")
                .param("text", "sampleText")
                .sessionAttr("post", new Post())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(HOME_PAGE))
                .andExpect(view().name(REDIRECT_TO_HOME_PAGE));

        ArgumentCaptor<Post> formObjectArgument = ArgumentCaptor.forClass(Post.class);
        verify(postService, times(1)).createPost(formObjectArgument.capture());
        verifyNoMoreInteractions(postService);

        Post formPost = formObjectArgument.getValue();

        assertThat(formPost.getId(), is(0L));
        assertThat(formPost.getAuthor(), nullValue());
        assertThat(formPost.getTitle(), is("sampleTitle"));
        assertThat(formPost.getText(), is("sampleText"));
        assertThat(formPost.getCreatedDate(), nullValue());
        assertThat(formPost.getPublishedDate(), nullValue());
    }

    @Test
    public void testInitEditPostForm() throws Exception {
        verify(postService, times(0)).read(1L);

        mockMvc.perform(get("/post/1/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", firstPublishedPost))
                .andExpect(view().name(VIEWS_POST_FORM));

        verify(postService, times(1)).read(1L);
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
                .andExpect(view().name(VIEWS_POST_FORM));
    }

    @Test
    public void testProcessEditPostFormSuccess() throws Exception {
        mockMvc.perform(post("/post/1/edit")
                .param("title", "newTitle1")
                .param("text", "newText1")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT_TO_HOME_PAGE));
    }

    @Test
    public void testPublishPost() throws Exception {
        System.out.println("-dupa " + firstDraftPost + " " + firstDraftPost.getPublishedDate());
        verify(postService, times(0)).read(3L);

        mockMvc.perform(get("/post/3/publish"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", firstDraftPost))
                .andExpect(view().name(VIEWS_POST_DETAIL));
        System.out.println("-dupa " + firstDraftPost + " " + firstDraftPost.getPublishedDate());
        verify(postService, times(1)).read(3L);
    }

    @Test
    public void testRemovePost() throws Exception {
        mockMvc.perform(get("/post/1/remove"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT_TO_HOME_PAGE));
    }

    @Test
    public void testInitCreateCommentForm() throws Exception {
        mockMvc.perform(get("/post/1/comment"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("comment"))
                .andExpect(model().attribute("comment", hasProperty("id", is(0L))))
                .andExpect(model().attribute("comment", hasProperty("author", nullValue())))
                .andExpect(model().attribute("comment", hasProperty("text", nullValue())))
                .andExpect(model().attribute("comment", hasProperty("createdDate", nullValue())))
                .andExpect(model().attribute("comment", hasProperty("approvedComment", is(false))))
                .andExpect(model().attribute("comment", hasProperty("post", nullValue())))
                .andExpect(forwardedUrl(VIEWS_COMMENT_FORM))
                .andExpect(view().name(VIEWS_COMMENT_FORM));

        verifyZeroInteractions(postService);
    }

    @Test
    public void testProcessCreateCommentFormHasErrors() throws Exception {
        mockMvc.perform(post("/post/1/comment")
                .param("author", "")
                .param("text", "")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("comment"))
                .andExpect(model().attributeHasFieldErrors("comment", "author"))
                .andExpect(model().attributeHasFieldErrors("comment", "text"))
                .andExpect(model().attribute("comment", hasProperty("id", is(0L))))
                .andExpect(model().attribute("comment", hasProperty("author", isEmptyString())))
                .andExpect(model().attribute("comment", hasProperty("text", isEmptyString())))
                .andExpect(model().attribute("comment", hasProperty("createdDate", nullValue())))
                .andExpect(model().attribute("comment", hasProperty("approvedComment", is(false))))
                .andExpect(model().attribute("comment", hasProperty("post", nullValue())))
                .andExpect(forwardedUrl(VIEWS_COMMENT_FORM))
                .andExpect(view().name(VIEWS_COMMENT_FORM));

        verifyZeroInteractions(postService);
    }

    @Test
    public void testProcessCreateCommentFormSuccess() throws Exception {
        mockMvc.perform(post("/post/1/comment")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("author", "sampleAuthor")
                .param("text", "sampleText")
                .sessionAttr("comment", new Comment())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/1"))
                .andExpect(view().name(REDIRECT_TO_HOME_PAGE + "post/1"));

        ArgumentCaptor<Comment> formObjectArgument = ArgumentCaptor.forClass(Comment.class);
        verify(postService, times(1)).addCommentToPost(formObjectArgument.capture(), eq(1L));
        verifyNoMoreInteractions(postService);

        Comment formComment = formObjectArgument.getValue();

        assertThat(formComment.getId(), is(0L));
        assertThat(formComment.getAuthor(), is("sampleAuthor"));
        assertThat(formComment.getText(), is("sampleText"));
        assertThat(formComment.getCreatedDate(), nullValue());
        assertThat(formComment.isApprovedComment(), is(false));
        assertThat(formComment.getPost(), nullValue());
    }
}

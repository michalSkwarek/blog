package com.skwarek.blog.web.controller;

import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.data.entity.User;
import com.skwarek.blog.service.PostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

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

    private final static String REDIRECT_TO = "redirect:";
    private final static String HOME_PAGE = "/";
    private final static String POST_DETAIL_PAGE = "/post";

    private final static long NON_EXISTENT_HIBERNATE_ID = 0;

    private final static long FIRST_PUBLISHED_POST_ID = 1;
    private final static long SECOND_PUBLISHED_POST_ID = 2;
    private final static long DRAFT_POST_ID = 3;
    private final static long NON_EXISTENT_POST_ID = 1001;

    private final static long APPROVED_COMMENT_ID = 1;
    private final static long NOT_APPROVED_COMMENT_ID = 2;

    private final static Date CREATED_DATE = new GregorianCalendar(2000, Calendar.JANUARY, 11, 11, 22, 33).getTime();
    private final static Date PUBLISHED_DATE = new GregorianCalendar(2000, Calendar.JANUARY, 12, 22, 33, 44).getTime();

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
        this.firstPublishedPost.setId(FIRST_PUBLISHED_POST_ID);
        this.firstPublishedPost.setAuthor(firstUser);
        this.firstPublishedPost.setTitle("title1");
        this.firstPublishedPost.setText("text1");
        this.firstPublishedPost.setCreatedDate(CREATED_DATE);
        this.firstPublishedPost.setPublishedDate(PUBLISHED_DATE);

        this.secondPublishedPost = new Post();
        this.secondPublishedPost.setId(SECOND_PUBLISHED_POST_ID);
        this.secondPublishedPost.setAuthor(secondUser);
        this.secondPublishedPost.setTitle("title2");
        this.secondPublishedPost.setText("text2");
        this.secondPublishedPost.setCreatedDate(CREATED_DATE);
        this.secondPublishedPost.setPublishedDate(PUBLISHED_DATE);

        this.listOfPublishedPosts = new ArrayList<>();
        this.listOfPublishedPosts.add(firstPublishedPost);
        this.listOfPublishedPosts.add(secondPublishedPost);

        this.firstDraftPost = new Post();
        this.firstDraftPost.setId(DRAFT_POST_ID);
        this.firstDraftPost.setAuthor(firstUser);
        this.firstDraftPost.setTitle("title3");
        this.firstDraftPost.setText("text3");
        this.firstDraftPost.setCreatedDate(CREATED_DATE);

        this.listOfDraftPosts = new ArrayList<>();
        this.listOfDraftPosts.add(firstDraftPost);

        this.firstApprovedComment = new Comment();
        this.firstApprovedComment.setId(APPROVED_COMMENT_ID);
        this.firstApprovedComment.setAuthor("author1");
        this.firstApprovedComment.setText("commentText1");
        this.firstApprovedComment.setCreatedDate(CREATED_DATE);
        this.firstApprovedComment.setApprovedComment(true);
        this.firstApprovedComment.setPost(firstPublishedPost);

        this.firstNotApprovedComment = new Comment();
        this.firstNotApprovedComment.setId(NOT_APPROVED_COMMENT_ID);
        this.firstNotApprovedComment.setAuthor("author2");
        this.firstNotApprovedComment.setText("commentText2");
        this.firstNotApprovedComment.setCreatedDate(CREATED_DATE);
        this.firstNotApprovedComment.setApprovedComment(false);
        this.firstNotApprovedComment.setPost(firstPublishedPost);

        this.postService = mock(PostService.class);
        this.postController = new PostController(this.postService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.postController)
                .build();

        given(this.postService.findAllPublishedPosts()).willReturn(listOfPublishedPosts);
        given(this.postService.read(FIRST_PUBLISHED_POST_ID)).willReturn(firstPublishedPost);

        given(this.postService.findAllDrafts()).willReturn(listOfDraftPosts);
        given(this.postService.read(DRAFT_POST_ID)).willReturn(firstDraftPost);

        given(this.postService.read(NON_EXISTENT_POST_ID)).willReturn(null);
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
                                hasProperty("id", is(FIRST_PUBLISHED_POST_ID)),
                                hasProperty("author", is(firstUser)),
                                hasProperty("title", is("title1")),
                                hasProperty("text", is("text1")),
                                hasProperty("createdDate", is(CREATED_DATE)),
                                hasProperty("publishedDate", is(PUBLISHED_DATE))
                        )
                )))
                .andExpect(model().attribute("posts", hasItem(
                        allOf(
                                hasProperty("id", is(SECOND_PUBLISHED_POST_ID)),
                                hasProperty("author", is(secondUser)),
                                hasProperty("title", is("title2")),
                                hasProperty("text", is("text2")),
                                hasProperty("createdDate", is(CREATED_DATE)),
                                hasProperty("publishedDate", is(PUBLISHED_DATE))
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
                                hasProperty("id", is(DRAFT_POST_ID)),
                                hasProperty("author", is(firstUser)),
                                hasProperty("title", is("title3")),
                                hasProperty("text", is("text3")),
                                hasProperty("createdDate", is(CREATED_DATE)),
                                hasProperty("publishedDate", nullValue())
                        )
                )))
                .andExpect(forwardedUrl(VIEWS_DRAFTS))
                .andExpect(view().name(VIEWS_DRAFTS));

        verify(postService, times(1)).findAllDrafts();
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void showEmptyPost() throws Exception {
        mockMvc.perform(get("/post/{postId}", NON_EXISTENT_POST_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("post"))
                .andExpect(model().attribute("post", nullValue()))
                .andExpect(forwardedUrl(VIEWS_POST_DETAIL))
                .andExpect(view().name(VIEWS_POST_DETAIL));

        verify(postService, times(1)).read(NON_EXISTENT_POST_ID);
//        verifyZeroInteractions(postService);
    }

    @Test
    public void showPost_ShouldAddTodoEntryToModelAndRenderViewTodoEntryView() throws Exception {
        mockMvc.perform(get("/post/{postId}", FIRST_PUBLISHED_POST_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", firstPublishedPost))
                .andExpect(model().attribute("post", hasProperty("id", is(FIRST_PUBLISHED_POST_ID))))
                .andExpect(model().attribute("post", hasProperty("author", is(firstUser))))
                .andExpect(model().attribute("post", hasProperty("title", is("title1"))))
                .andExpect(model().attribute("post", hasProperty("text", is("text1"))))
                .andExpect(model().attribute("post", hasProperty("createdDate", is(CREATED_DATE))))
                .andExpect(model().attribute("post", hasProperty("publishedDate", is(PUBLISHED_DATE))))
                .andExpect(forwardedUrl(VIEWS_POST_DETAIL))
                .andExpect(view().name(VIEWS_POST_DETAIL));

        verify(postService, times(1)).read(FIRST_PUBLISHED_POST_ID);
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
                .andExpect(model().attribute("post", hasProperty("id", is(NON_EXISTENT_HIBERNATE_ID))))
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
                .andExpect(model().attribute("post", hasProperty("id", is(NON_EXISTENT_HIBERNATE_ID))))
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
                .andExpect(view().name(REDIRECT_TO + HOME_PAGE));

        ArgumentCaptor<Post> formObjectArgument = ArgumentCaptor.forClass(Post.class);
        verify(postService, times(1)).createPost(formObjectArgument.capture());
        verifyNoMoreInteractions(postService);

        Post formPost = formObjectArgument.getValue();

        assertThat(formPost.getId(), is(NON_EXISTENT_HIBERNATE_ID));
        assertThat(formPost.getAuthor(), nullValue());
        assertThat(formPost.getTitle(), is("sampleTitle"));
        assertThat(formPost.getText(), is("sampleText"));
        assertThat(formPost.getCreatedDate(), nullValue());
        assertThat(formPost.getPublishedDate(), nullValue());
    }

    @Test
    public void testInitEditPostForm() throws Exception {
        mockMvc.perform(get("/post/{postId}/edit", FIRST_PUBLISHED_POST_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", firstPublishedPost))
                .andExpect(model().attribute("post", hasProperty("id", is(FIRST_PUBLISHED_POST_ID))))
                .andExpect(model().attribute("post", hasProperty("author", is(firstUser))))
                .andExpect(model().attribute("post", hasProperty("title", is("title1"))))
                .andExpect(model().attribute("post", hasProperty("text", is("text1"))))
                .andExpect(model().attribute("post", hasProperty("createdDate", is(CREATED_DATE))))
                .andExpect(model().attribute("post", hasProperty("publishedDate", is(PUBLISHED_DATE))))
                .andExpect(forwardedUrl(VIEWS_POST_FORM))
                .andExpect(view().name(VIEWS_POST_FORM));

        verify(postService, times(1)).read(FIRST_PUBLISHED_POST_ID);
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void testProcessEditPostFormHasErrors() throws Exception {
        mockMvc.perform(post("/post/{postId}/edit", FIRST_PUBLISHED_POST_ID)
                .param("title", "")
                .param("text", "")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("post"))
                .andExpect(model().attributeHasFieldErrors("post", "title"))
                .andExpect(model().attributeHasFieldErrors("post", "text"))
                .andExpect(model().attribute("post", hasProperty("id", is(NON_EXISTENT_HIBERNATE_ID))))
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
    public void testProcessEditPostFormSuccess() throws Exception {
        mockMvc.perform(post("/post/{postId}/edit", FIRST_PUBLISHED_POST_ID)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "sampleTitle")
                .param("text", "sampleText")
                .sessionAttr("post", postService.read(FIRST_PUBLISHED_POST_ID))
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(HOME_PAGE))
                .andExpect(view().name(REDIRECT_TO + HOME_PAGE));
//
//        ArgumentCaptor<Post> formObjectArgument = ArgumentCaptor.forClass(Post.class);
//        verify(postService, times(1)).updatePost(formObjectArgument.capture());
//        verifyNoMoreInteractions(postService);
//
//        Post formPost = formObjectArgument.getValue();
//
//        assertThat(formPost.getId(), is(0L));
//        assertThat(formPost.getAuthor(), nullValue());
//        assertThat(formPost.getTitle(), is("sampleTitle"));
//        assertThat(formPost.getText(), is("sampleText"));
//        assertThat(formPost.getCreatedDate(), nullValue());
//        assertThat(formPost.getPublishedDate(), nullValue());
//
//
//        mockMvc.perform(post("/post/{postId}/edit", 1L)
//                .param("title", "newTitle1")
//                .param("text", "newText1")
//        )
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name(REDIRECT_TO_HOME_PAGE));
    }

    @Test
    public void testPublishPost() throws Exception {
        System.out.println("-dupa " + firstDraftPost + " " + firstDraftPost.getPublishedDate());
        verify(postService, times(0)).read(DRAFT_POST_ID);

        mockMvc.perform(get("/post/{postId}/publish", DRAFT_POST_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", firstDraftPost))
                .andExpect(view().name(VIEWS_POST_DETAIL));
        System.out.println("-dupa " + firstDraftPost + " " + firstDraftPost.getPublishedDate());
        verify(postService, times(1)).read(DRAFT_POST_ID);
    }

    @Test
    public void testRemovePost() throws Exception {
        mockMvc.perform(get("/post/{postId}/remove", FIRST_PUBLISHED_POST_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT_TO + HOME_PAGE));
    }

    @Test
    public void testInitCreateCommentForm() throws Exception {
        mockMvc.perform(get("/post/{postId}/comment", FIRST_PUBLISHED_POST_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("comment"))
                .andExpect(model().attribute("comment", hasProperty("id", is(NON_EXISTENT_HIBERNATE_ID))))
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
        mockMvc.perform(post("/post/{postId}/comment", FIRST_PUBLISHED_POST_ID)
                .param("author", "")
                .param("text", "")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("comment"))
                .andExpect(model().attributeHasFieldErrors("comment", "author"))
                .andExpect(model().attributeHasFieldErrors("comment", "text"))
                .andExpect(model().attribute("comment", hasProperty("id", is(NON_EXISTENT_HIBERNATE_ID))))
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
        mockMvc.perform(post("/post/{postId}/comment", FIRST_PUBLISHED_POST_ID)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("author", "sampleAuthor")
                .param("text", "sampleText")
                .sessionAttr("comment", new Comment())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(POST_DETAIL_PAGE + "/" + FIRST_PUBLISHED_POST_ID))
                .andExpect(view().name(REDIRECT_TO + POST_DETAIL_PAGE + "/" + FIRST_PUBLISHED_POST_ID));

        ArgumentCaptor<Comment> formObjectArgument = ArgumentCaptor.forClass(Comment.class);
        verify(postService, times(1)).addCommentToPost(formObjectArgument.capture(), eq(FIRST_PUBLISHED_POST_ID));
        verifyNoMoreInteractions(postService);

        Comment formComment = formObjectArgument.getValue();

        assertThat(formComment.getId(), is(NON_EXISTENT_HIBERNATE_ID));
        assertThat(formComment.getAuthor(), is("sampleAuthor"));
        assertThat(formComment.getText(), is("sampleText"));
        assertThat(formComment.getCreatedDate(), nullValue());
        assertThat(formComment.isApprovedComment(), is(false));
        assertThat(formComment.getPost(), nullValue());
    }
}

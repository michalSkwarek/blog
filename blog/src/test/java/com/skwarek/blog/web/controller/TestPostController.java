package com.skwarek.blog.web.controller;

import com.skwarek.blog.MyEmbeddedDatabase;
import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.data.entity.User;
import com.skwarek.blog.service.PostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
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
    private final static String POST_PAGE = "/post";

    private final static long ZERO_HIBERNATE_ID = 0;

    private final static long FIRST_PUBLISHED_POST_ID = 1;
    private final static long SECOND_PUBLISHED_POST_ID = 2;
    private final static long DRAFT_POST_ID = 3;
    private final static long NON_EXISTENT_POST_ID = 1001;

    private final static long APPROVED_COMMENT_ID = 1;
    private final static long NOT_APPROVED_COMMENT_ID = 2;

    private final static Date CREATED_DATE = MyEmbeddedDatabase.getCreatedDate();
    private final static Date PUBLISHED_DATE = MyEmbeddedDatabase.getPublishedDate();

    private User firstAuthor;
    private User secondAuthor;

    private Post firstPublishedPost;
    private Post secondPublishedPost;
    private Post draftPost;

    private Comment approvedComment;
    private Comment notApprovedComment;

    private PostService postService;
    private PostController postController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MyEmbeddedDatabase myDB = new MyEmbeddedDatabase();

        this.firstAuthor = myDB.getUser_no_1();
        this.secondAuthor = myDB.getUser_no_2();

        this.firstPublishedPost = myDB.getPost_no_1();
        this.secondPublishedPost = myDB.getPost_no_2();
        this.draftPost = myDB.getPost_no_3();

        this.approvedComment = myDB.getComment_no_1();
        this.notApprovedComment = myDB.getComment_no_2();

        this.postService = mock(PostService.class);

        this.postController = new PostController(this.postService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.postController)
                .build();
    }

    @Test
    public void showPublishedPosts_ShouldAddPostEntriesToModelAndRenderPostListView() throws Exception {
        given(this.postService.findAllPublishedPosts()).willReturn(Arrays.asList(firstPublishedPost, secondPublishedPost));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("posts", Arrays.asList(firstPublishedPost, secondPublishedPost)))
                .andExpect(model().attribute("posts", hasSize(2)))
                .andExpect(model().attribute("posts", hasItem(
                        allOf(
                                hasProperty("id", is(FIRST_PUBLISHED_POST_ID)),
                                hasProperty("author", is(firstAuthor)),
                                hasProperty("title", is("title1")),
                                hasProperty("text", is("text1")),
                                hasProperty("createdDate", is(CREATED_DATE)),
                                hasProperty("publishedDate", is(PUBLISHED_DATE))
                        )
                )))
                .andExpect(model().attribute("posts", hasItem(
                        allOf(
                                hasProperty("id", is(SECOND_PUBLISHED_POST_ID)),
                                hasProperty("author", is(secondAuthor)),
                                hasProperty("title", is("title2")),
                                hasProperty("text", is("text2")),
                                hasProperty("createdDate", is(CREATED_DATE)),
                                hasProperty("publishedDate", is(PUBLISHED_DATE))
                        )
                )))
                .andExpect(forwardedUrl(VIEWS_POST_LIST))
                .andExpect(view().name(VIEWS_POST_LIST));

        verify(postService, times(1)).findAllPublishedPosts();
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void showDrafts_ShouldAddDraftEntriesToModelAndRenderDraftListView() throws Exception {
        given(this.postService.findAllDrafts()).willReturn(Collections.singletonList(draftPost));

        mockMvc.perform(get("/drafts"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("posts", Collections.singletonList(draftPost)))
                .andExpect(model().attribute("posts", hasSize(1)))
                .andExpect(model().attribute("posts", hasItem(
                        allOf(
                                hasProperty("id", is(DRAFT_POST_ID)),
                                hasProperty("author", is(firstAuthor)),
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
    public void showPost_PostEntryNotFound_ShouldRenderEmptyView() throws Exception {
        mockMvc.perform(get("/post/{postId}", NON_EXISTENT_POST_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("post"))
                .andExpect(model().attribute("post", nullValue()))
                .andExpect(forwardedUrl(VIEWS_POST_DETAIL))
                .andExpect(view().name(VIEWS_POST_DETAIL));

        verify(postService, times(1)).read(NON_EXISTENT_POST_ID);
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void showPost_PostEntryFound_ShouldAddPostEntryToModelAndRenderPostEntryView() throws Exception {
        given(this.postService.read(FIRST_PUBLISHED_POST_ID)).willReturn(firstPublishedPost);

        mockMvc.perform(get("/post/{postId}", FIRST_PUBLISHED_POST_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", firstPublishedPost))
                .andExpect(model().attribute("post", hasProperty("id", is(FIRST_PUBLISHED_POST_ID))))
                .andExpect(model().attribute("post", hasProperty("author", is(firstAuthor))))
                .andExpect(model().attribute("post", hasProperty("title", is("title1"))))
                .andExpect(model().attribute("post", hasProperty("text", is("text1"))))
                .andExpect(model().attribute("post", hasProperty("createdDate", is(CREATED_DATE))))
                .andExpect(model().attribute("post", hasProperty("publishedDate", is(PUBLISHED_DATE))))
                .andExpect(forwardedUrl(VIEWS_POST_DETAIL))
                .andExpect(view().name(VIEWS_POST_DETAIL));

        verify(postService, times(1)).read(FIRST_PUBLISHED_POST_ID);
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void showPost_PostAndCommentsEntryFound_ShouldAddCommentsEntryToModelAndRenderCommentsEntryView() throws Exception {
        given(this.postService.read(FIRST_PUBLISHED_POST_ID)).willReturn(firstPublishedPost);

        mockMvc.perform(get("/post/{postId}", FIRST_PUBLISHED_POST_ID))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attribute("comments", Arrays.asList(approvedComment, notApprovedComment)))
                .andExpect(model().attribute("comments", hasSize(2)))
                .andExpect(model().attribute("comments", hasItem(
                        allOf(
                                hasProperty("id", is(APPROVED_COMMENT_ID)),
                                hasProperty("author", is("author1")),
                                hasProperty("text", is("commentText1")),
                                hasProperty("createdDate", is(CREATED_DATE)),
                                hasProperty("approvedComment", is(true)),
                                hasProperty("post", is(firstPublishedPost))
                        )
                )))
                .andExpect(model().attribute("comments", hasItem(
                        allOf(
                                hasProperty("id", is(NOT_APPROVED_COMMENT_ID)),
                                hasProperty("author", is("author2")),
                                hasProperty("text", is("commentText2")),
                                hasProperty("createdDate", is(CREATED_DATE)),
                                hasProperty("approvedComment", is(false)),
                                hasProperty("post", is(firstPublishedPost))
                        )
                )));
    }

    @Test
    public void initCreatePostForm_ShouldAddPostToModelAndRenderEmptyPostFormView() throws Exception {
        mockMvc.perform(get("/post/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", hasProperty("id", is(ZERO_HIBERNATE_ID))))
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
    public void processCreatePostForm_TitleAndTextAreEmpty_ShouldHasErrors() throws Exception {
        mockMvc.perform(post("/post/new")
                .param("title", "")
                .param("text", "")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attributeHasErrors("post"))
                .andExpect(model().attributeHasFieldErrors("post", "title"))
                .andExpect(model().attributeHasFieldErrors("post", "text"))
                .andExpect(model().attribute("post", hasProperty("id", is(ZERO_HIBERNATE_ID))))
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
    public void processCreatePostForm_ShouldBeFine() throws Exception {
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

        assertThat(formPost.getId(), is(ZERO_HIBERNATE_ID));
        assertThat(formPost.getAuthor(), nullValue());
        assertThat(formPost.getTitle(), is("sampleTitle"));
        assertThat(formPost.getText(), is("sampleText"));
        assertThat(formPost.getCreatedDate(), nullValue());
        assertThat(formPost.getPublishedDate(), nullValue());
    }

    @Test
    public void initEditPostForm_ShouldAddPostToModelAndRenderPostFormView() throws Exception {
        given(this.postService.read(FIRST_PUBLISHED_POST_ID)).willReturn(firstPublishedPost);

        mockMvc.perform(get("/post/{postId}/edit", FIRST_PUBLISHED_POST_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", firstPublishedPost))
                .andExpect(model().attribute("post", hasProperty("id", is(FIRST_PUBLISHED_POST_ID))))
                .andExpect(model().attribute("post", hasProperty("author", is(firstAuthor))))
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
    public void processEditPostForm_TitleAndTextAreEmpty_ShouldHasErrors() throws Exception {
        mockMvc.perform(post("/post/{postId}/edit", FIRST_PUBLISHED_POST_ID)
                .param("title", "")
                .param("text", "")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attributeHasErrors("post"))
                .andExpect(model().attributeHasFieldErrors("post", "title"))
                .andExpect(model().attributeHasFieldErrors("post", "text"))
                .andExpect(model().attribute("post", hasProperty("id", is(ZERO_HIBERNATE_ID))))
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
    public void processEditPostForm_ShouldBeFine() throws Exception {
        mockMvc.perform(post("/post/{postId}/edit", FIRST_PUBLISHED_POST_ID)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "sampleTitle")
                .param("text", "sampleText")
                .sessionAttr("post", new Post())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(HOME_PAGE))
                .andExpect(view().name(REDIRECT_TO + HOME_PAGE));

        ArgumentCaptor<Post> formObjectArgument = ArgumentCaptor.forClass(Post.class);
        verify(postService, times(1)).updatePost(formObjectArgument.capture());
        verifyNoMoreInteractions(postService);

        Post formPost = formObjectArgument.getValue();

        assertThat(formPost.getId(), is(ZERO_HIBERNATE_ID));
        assertThat(formPost.getAuthor(), nullValue());
        assertThat(formPost.getTitle(), is("sampleTitle"));
        assertThat(formPost.getText(), is("sampleText"));
        assertThat(formPost.getCreatedDate(), nullValue());
        assertThat(formPost.getPublishedDate(), nullValue());
    }

    @Test
    public void publishPost_ShouldAddPublishedDateToPost() throws Exception {
        given(postService.read(DRAFT_POST_ID)).willReturn(draftPost);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                draftPost.setPublishedDate(PUBLISHED_DATE);
                return null;
            }
        }).when(this.postService).publishPost(draftPost);

        mockMvc.perform(get("/post/{postId}/publish", DRAFT_POST_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", hasProperty("id", is(DRAFT_POST_ID))))
                .andExpect(model().attribute("post", hasProperty("author", is(firstAuthor))))
                .andExpect(model().attribute("post", hasProperty("title", is("title3"))))
                .andExpect(model().attribute("post", hasProperty("text", is("text3"))))
                .andExpect(model().attribute("post", hasProperty("createdDate", is(CREATED_DATE))))
                .andExpect(model().attribute("post", hasProperty("publishedDate", is(PUBLISHED_DATE))))
                .andExpect(forwardedUrl(VIEWS_POST_DETAIL))
                .andExpect(view().name(VIEWS_POST_DETAIL));

        verify(postService, times(1)).read(DRAFT_POST_ID);
        verify(postService, times(1)).publishPost(draftPost);
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void removePost_ShouldRemovePost() throws Exception {
        mockMvc.perform(get("/post/{postId}/remove", FIRST_PUBLISHED_POST_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(HOME_PAGE))
                .andExpect(view().name(REDIRECT_TO + HOME_PAGE));

        verify(postService, times(1)).removePost(FIRST_PUBLISHED_POST_ID);
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void initCreateCommentForm_ShouldAddCommentToModelAndRenderEmptyCommentFormView() throws Exception {
        mockMvc.perform(get("/post/{postId}/comment", FIRST_PUBLISHED_POST_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("comment"))
                .andExpect(model().attribute("comment", hasProperty("id", is(ZERO_HIBERNATE_ID))))
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
    public void processCreateCommentForm_AuthorAndTextAreEmpty_ShouldHasErrors() throws Exception {
        mockMvc.perform(post("/post/{postId}/comment", FIRST_PUBLISHED_POST_ID)
                .param("author", "")
                .param("text", "")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("comment"))
                .andExpect(model().attributeHasErrors("comment"))
                .andExpect(model().attributeHasFieldErrors("comment", "author"))
                .andExpect(model().attributeHasFieldErrors("comment", "text"))
                .andExpect(model().attribute("comment", hasProperty("id", is(ZERO_HIBERNATE_ID))))
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
    public void processCreateCommentForm_ShouldBeFine() throws Exception {
        mockMvc.perform(post("/post/{postId}/comment", FIRST_PUBLISHED_POST_ID)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("author", "sampleAuthor")
                .param("text", "sampleText")
                .sessionAttr("comment", new Comment())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(POST_PAGE + "/" + FIRST_PUBLISHED_POST_ID))
                .andExpect(view().name(REDIRECT_TO + POST_PAGE + "/" + FIRST_PUBLISHED_POST_ID));

        ArgumentCaptor<Comment> formObjectArgument = ArgumentCaptor.forClass(Comment.class);
        verify(postService, times(1)).addCommentToPost(formObjectArgument.capture(), eq(FIRST_PUBLISHED_POST_ID));
        verifyNoMoreInteractions(postService);

        Comment formComment = formObjectArgument.getValue();

        assertThat(formComment.getId(), is(ZERO_HIBERNATE_ID));
        assertThat(formComment.getAuthor(), is("sampleAuthor"));
        assertThat(formComment.getText(), is("sampleText"));
        assertThat(formComment.getCreatedDate(), nullValue());
        assertThat(formComment.isApprovedComment(), is(false));
        assertThat(formComment.getPost(), nullValue());
    }
}

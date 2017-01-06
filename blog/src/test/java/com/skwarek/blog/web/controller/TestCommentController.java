package com.skwarek.blog.web.controller;

import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.service.CommentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Michal on 05/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestCommentController {

    private final static String REDIRECT_TO = "redirect:";
    private final static String POST_PAGE = "/post";

    private final static long PUBLISHED_POST_ID = 1;

    private final static long APPROVED_COMMENT_ID = 1;
    private final static long NOT_APPROVED_COMMENT_ID = 2;

    private final static Date CREATED_DATE = new GregorianCalendar(2000, Calendar.JANUARY, 11, 11, 22, 33).getTime();

    private Post publishedPost;

    private Comment approvedComment;
    private Comment notApprovedComment;

    private CommentService commentService;
    private CommentController commentController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.publishedPost = new Post();
        this.publishedPost.setId(PUBLISHED_POST_ID);

        this.approvedComment = new Comment();
        this.approvedComment.setId(APPROVED_COMMENT_ID);
        this.approvedComment.setAuthor("author1");
        this.approvedComment.setText("commentText1");
        this.approvedComment.setCreatedDate(CREATED_DATE);
        this.approvedComment.setApprovedComment(true);
        this.approvedComment.setPost(publishedPost);

        this.notApprovedComment = new Comment();
        this.notApprovedComment.setId(NOT_APPROVED_COMMENT_ID);
        this.notApprovedComment.setAuthor("author2");
        this.notApprovedComment.setText("commentText2");
        this.notApprovedComment.setCreatedDate(CREATED_DATE);
        this.notApprovedComment.setApprovedComment(false);
        this.notApprovedComment.setPost(publishedPost);

        this.publishedPost.setComments(Arrays.asList(approvedComment, notApprovedComment));

        this.commentService = mock(CommentService.class);
        this.commentController = new CommentController(this.commentService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.commentController)
                .build();

        given(commentService.read(NOT_APPROVED_COMMENT_ID)).willReturn(notApprovedComment);

        given(commentService.read(APPROVED_COMMENT_ID)).willReturn(approvedComment);
    }

    @Test
    public void approveComment_ShouldSetApproveFlagToComment() throws Exception {
        mockMvc.perform(get("/comment/{commentId}/approve", NOT_APPROVED_COMMENT_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(POST_PAGE + "/" + PUBLISHED_POST_ID))
                .andExpect(view().name(REDIRECT_TO + POST_PAGE + "/" + PUBLISHED_POST_ID));

        verify(commentService, times(1)).read(NOT_APPROVED_COMMENT_ID);
        verify(commentService, times(1)).approve(notApprovedComment);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void removeComment_ShouldRemoveComment() throws Exception {
        mockMvc.perform(get("/comment/{commentId}/remove", APPROVED_COMMENT_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(POST_PAGE +"/" + PUBLISHED_POST_ID))
                .andExpect(view().name(REDIRECT_TO + POST_PAGE + "/" + PUBLISHED_POST_ID));

        verify(commentService, times(1)).read(APPROVED_COMMENT_ID);
        verify(commentService, times(1)).removeComment(APPROVED_COMMENT_ID);
        verifyNoMoreInteractions(commentService);
    }
}

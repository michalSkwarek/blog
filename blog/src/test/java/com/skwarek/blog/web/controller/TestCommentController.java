package com.skwarek.blog.web.controller;

import com.skwarek.blog.MyEmbeddedDatabase;
import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.service.CommentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

    private final static long FIRST_PUBLISHED_POST_ID = 1;

    private final static long APPROVED_COMMENT_ID = 1;
    private final static long NOT_APPROVED_COMMENT_ID = 2;

    private Comment approvedComment;
    private Comment notApprovedComment;

    private CommentService commentService;
    private CommentController commentController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MyEmbeddedDatabase myDB = new MyEmbeddedDatabase();

        this.approvedComment = myDB.getComment_no_1();
        this.notApprovedComment = myDB.getComment_no_2();

        this.commentService = mock(CommentService.class);

        this.commentController = new CommentController(this.commentService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.commentController)
                .build();
    }

    @Test
    public void approveComment_ShouldSetApproveFlagToComment() throws Exception {
        given(commentService.read(NOT_APPROVED_COMMENT_ID)).willReturn(notApprovedComment);

        mockMvc.perform(get("/comment/{commentId}/approve", NOT_APPROVED_COMMENT_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(POST_PAGE + "/" + FIRST_PUBLISHED_POST_ID))
                .andExpect(view().name(REDIRECT_TO + POST_PAGE + "/" + FIRST_PUBLISHED_POST_ID));

        verify(commentService, times(1)).read(NOT_APPROVED_COMMENT_ID);
        verify(commentService, times(1)).approve(notApprovedComment);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void removeComment_ShouldRemoveComment() throws Exception {
        given(commentService.read(APPROVED_COMMENT_ID)).willReturn(approvedComment);

        mockMvc.perform(get("/comment/{commentId}/remove", APPROVED_COMMENT_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(POST_PAGE +"/" + FIRST_PUBLISHED_POST_ID))
                .andExpect(view().name(REDIRECT_TO + POST_PAGE + "/" + FIRST_PUBLISHED_POST_ID));

        verify(commentService, times(1)).read(APPROVED_COMMENT_ID);
        verify(commentService, times(1)).removeComment(APPROVED_COMMENT_ID);
        verifyNoMoreInteractions(commentService);
    }
}

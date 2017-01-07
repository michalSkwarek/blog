package com.skwarek.blog.service;

import com.skwarek.blog.configuration.ApplicationContextConfiguration;
import com.skwarek.blog.data.dao.CommentDao;
import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.service.impl.CommentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by Michal on 05/01/2017.
 */
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
public class TestCommentService {

    private final static long PUBLISHED_POST_ID = 1;

    private final static long APPROVED_COMMENT_ID = 1;
    private final static long NOT_APPROVED_COMMENT_ID = 2;

    private final static Date CREATED_DATE = new GregorianCalendar(2000, Calendar.JANUARY, 11, 11, 22, 33).getTime();

    private Post publishedPost;

    private Comment approvedComment;
    private Comment notApprovedComment;

    @Autowired
    private CommentService commentService;

    private CommentDao commentDao;

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

        this.commentDao = mock(CommentDao.class);

        this.commentService = new CommentServiceImpl(commentDao);

        doNothing().when(this.commentDao).update(approvedComment);
        given(commentDao.removeComment(APPROVED_COMMENT_ID)).willReturn(true);
    }

    @Test
    public void testApproveComment() throws Exception {
        commentService.approve(notApprovedComment);

        assertEquals(true, notApprovedComment.isApprovedComment());

        verify(commentDao, times(1)).update(notApprovedComment);
        verifyNoMoreInteractions(commentDao);
    }

    @Test
    public void testRemoveComment() throws Exception {
        commentService.removeComment(APPROVED_COMMENT_ID);

        verify(commentDao, times(1)).removeComment(APPROVED_COMMENT_ID);
        verifyNoMoreInteractions(commentDao);
    }
}

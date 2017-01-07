package com.skwarek.blog.service;

import com.skwarek.blog.MyEmbeddedDatabase;
import com.skwarek.blog.configuration.ApplicationContextConfiguration;
import com.skwarek.blog.data.dao.CommentDao;
import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.service.impl.CommentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Created by Michal on 05/01/2017.
 */
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
public class TestCommentService {

    private final static long APPROVED_COMMENT_ID = 1;

    private Comment approvedComment;
    private Comment notApprovedComment;

    private CommentDao commentDao;

    @Autowired
    private CommentService commentService;

    @Before
    public void setUp() {
        MyEmbeddedDatabase myDB = new MyEmbeddedDatabase();

        this.approvedComment = myDB.getComment_no_1();
        this.notApprovedComment = myDB.getComment_no_2();

        this.commentDao = mock(CommentDao.class);

        this.commentService = new CommentServiceImpl(commentDao);
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
        given(commentDao.removeComment(APPROVED_COMMENT_ID)).willReturn(true);

        commentService.removeComment(APPROVED_COMMENT_ID);

        verify(commentDao, times(1)).removeComment(APPROVED_COMMENT_ID);
        verifyNoMoreInteractions(commentDao);
    }
}

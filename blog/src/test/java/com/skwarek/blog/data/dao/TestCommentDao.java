package com.skwarek.blog.data.dao;

import com.skwarek.blog.configuration.ApplicationContextConfiguration;
import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.data.entity.Post;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Michal on 02/01/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
@WebAppConfiguration
@Transactional
public class TestCommentDao {

    private final static long PUBLISHED_POST_ID = 1;

    private final static long APPROVED_COMMENT_ID = 1;
    private final static long NOT_APPROVED_COMMENT_ID = 2;

    private final static Date CREATED_DATE = new GregorianCalendar(2000, Calendar.JANUARY, 11, 11, 22, 33).getTime();

    private Post publishedPost;

    private Comment newComment;
    private Comment approvedComment;
    private Comment notApprovedComment;

    @Autowired
    private CommentDao commentDao;

    @Before
    public void setUp() {
        this.publishedPost = new Post();
//        this.publishedPost.setId(PUBLISHED_POST_ID);

        this.newComment = new Comment();
        this.newComment.setAuthor("newAuthor");
        this.newComment.setText("newText");
        this.newComment.setCreatedDate(CREATED_DATE);
        this.newComment.setApprovedComment(false);
        this.newComment.setPost(publishedPost);

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

        this.commentDao.create(approvedComment);
        this.commentDao.create(notApprovedComment);
    }

    @Test
    public void testCreateComment() throws Exception {
        int oldSize = commentDao.findAll().size();
        commentDao.create(newComment);
        assertEquals(oldSize + 1, commentDao.findAll().size());

        long newCommentId = 3;

        Comment found = commentDao.read(newCommentId);

        assertNotNull(found);
        assertEquals(newCommentId, found.getId());
        assertEquals(newComment.getAuthor(), found.getAuthor());
        assertEquals(newComment.getText(), found.getText());
        assertEquals(newComment.getCreatedDate(), found.getCreatedDate());
        assertEquals(newComment.isApprovedComment(), found.isApprovedComment());
        assertEquals(newComment.getPost(), found.getPost());
    }

    @Test
    public void testReadComment() throws Exception {
        Comment found = commentDao.read(APPROVED_COMMENT_ID);

        assertNotNull(found);
        assertEquals(approvedComment.getId(), found.getId());
        assertEquals(approvedComment.getAuthor(), found.getAuthor());
        assertEquals(approvedComment.getText(), found.getText());
        assertEquals(approvedComment.getCreatedDate(), found.getCreatedDate());
        assertEquals(approvedComment.isApprovedComment(), found.isApprovedComment());
        assertEquals(approvedComment.getPost(), found.getPost());
    }

    @Test
    public void testUpdateComment() throws Exception {
        approvedComment.setText("editedText");
        commentDao.update(approvedComment);

        Comment found = commentDao.read(APPROVED_COMMENT_ID);

        assertNotNull(found);
        assertEquals(approvedComment.getId(), found.getId());
        assertEquals(approvedComment.getAuthor(), found.getAuthor());
        assertEquals(approvedComment.getText(), found.getText());
        assertEquals(approvedComment.getCreatedDate(), found.getCreatedDate());
        assertEquals(approvedComment.isApprovedComment(), found.isApprovedComment());
        assertEquals(approvedComment.getPost(), found.getPost());
    }

    @Test
    public void testDeleteComment() throws Exception {
        int oldSize = commentDao.findAll().size();
        commentDao.delete(approvedComment);
        assertEquals(oldSize - 1, commentDao.findAll().size());

        Comment found = commentDao.read(APPROVED_COMMENT_ID);

        assertNull(found);
    }

    @Test
    public void testFindAll() throws Exception {
        List<Comment> found = commentDao.findAll();

        assertEquals(1 + 1, found.size());

        assertEquals(approvedComment.getId(), found.get(0).getId());
        assertEquals(approvedComment.getAuthor(), found.get(0).getAuthor());
        assertEquals(approvedComment.getText(), found.get(0).getText());
        assertEquals(approvedComment.getCreatedDate(), found.get(0).getCreatedDate());
        assertEquals(approvedComment.isApprovedComment(), found.get(0).isApprovedComment());
        assertEquals(approvedComment.getPost(), found.get(0).getPost());

        assertEquals(approvedComment.getId(), found.get(1).getId());
        assertEquals(approvedComment.getAuthor(), found.get(1).getAuthor());
        assertEquals(approvedComment.getText(), found.get(1).getText());
        assertEquals(approvedComment.getCreatedDate(), found.get(1).getCreatedDate());
        assertEquals(approvedComment.isApprovedComment(), found.get(1).isApprovedComment());
        assertEquals(approvedComment.getPost(), found.get(1).getPost());
    }

    @Test
    public void testRemoveComment() throws Exception {
        List<Comment> foundAllComments = commentDao.findAll();
        assertEquals(3, foundAllComments.size());

//        Post post = foundAllComments.get(0);
//        post.getComments().clear();

        commentDao.removeComment(APPROVED_COMMENT_ID);

        List<Comment> foundComments = commentDao.findAll();
        assertEquals(2, foundComments.size());
    }
}


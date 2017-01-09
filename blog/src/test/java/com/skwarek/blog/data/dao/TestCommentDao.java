package com.skwarek.blog.data.dao;

import com.skwarek.blog.MyEmbeddedDatabase;
import com.skwarek.blog.configuration.ForTestsApplicationContextConfiguration;
import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.data.entity.Post;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Michal on 02/01/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ForTestsApplicationContextConfiguration.class)
@Transactional
public class TestCommentDao {

    private final static long FIRST_PUBLISHED_POST_ID = 1;

    private final static long APPROVED_COMMENT_ID = 1;

    private final static Date CREATED_DATE = MyEmbeddedDatabase.getCreatedDate();

    private Post firstPublishedPost;

    private Comment newComment;

    @Autowired
    private PostDao postDao;

    @Autowired
    private CommentDao commentDao;

    @Before
    public void setUp() {
        this.firstPublishedPost = postDao.read(FIRST_PUBLISHED_POST_ID);

        this.newComment = new Comment();
        this.newComment.setAuthor("newAuthor");
        this.newComment.setText("newText");
        this.newComment.setCreatedDate(CREATED_DATE);
        this.newComment.setApprovedComment(false);
        this.newComment.setPost(postDao.read(FIRST_PUBLISHED_POST_ID));
    }

    @Test
    public void testCreateComment() throws Exception {
        assertEquals(2, commentDao.findAll().size());
        assertEquals(3, postDao.findAll().size());

        commentDao.create(newComment);

        long newCommentId = 3;
        Comment found = commentDao.read(newCommentId);

        assertNotNull(found);
        assertEquals(newCommentId, found.getId());
        assertEquals("newAuthor", found.getAuthor());
        assertEquals("newText", found.getText());
        assertEquals(CREATED_DATE, found.getCreatedDate());
        assertEquals(false, found.isApprovedComment());
        assertEquals(firstPublishedPost, found.getPost());

        assertEquals(3, commentDao.findAll().size());
        assertEquals(3, postDao.findAll().size());
    }

    @Test
    public void testReadComment() throws Exception {
        Comment found = commentDao.read(APPROVED_COMMENT_ID);

        assertNotNull(found);
        assertEquals(APPROVED_COMMENT_ID, found.getId());
        assertEquals("author1", found.getAuthor());
        assertEquals("commentText1", found.getText());
        assertEquals(CREATED_DATE, found.getCreatedDate());
        assertEquals(true, found.isApprovedComment());
        assertEquals(firstPublishedPost, found.getPost());
    }

    @Test
    public void testUpdateComment() throws Exception {
        assertEquals(2, commentDao.findAll().size());
        assertEquals(3, postDao.findAll().size());

        Comment toUpdate = commentDao.read(APPROVED_COMMENT_ID);
        toUpdate.setText("editedText");
        commentDao.update(toUpdate);

        Comment found = commentDao.read(APPROVED_COMMENT_ID);

        assertNotNull(found);
        assertEquals(APPROVED_COMMENT_ID, found.getId());
        assertEquals("author1", found.getAuthor());
        assertEquals("editedText", found.getText());
        assertEquals(CREATED_DATE, found.getCreatedDate());
        assertEquals(true, found.isApprovedComment());
        assertEquals(firstPublishedPost, found.getPost());

        assertEquals(2, commentDao.findAll().size());
        assertEquals(3, postDao.findAll().size());
    }

    @Test
    public void testFindAll() throws Exception {
        List<Comment> found = commentDao.findAll();

        assertEquals(1 + 1, found.size());

        assertEquals(1, found.get(0).getId());
        assertEquals("author1", found.get(0).getAuthor());
        assertEquals("commentText1", found.get(0).getText());
        assertEquals(CREATED_DATE, found.get(0).getCreatedDate());
        assertEquals(true, found.get(0).isApprovedComment());
        assertEquals(firstPublishedPost, found.get(0).getPost());

        assertEquals(2, found.get(1).getId());
        assertEquals("author2", found.get(1).getAuthor());
        assertEquals("commentText2", found.get(1).getText());
        assertEquals(CREATED_DATE, found.get(1).getCreatedDate());
        assertEquals(false, found.get(1).isApprovedComment());
        assertEquals(firstPublishedPost, found.get(1).getPost());
    }

    @Test
    public void testRemoveComment() throws Exception {
        assertEquals(2, commentDao.findAll().size());
        assertEquals(3, postDao.findAll().size());

        commentDao.removeComment(APPROVED_COMMENT_ID);

        assertEquals(1, commentDao.findAll().size());
        assertEquals(3, postDao.findAll().size());
    }
}


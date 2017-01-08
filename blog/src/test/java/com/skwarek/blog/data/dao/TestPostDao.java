package com.skwarek.blog.data.dao;

import com.skwarek.blog.configuration.ForTestsApplicationContextConfiguration;
import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.data.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Created by Michal on 02/01/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ForTestsApplicationContextConfiguration.class)
@WebAppConfiguration
@Transactional
public class TestPostDao {

    private final static long FIRST_USER_ID = 1;
    private final static long SECOND_USER_ID = 2;

    private final static long FIRST_PUBLISHED_POST_ID = 1;
    private final static long SECOND_PUBLISHED_POST_ID = 2;
    private final static long DRAFT_POST_ID = 3;

    private final static long APPROVED_COMMENT_ID = 1;
    private final static long NOT_APPROVED_COMMENT_ID = 2;

    private final static Date CREATED_DATE = new GregorianCalendar(2000, Calendar.JANUARY, 11, 11, 22, 33).getTime();
    private final static Date PUBLISHED_DATE = new GregorianCalendar(2000, Calendar.JANUARY, 12, 22, 33, 44).getTime();

    private User firstUser;
    private User secondUser;

    private Post newPost;
    private Post editedFirstPublishedPost;

    private Post firstPublishedPost;
    private Post secondPublishedPost;
    private Post draftPost;

    private Comment newComment;

    private Comment approvedComment;
    private Comment notApprovedComment;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private CommentDao commentDao;

    SecurityContext securityContext;

    @Before
    public void setUp() {
        this.firstUser = userDao.read(FIRST_USER_ID);
        this.secondUser = userDao.read(SECOND_USER_ID);

        this.newPost = new Post();
        this.newPost.setAuthor(firstUser);
        this.newPost.setTitle("newTitle");
        this.newPost.setText("newTest");
        this.newPost.setCreatedDate(CREATED_DATE);

        this.firstPublishedPost = postDao.read(FIRST_PUBLISHED_POST_ID);
        this.secondPublishedPost = postDao.read(SECOND_PUBLISHED_POST_ID);
        this.draftPost = postDao.read(DRAFT_POST_ID);

        this.newComment = new Comment();
        this.newComment.setAuthor("newAuthor");
        this.newComment.setText("newText");

        this.approvedComment = commentDao.read(APPROVED_COMMENT_ID);
        this.notApprovedComment = commentDao.read(NOT_APPROVED_COMMENT_ID);

        Authentication authentication = mock(Authentication.class);
        this.securityContext = mock(SecurityContext.class);

        given(securityContext.getAuthentication()).willReturn(authentication);
        given(authentication.getName()).willReturn(firstUser.getUsername());

//        doNothing().when(this.postDao).update(firstPublishedPost);
//
//        doNothing().when(this.postDao).update(draftPost);
    }

//    @Test
//    public void testCreatePost() throws Exception {
//        assertEquals(2, commentDao.findAll().size());
//        assertEquals(3, postDao.findAll().size());
//
//        commentDao.create(newComment);
//        assertEquals(3, commentDao.findAll().size());
//
//        long newCommentId = 3;
//        Comment found = commentDao.read(newCommentId);
////        Post post = postDao.read(PUBLISHED_POST_ID);
//
//        assertNotNull(found);
//        assertEquals(newCommentId, found.getId());
//        assertEquals("newAuthor", found.getAuthor());
//        assertEquals("newText", found.getText());
//        assertEquals(CREATED_DATE, found.getCreatedDate());
//        assertEquals(false, found.isApprovedComment());
////        assertEquals(post, found.getPost());
//
//        assertEquals(3, commentDao.findAll().size());
//        assertEquals(3, postDao.findAll().size());
//    }

    @Test
    public void testReadPost() throws Exception {
        Post found = postDao.read(FIRST_PUBLISHED_POST_ID);

        assertNotNull(found);
        assertEquals(FIRST_PUBLISHED_POST_ID, found.getId());
        assertEquals(firstUser, found.getAuthor());
        assertEquals("title1", found.getTitle());
        assertEquals("text1", found.getText());
        assertEquals(CREATED_DATE, found.getCreatedDate());
        assertEquals(PUBLISHED_DATE, found.getPublishedDate());
        assertEquals(Arrays.asList(approvedComment, notApprovedComment), found.getComments());
    }

    @Test
    public void testUpdatePost() throws Exception {
        assertEquals(3, postDao.findAll().size());
        assertEquals(2, userDao.findAll().size());
        assertEquals(2, commentDao.findAll().size());

        Post toUpdate = postDao.read(FIRST_PUBLISHED_POST_ID);
        toUpdate.setTitle("editedTitle");
        toUpdate.setText("editedText");
        postDao.update(toUpdate);

        Post found = postDao.read(FIRST_PUBLISHED_POST_ID);

        assertNotNull(found);
        assertEquals(FIRST_PUBLISHED_POST_ID, found.getId());
        assertEquals(firstUser, found.getAuthor());
        assertEquals("editedTitle", found.getTitle());
        assertEquals("editedText", found.getText());
        assertEquals(CREATED_DATE, found.getCreatedDate());
        assertEquals(PUBLISHED_DATE, found.getPublishedDate());
        assertEquals(Arrays.asList(approvedComment, notApprovedComment), found.getComments());

        assertEquals(3, postDao.findAll().size());
        assertEquals(2, userDao.findAll().size());
        assertEquals(2, commentDao.findAll().size());
    }

    @Test
    public void testFindAll() throws Exception {
        List<Post> found = postDao.findAll();

        assertEquals(2 + 1, found.size());

        assertEquals(FIRST_PUBLISHED_POST_ID, found.get(0).getId());
        assertEquals(firstUser, found.get(0).getAuthor());
        assertEquals("title1", found.get(0).getTitle());
        assertEquals("text1", found.get(0).getText());
        assertEquals(CREATED_DATE, found.get(0).getCreatedDate());
        assertEquals(PUBLISHED_DATE, found.get(0).getPublishedDate());
        assertEquals(Arrays.asList(approvedComment, notApprovedComment), found.get(0).getComments());

        assertEquals(SECOND_PUBLISHED_POST_ID, found.get(1).getId());
        assertEquals(secondUser, found.get(1).getAuthor());
        assertEquals("title2", found.get(1).getTitle());
        assertEquals("text2", found.get(1).getText());
        assertEquals(CREATED_DATE, found.get(1).getCreatedDate());
        assertEquals(PUBLISHED_DATE, found.get(1).getPublishedDate());
        assertEquals(Collections.emptyList(), found.get(1).getComments());

        assertEquals(DRAFT_POST_ID, found.get(2).getId());
        assertEquals(firstUser, found.get(2).getAuthor());
        assertEquals("title3", found.get(2).getTitle());
        assertEquals("text3", found.get(2).getText());
        assertEquals(CREATED_DATE, found.get(2).getCreatedDate());
        assertEquals(null, found.get(2).getPublishedDate());
        assertEquals(Collections.emptyList(), found.get(2).getComments());
    }

//    @Test
//    public void testCreatePost() throws Exception {
//        int oldSize = postDao.findAll().size();
//        postDao.create(newPost);
//        assertEquals(oldSize + 1, postDao.findAll().size());
//
//        long newPostId = 4;
//
//        Post found = postDao.read(newPostId);
//
//        assertNotNull(found);
//        assertEquals(newPostId, found.getId());
//        assertEquals(newPost.getAuthor(), found.getAuthor());
//        assertEquals(newPost.getTitle(), found.getTitle());
//        assertEquals(newPost.getText(), found.getText());
//        assertEquals(newPost.getCreatedDate(), found.getCreatedDate());
//        assertEquals(null, found.getPublishedDate());
//        assertEquals(Collections.<Comment>emptyList(), found.getComments());
//    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindAllPublishedPosts() throws Exception {
        List<Post> found = postDao.findAllPublishedPosts();

        assertEquals(2, found.size());

        assertEquals(FIRST_PUBLISHED_POST_ID, found.get(0).getId());
        assertEquals(firstUser, found.get(0).getAuthor());
        assertEquals("title1", found.get(0).getTitle());
        assertEquals("text1", found.get(0).getText());
        assertEquals(CREATED_DATE, found.get(0).getCreatedDate());
        assertEquals(PUBLISHED_DATE, found.get(0).getPublishedDate());
        assertEquals(Arrays.asList(approvedComment, notApprovedComment), found.get(0).getComments());

        assertEquals(SECOND_PUBLISHED_POST_ID, found.get(1).getId());
        assertEquals(secondUser, found.get(1).getAuthor());
        assertEquals("title2", found.get(1).getTitle());
        assertEquals("text2", found.get(1).getText());
        assertEquals(CREATED_DATE, found.get(1).getCreatedDate());
        assertEquals(PUBLISHED_DATE, found.get(1).getPublishedDate());
        assertEquals(Collections.emptyList(), found.get(1).getComments());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindAllDrafts() throws Exception {
        List<Post> found = postDao.findAllDrafts();

        assertEquals(1, found.size());

        assertEquals(DRAFT_POST_ID, found.get(0).getId());
        assertEquals(firstUser, found.get(0).getAuthor());
        assertEquals("title3", found.get(0).getTitle());
        assertEquals("text3", found.get(0).getText());
        assertEquals(CREATED_DATE, found.get(0).getCreatedDate());
        assertEquals(null, found.get(0).getPublishedDate());
        assertEquals(Collections.emptyList(), found.get(0).getComments());
    }

    @Test
    public void testRemovePost() throws Exception {
        assertEquals(3, postDao.findAll().size());
        assertEquals(2, userDao.findAll().size());
        assertEquals(2, commentDao.findAll().size());

        postDao.removePost(FIRST_PUBLISHED_POST_ID);

        assertEquals(2, postDao.findAll().size());
        assertEquals(2, userDao.findAll().size());
        assertEquals(0, commentDao.findAll().size());
    }
}

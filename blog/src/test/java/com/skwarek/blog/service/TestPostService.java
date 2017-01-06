package com.skwarek.blog.service;

import com.skwarek.blog.configuration.ApplicationContextConfiguration;
import com.skwarek.blog.data.dao.CommentDao;
import com.skwarek.blog.data.dao.PostDao;
import com.skwarek.blog.data.dao.UserDao;
import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.data.entity.User;
import com.skwarek.blog.service.impl.PostServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Created by Michal on 02/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
public class TestPostService {

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

    private List<Post> listOfPublishedPosts;
    private List<Post> listOfDraftPosts;

    private Comment newComment;

    private Comment approvedComment;
    private Comment notApprovedComment;

    @Autowired
    private PostService postService;

    private PostDao postDao;
    private CommentDao commentDao;
    private UserDao userDao;

    SecurityContext securityContext;

    @Before
    public void setUp() {
        this.firstUser = new User();
        this.firstUser.setId(FIRST_USER_ID);
        this.firstUser.setUsername("user1");
        this.firstUser.setPassword("pass1");
        this.firstUser.setEnabled(true);
        this.firstUser.setRole("ROLE_ADMIN");

        this.secondUser = new User();
        this.secondUser.setId(SECOND_USER_ID);

        this.newPost = new Post();
        this.newPost.setTitle("newTitle");
        this.newPost.setText("newTest");

        this.editedFirstPublishedPost = new Post();
        this.editedFirstPublishedPost.setId(FIRST_PUBLISHED_POST_ID);
        this.editedFirstPublishedPost.setTitle("editedTitle");
        this.editedFirstPublishedPost.setText("editedTest");

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

        this.draftPost = new Post();
        this.draftPost.setId(DRAFT_POST_ID);
        this.draftPost.setAuthor(firstUser);
        this.draftPost.setTitle("title3");
        this.draftPost.setText("text3");
        this.draftPost.setCreatedDate(CREATED_DATE);

        this.listOfDraftPosts = new ArrayList<>();
        this.listOfDraftPosts.add(draftPost);

        this.newComment = new Comment();
        this.newComment.setAuthor("newAuthor");
        this.newComment.setText("newText");

        this.approvedComment = new Comment();
        this.approvedComment.setId(APPROVED_COMMENT_ID);
        this.approvedComment.setAuthor("author1");
        this.approvedComment.setText("commentText1");
        this.approvedComment.setCreatedDate(CREATED_DATE);
        this.approvedComment.setApprovedComment(true);
        this.approvedComment.setPost(firstPublishedPost);

        this.notApprovedComment = new Comment();
        this.notApprovedComment.setId(NOT_APPROVED_COMMENT_ID);
        this.notApprovedComment.setAuthor("author2");
        this.notApprovedComment.setText("commentText2");
        this.notApprovedComment.setCreatedDate(CREATED_DATE);
        this.notApprovedComment.setApprovedComment(false);
        this.notApprovedComment.setPost(firstPublishedPost);

        this.firstPublishedPost.setComments(Arrays.asList(approvedComment, notApprovedComment));

        this.postDao = mock(PostDao.class);
        this.commentDao = mock(CommentDao.class);
        this.userDao = mock(UserDao.class);

        Authentication authentication = mock(Authentication.class);
        this.securityContext = mock(SecurityContext.class);

        this.postService = new PostServiceImpl(postDao, commentDao, userDao);

        given(this.postDao.findAllPublishedPosts()).willReturn(listOfPublishedPosts);

        given(this.postDao.findAllDrafts()).willReturn(listOfDraftPosts);

        given(securityContext.getAuthentication()).willReturn(authentication);
        given(authentication.getName()).willReturn(firstUser.getUsername());

        given(this.userDao.findUserByUsername(firstUser.getUsername())).willReturn(firstUser);

        given(this.postDao.read(FIRST_PUBLISHED_POST_ID)).willReturn(firstPublishedPost);

        doNothing().when(this.postDao).update(firstPublishedPost);

        doNothing().when(this.postDao).update(draftPost);

        doNothing().when(this.commentDao).create(newComment);

        given(this.postDao.removePost(FIRST_PUBLISHED_POST_ID)).willReturn(true);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindAllPublishedPosts() throws Exception {
        List<Post> found = postService.findAllPublishedPosts();

        assertEquals(listOfPublishedPosts.size(), found.size());

        assertEquals(firstPublishedPost.getId(), found.get(0).getId());
        assertEquals(firstPublishedPost.getAuthor(), found.get(0).getAuthor());
        assertEquals(firstPublishedPost.getTitle(), found.get(0).getTitle());
        assertEquals(firstPublishedPost.getText(), found.get(0).getText());
        assertEquals(firstPublishedPost.getCreatedDate(), found.get(0).getCreatedDate());
        assertEquals(firstPublishedPost.getPublishedDate(), found.get(0).getPublishedDate());
        assertEquals(firstPublishedPost.getComments(), found.get(0).getComments());

        assertEquals(secondPublishedPost.getId(), found.get(1).getId());
        assertEquals(secondPublishedPost.getAuthor(), found.get(1).getAuthor());
        assertEquals(secondPublishedPost.getTitle(), found.get(1).getTitle());
        assertEquals(secondPublishedPost.getText(), found.get(1).getText());
        assertEquals(secondPublishedPost.getCreatedDate(), found.get(1).getCreatedDate());
        assertEquals(secondPublishedPost.getPublishedDate(), found.get(1).getPublishedDate());
        assertEquals(secondPublishedPost.getComments(), found.get(1).getComments());

        verify(postDao, times(1)).findAllPublishedPosts();
        verifyNoMoreInteractions(postDao);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindAllDrafts() throws Exception {
        List<Post> found = postService.findAllDrafts();

        assertEquals(listOfDraftPosts.size(), found.size());

        assertEquals(draftPost.getId(), found.get(0).getId());
        assertEquals(draftPost.getAuthor(), found.get(0).getAuthor());
        assertEquals(draftPost.getTitle(), found.get(0).getTitle());
        assertEquals(draftPost.getText(), found.get(0).getText());
        assertEquals(draftPost.getCreatedDate(), found.get(0).getCreatedDate());
        assertEquals(draftPost.getPublishedDate(), found.get(0).getPublishedDate());
        assertEquals(draftPost.getComments(), found.get(0).getComments());

        verify(postDao, times(1)).findAllDrafts();
        verifyNoMoreInteractions(postDao);
    }

    @Test
    public void testCreatePost() throws Exception {
        SecurityContextHolder.setContext(securityContext);
        postService.createPost(newPost);

        assertNotNull(newPost.getCreatedDate());
        assertEquals(firstUser, newPost.getAuthor());

        verify(userDao, times(1)).findUserByUsername(firstUser.getUsername());
        verifyNoMoreInteractions(userDao);
        verify(postDao, times(1)).create(newPost);
        verifyNoMoreInteractions(postDao);
    }

    @Test
    public void testUpdatePost() throws Exception {
        postService.updatePost(editedFirstPublishedPost);

        assertEquals(editedFirstPublishedPost.getTitle(), firstPublishedPost.getTitle());
        assertEquals(editedFirstPublishedPost.getText(), firstPublishedPost.getText());

        verify(postDao, times(1)).read(FIRST_PUBLISHED_POST_ID);
        verify(postDao, times(1)).update(firstPublishedPost);
        verifyNoMoreInteractions(postDao);
    }

    @Test
    public void testPublishPost() throws Exception {
        postService.publishPost(draftPost);

        assertNotNull(draftPost.getPublishedDate());

        verify(postDao, times(1)).update(draftPost);
        verifyNoMoreInteractions(postDao);
    }

    @Test
    public void testAddCommentToPost() throws Exception {
        postService.addCommentToPost(newComment, FIRST_PUBLISHED_POST_ID);

        assertNotNull(newComment.getCreatedDate());
        assertEquals(false, newComment.isApprovedComment());
        assertEquals(firstPublishedPost, newComment.getPost());

        verify(postDao, times(1)).read(FIRST_PUBLISHED_POST_ID);
        verifyNoMoreInteractions(postDao);
        verify(commentDao, times(1)).create(newComment);
        verifyNoMoreInteractions(commentDao);
    }

    @Test
    public void testRemovePost() throws Exception {
        postService.removePost(FIRST_PUBLISHED_POST_ID);

        verify(postDao, times(1)).removePost(FIRST_PUBLISHED_POST_ID);
        verifyNoMoreInteractions(postDao);
    }
}

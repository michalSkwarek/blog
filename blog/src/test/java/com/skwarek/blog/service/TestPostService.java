package com.skwarek.blog.service;

import com.skwarek.blog.MyEmbeddedDatabase;
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
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
public class TestPostService {

    private final static long FIRST_PUBLISHED_POST_ID = 1;

    private final static Date CREATED_DATE = MyEmbeddedDatabase.getCreatedDate();
    private final static Date PUBLISHED_DATE = MyEmbeddedDatabase.getPublishedDate();

    private User firstAuthor;
    private User secondAuthor;

    private Post firstPublishedPost;
    private Post secondPublishedPost;
    private Post draftPost;

    private Comment approvedComment;
    private Comment notApprovedComment;

    private Post newPost;
    private Post editedFirstPublishedPost;

    private Comment newComment;

    private PostDao postDao;
    private CommentDao commentDao;
    private UserDao userDao;

    Authentication authentication;
    SecurityContext securityContext;

    @Autowired
    private PostService postService;

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

        this.newPost = new Post();
        this.newPost.setTitle("newTitle");
        this.newPost.setText("newText");

        this.editedFirstPublishedPost = new Post();
        this.editedFirstPublishedPost.setId(FIRST_PUBLISHED_POST_ID);
        this.editedFirstPublishedPost.setTitle("editedTitle");
        this.editedFirstPublishedPost.setText("editedText");

        this.newComment = new Comment();
        this.newComment.setAuthor("newAuthor");
        this.newComment.setText("newText");

        this.postDao = mock(PostDao.class);
        this.commentDao = mock(CommentDao.class);
        this.userDao = mock(UserDao.class);

        this.authentication = mock(Authentication.class);
        this.securityContext = mock(SecurityContext.class);

        this.postService = new PostServiceImpl(postDao, commentDao, userDao);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindAllPublishedPosts() throws Exception {
        given(this.postDao.findAllPublishedPosts()).willReturn(Arrays.asList(firstPublishedPost, secondPublishedPost));

        List<Post> found = postService.findAllPublishedPosts();

        assertEquals(2, found.size());

        assertEquals(1, found.get(0).getId());
        assertEquals(firstAuthor, found.get(0).getAuthor());
        assertEquals("title1", found.get(0).getTitle());
        assertEquals("text1", found.get(0).getText());
        assertEquals(CREATED_DATE, found.get(0).getCreatedDate());
        assertEquals(PUBLISHED_DATE, found.get(0).getPublishedDate());
        assertEquals(Arrays.asList(approvedComment, notApprovedComment), found.get(0).getComments());

        assertEquals(2, found.get(1).getId());
        assertEquals(secondAuthor, found.get(1).getAuthor());
        assertEquals("title2", found.get(1).getTitle());
        assertEquals("text2", found.get(1).getText());
        assertEquals(CREATED_DATE, found.get(1).getCreatedDate());
        assertEquals(PUBLISHED_DATE, found.get(1).getPublishedDate());
        assertEquals(Collections.emptyList(), found.get(1).getComments());

        verify(postDao, times(1)).findAllPublishedPosts();
        verifyNoMoreInteractions(postDao);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindAllDrafts() throws Exception {
        given(this.postDao.findAllDrafts()).willReturn(Collections.singletonList(draftPost));

        List<Post> found = postService.findAllDrafts();

        assertEquals(1, found.size());

        assertEquals(3, found.get(0).getId());
        assertEquals(firstAuthor, found.get(0).getAuthor());
        assertEquals("title3", found.get(0).getTitle());
        assertEquals("text3", found.get(0).getText());
        assertEquals(CREATED_DATE, found.get(0).getCreatedDate());
        assertEquals(null, found.get(0).getPublishedDate());
        assertEquals(Collections.emptyList(), found.get(0).getComments());

        verify(postDao, times(1)).findAllDrafts();
        verifyNoMoreInteractions(postDao);
    }

    @Test
    public void testCreatePost() throws Exception {
        given(securityContext.getAuthentication()).willReturn(authentication);
        given(authentication.getName()).willReturn(firstAuthor.getUsername());
        given(this.userDao.findUserByUsername(firstAuthor.getUsername())).willReturn(firstAuthor);

        SecurityContextHolder.setContext(securityContext);

        postService.createPost(newPost);

        assertNotNull(newPost.getCreatedDate());
        assertEquals(firstAuthor, newPost.getAuthor());

        verify(userDao, times(1)).findUserByUsername(firstAuthor.getUsername());
        verifyNoMoreInteractions(userDao);
        verify(postDao, times(1)).create(newPost);
        verifyNoMoreInteractions(postDao);
    }

    @Test
    public void testUpdatePost() throws Exception {
        given(this.postDao.read(FIRST_PUBLISHED_POST_ID)).willReturn(firstPublishedPost);

        postService.updatePost(editedFirstPublishedPost);

        assertEquals("editedTitle", firstPublishedPost.getTitle());
        assertEquals("editedText", firstPublishedPost.getText());

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
        given(this.postDao.read(FIRST_PUBLISHED_POST_ID)).willReturn(firstPublishedPost);

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
        given(this.postDao.removePost(FIRST_PUBLISHED_POST_ID)).willReturn(true);

        postService.removePost(FIRST_PUBLISHED_POST_ID);

        verify(postDao, times(1)).removePost(FIRST_PUBLISHED_POST_ID);
        verifyNoMoreInteractions(postDao);
    }
}

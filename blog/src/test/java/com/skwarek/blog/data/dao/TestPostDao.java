package com.skwarek.blog.data.dao;

import com.skwarek.blog.configuration.ApplicationContextConfiguration;
import com.skwarek.blog.data.dao.impl.PostDaoImpl;
import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.data.entity.User;
import com.skwarek.blog.service.PostService;
import com.skwarek.blog.service.impl.PostServiceImpl;
import javafx.geometry.Pos;
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

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by Michal on 02/01/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
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

    private List<Post> listOfPublishedPosts;
    private List<Post> listOfDraftPosts;

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
        this.firstUser = new User();
        this.firstUser.setId(FIRST_USER_ID);
        this.firstUser.setUsername("user1");
        this.firstUser.setPassword("pass1");
        this.firstUser.setEnabled(true);
        this.firstUser.setRole("ROLE_ADMIN");

        this.secondUser = new User();
        this.secondUser.setId(SECOND_USER_ID);

        this.newPost = new Post();
        this.newPost.setAuthor(firstUser);
        this.newPost.setTitle("newTitle");
        this.newPost.setText("newTest");
        this.newPost.setCreatedDate(CREATED_DATE);

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

        Authentication authentication = mock(Authentication.class);
        this.securityContext = mock(SecurityContext.class);

//        this.postDao = new PostDaoImpl();

        this.userDao.create(firstUser);
        this.userDao.create(secondUser);

        this.postDao.create(firstPublishedPost);
        this.postDao.create(secondPublishedPost);
        this.postDao.create(draftPost);

        given(securityContext.getAuthentication()).willReturn(authentication);
        given(authentication.getName()).willReturn(firstUser.getUsername());

//        doNothing().when(this.postDao).update(firstPublishedPost);
//
//        doNothing().when(this.postDao).update(draftPost);
    }

    @Test
    public void testCreatePost() throws Exception {
        int oldSize = postDao.findAll().size();
        postDao.create(newPost);
        assertEquals(oldSize + 1, postDao.findAll().size());

        long newPostId = 4;

        Post found = postDao.read(newPostId);

        assertNotNull(found);
        assertEquals(newPostId, found.getId());
        assertEquals(newPost.getAuthor(), found.getAuthor());
        assertEquals(newPost.getTitle(), found.getTitle());
        assertEquals(newPost.getText(), found.getText());
        assertEquals(newPost.getCreatedDate(), found.getCreatedDate());
        assertEquals(null, found.getPublishedDate());
        assertEquals(Collections.<Comment>emptyList(), found.getComments());
    }

    @Test
    public void testReadPost() throws Exception {
        Post found = postDao.read(FIRST_PUBLISHED_POST_ID);

        assertNotNull(found);
        assertEquals(firstPublishedPost.getId(), found.getId());
        assertEquals(firstPublishedPost.getAuthor(), found.getAuthor());
        assertEquals(firstPublishedPost.getTitle(), found.getTitle());
        assertEquals(firstPublishedPost.getText(), found.getText());
        assertEquals(firstPublishedPost.getCreatedDate(), found.getCreatedDate());
        assertEquals(firstPublishedPost.getPublishedDate(), found.getPublishedDate());
        assertEquals(firstPublishedPost.getComments(), found.getComments());
    }

    @Test
    public void testUpdatePost() throws Exception {
        firstPublishedPost.setTitle("editedTitle");
        firstPublishedPost.setText("editedText");
        postDao.update(firstPublishedPost);

        Post found = postDao.read(FIRST_PUBLISHED_POST_ID);

        assertNotNull(found);
        assertEquals(firstPublishedPost.getId(), found.getId());
        assertEquals(firstPublishedPost.getAuthor(), found.getAuthor());
        assertEquals(firstPublishedPost.getTitle(), found.getTitle());
        assertEquals(firstPublishedPost.getText(), found.getText());
        assertEquals(firstPublishedPost.getCreatedDate(), found.getCreatedDate());
        assertEquals(firstPublishedPost.getPublishedDate(), found.getPublishedDate());
        assertEquals(firstPublishedPost.getComments(), found.getComments());
    }

    @Test
    public void testDeletePost() throws Exception {
        int oldSize = postDao.findAll().size();
        postDao.delete(firstPublishedPost);
        assertEquals(oldSize - 1, postDao.findAll().size());

        Post found = postDao.read(FIRST_PUBLISHED_POST_ID);

        assertNull(found);
    }

    @Test
    public void testFindAll() throws Exception {
        List<Post> found = postDao.findAll();

        assertEquals(listOfPublishedPosts.size() + 1, found.size());

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

        assertEquals(draftPost.getId(), found.get(2).getId());
        assertEquals(draftPost.getAuthor(), found.get(2).getAuthor());
        assertEquals(draftPost.getTitle(), found.get(2).getTitle());
        assertEquals(draftPost.getText(), found.get(2).getText());
        assertEquals(draftPost.getCreatedDate(), found.get(2).getCreatedDate());
        assertEquals(draftPost.getPublishedDate(), found.get(2).getPublishedDate());
        assertEquals(draftPost.getComments(), found.get(2).getComments());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindAllPublishedPosts() throws Exception {
        List<Post> found = postDao.findAllPublishedPosts();

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
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindAllDrafts() throws Exception {
        List<Post> found = postDao.findAllDrafts();

        assertEquals(listOfDraftPosts.size(), found.size());

        assertEquals(draftPost.getId(), found.get(0).getId());
        assertEquals(draftPost.getAuthor(), found.get(0).getAuthor());
        assertEquals(draftPost.getTitle(), found.get(0).getTitle());
        assertEquals(draftPost.getText(), found.get(0).getText());
        assertEquals(draftPost.getCreatedDate(), found.get(0).getCreatedDate());
        assertEquals(draftPost.getPublishedDate(), found.get(0).getPublishedDate());
        assertEquals(draftPost.getComments(), found.get(0).getComments());
    }

    @Test
    public void testRemovePost() throws Exception {
        List<Post> foundAllPosts = postDao.findAll();
        assertEquals(3, foundAllPosts.size());

        Post post = foundAllPosts.get(0);
        post.getComments().clear();

        postDao.removePost(FIRST_PUBLISHED_POST_ID);

        List<Post> foundPosts = postDao.findAll();
        assertEquals(2, foundPosts.size());
    }

    @Test
    public void testRemoveCommentsFromPost() throws Exception {

    }

    private int countComment(List<Post> posts) {
        int count = 0;
        for (Post post : posts) {
            for (Comment comment : post.getComments()) {
                ++count;
            }
        }
        return count;
    }
}

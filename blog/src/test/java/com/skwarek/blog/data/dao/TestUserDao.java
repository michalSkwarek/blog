package com.skwarek.blog.data.dao;

import com.skwarek.blog.configuration.ForTestsApplicationContextConfiguration;
import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.data.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Michal on 05/01/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ForTestsApplicationContextConfiguration.class})
@Transactional
public class TestUserDao {

    private static final long FIRST_PUBLISHED_POST_ID = 1;
    private static final long SECOND_PUBLISHED_POST_ID = 2;
    private static final long DRAFT_POST_ID = 3;

    private static final long FIRST_AUTHOR_ID = 1;

    private Post firstPublishedPost;
    private Post secondPublishedPost;
    private Post draftPost;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    @Before
    public void setUp() {
        this.firstPublishedPost = postDao.read(FIRST_PUBLISHED_POST_ID);
        this.secondPublishedPost = postDao.read(SECOND_PUBLISHED_POST_ID);
        this.draftPost = postDao.read(DRAFT_POST_ID);
    }

    @Test
    public void testReadUser() throws Exception {
        User found = userDao.read(FIRST_AUTHOR_ID);

        assertNotNull(found);
        assertEquals(FIRST_AUTHOR_ID, found.getId());
        assertEquals("user1", found.getUsername());
        assertEquals("pass1", found.getPassword());
        assertEquals(true, found.getEnabled());
        assertEquals("ROLE_ADMIN", found.getRole());
        assertEquals(Arrays.asList(firstPublishedPost, draftPost), found.getPosts());
    }

    @Test
    public void testAll() throws Exception {
        List<User> found = userDao.findAll();

        assertEquals(2, found.size());

        assertEquals(1, found.get(0).getId());
        assertEquals("user1", found.get(0).getUsername());
        assertEquals("pass1", found.get(0).getPassword());
        assertEquals(true, found.get(0).getEnabled());
        assertEquals("ROLE_ADMIN", found.get(0).getRole());
        assertEquals(Arrays.asList(firstPublishedPost, draftPost), found.get(0).getPosts());

        assertEquals(2, found.get(1).getId());
        assertEquals("user2", found.get(1).getUsername());
        assertEquals("pass2", found.get(1).getPassword());
        assertEquals(true, found.get(1).getEnabled());
        assertEquals("ROLE_ADMIN", found.get(1).getRole());
        assertEquals(Collections.singletonList(secondPublishedPost), found.get(1).getPosts());
    }

    @Test
    public void testFindUserByUsername() {
        User notFound = userDao.findUserByUsername("notFound");

        assertNull(notFound);

        User found = userDao.findUserByUsername("user1");

        assertNotNull(found);
        assertEquals(FIRST_AUTHOR_ID, found.getId());
        assertEquals("user1", found.getUsername());
        assertEquals("pass1", found.getPassword());
        assertEquals(true, found.getEnabled());
        assertEquals("ROLE_ADMIN", found.getRole());
        assertEquals(Arrays.asList(firstPublishedPost, draftPost), found.getPosts());
    }
}

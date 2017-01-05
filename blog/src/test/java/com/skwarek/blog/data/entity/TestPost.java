package com.skwarek.blog.data.entity;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Michal on 05/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestPost {

    private final static Date createdDate = new GregorianCalendar(2000, Calendar.JANUARY, 11, 11, 22, 33).getTime();
    private final static Date publishedDate = new GregorianCalendar(2000, Calendar.JANUARY, 12, 22, 33, 44).getTime();

    private User firstUser;
    private Post firstPublishedPost;

    @Before
    public void setUp() {
        this.firstUser = new User();
        this.firstUser.setUsername("user1");

        this.firstPublishedPost = new Post();
        this.firstPublishedPost.setId(1L);
        this.firstPublishedPost.setAuthor(firstUser);
        this.firstPublishedPost.setTitle("title1");
        this.firstPublishedPost.setText("text1");
        this.firstPublishedPost.setCreatedDate(createdDate);
        this.firstPublishedPost.setPublishedDate(publishedDate);
    }

    @Test
    public void beanIsSerializable() {
        final byte[] serializedPost = SerializationUtils.serialize(firstPublishedPost);
        final Post deserializedPost = (Post) SerializationUtils.deserialize(serializedPost);
        assertEquals(firstPublishedPost, deserializedPost);
    }
}

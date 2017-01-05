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
public class TestComment {

    private final static Date createdDate = new GregorianCalendar(2000, Calendar.JANUARY, 11, 11, 22, 33).getTime();
    private final static Date publishedDate = new GregorianCalendar(2000, Calendar.JANUARY, 12, 22, 33, 44).getTime();

    private User firstUser;
    private Post firstPublishedPost;
    private Comment firstApprovedComment;

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

        this.firstApprovedComment = new Comment();
        this.firstApprovedComment.setId(1L);
        this.firstApprovedComment.setAuthor("author1");
        this.firstApprovedComment.setText("commentText1");
        this.firstApprovedComment.setCreatedDate(createdDate);
        this.firstApprovedComment.setApprovedComment(true);
        this.firstApprovedComment.setPost(firstPublishedPost);
    }

    @Test
    public void beanIsSerializable() {
        final byte[] serializedComment = SerializationUtils.serialize(firstApprovedComment);
        final Comment deserializedComment = (Comment) SerializationUtils.deserialize(serializedComment);
        assertEquals(firstApprovedComment, deserializedComment);
    }
}

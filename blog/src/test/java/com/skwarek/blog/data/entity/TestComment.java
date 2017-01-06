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
public class TestComment {

    private final static long PUBLISHED_POST_ID = 1;

    private final static long APPROVED_COMMENT_ID = 1;

    private final static Date CREATED_DATE = new GregorianCalendar(2000, Calendar.JANUARY, 11, 11, 22, 33).getTime();
    private final static Date PUBLISHED_DATE = new GregorianCalendar(2000, Calendar.JANUARY, 12, 22, 33, 44).getTime();

    private User user;

    private Post publishedPost;

    private Comment approvedComment;

    @Before
    public void setUp() {
        this.user = new User();
        this.user.setUsername("user1");

        this.publishedPost = new Post();
        this.publishedPost.setId(PUBLISHED_POST_ID);
        this.publishedPost.setAuthor(user);
        this.publishedPost.setTitle("title1");
        this.publishedPost.setText("text1");
        this.publishedPost.setCreatedDate(CREATED_DATE);
        this.publishedPost.setPublishedDate(PUBLISHED_DATE);

        this.approvedComment = new Comment();
        this.approvedComment.setId(APPROVED_COMMENT_ID);
        this.approvedComment.setAuthor("author1");
        this.approvedComment.setText("commentText1");
        this.approvedComment.setCreatedDate(CREATED_DATE);
        this.approvedComment.setApprovedComment(true);
        this.approvedComment.setPost(publishedPost);
    }

    @Test
    public void beanIsSerializable() {
        final byte[] serializedComment = SerializationUtils.serialize(approvedComment);
        final Comment deserializedComment = (Comment) SerializationUtils.deserialize(serializedComment);
        assertEquals(approvedComment, deserializedComment);
    }
}

package com.skwarek.blog.data.entity;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Michal on 05/01/2017.
 */
public class TestPost {

    private final static long PUBLISHED_POST_ID = 1;

    private final static long APPROVED_COMMENT_ID = 1;
    private final static long NOT_APPROVED_COMMENT_ID = 2;

    private final static Date CREATED_DATE = new GregorianCalendar(2000, Calendar.JANUARY, 11, 11, 22, 33).getTime();
    private final static Date PUBLISHED_DATE = new GregorianCalendar(2000, Calendar.JANUARY, 12, 22, 33, 44).getTime();

    private User user;

    private Post publishedPost;

    private Comment approvedComment;
    private Comment notApprovedComment;

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

        this.notApprovedComment = new Comment();
        this.notApprovedComment.setId(NOT_APPROVED_COMMENT_ID);
        this.notApprovedComment.setAuthor("author2");
        this.notApprovedComment.setText("commentText2");
        this.notApprovedComment.setCreatedDate(CREATED_DATE);
        this.notApprovedComment.setApprovedComment(false);
        this.notApprovedComment.setPost(publishedPost);

        this.publishedPost.setComments(Arrays.asList(approvedComment, notApprovedComment));
    }

    @Test
    public void beanIsSerializable() {
        final byte[] serializedPost = SerializationUtils.serialize(publishedPost);
        final Post deserializedPost = (Post) SerializationUtils.deserialize(serializedPost);
        assertEquals(publishedPost, deserializedPost);
    }

    @Test
    public void shouldReturnCounterOnlyApprovedComment() {
        assertEquals(2, publishedPost.getComments().size());
        assertEquals(1, publishedPost.approvedCommentCounter());
    }
}

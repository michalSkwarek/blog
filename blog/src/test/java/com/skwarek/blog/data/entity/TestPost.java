package com.skwarek.blog.data.entity;

import com.skwarek.blog.MyEmbeddedDatabase;
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

    private Post publishedPost;

    @Before
    public void setUp() {
        MyEmbeddedDatabase myDB = new MyEmbeddedDatabase();

        this.publishedPost = myDB.getPost_no_1();
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

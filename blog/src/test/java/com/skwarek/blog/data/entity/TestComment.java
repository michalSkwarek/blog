package com.skwarek.blog.data.entity;

import com.skwarek.blog.MyEmbeddedDatabase;
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

    private Comment approvedComment;

    @Before
    public void setUp() {
        MyEmbeddedDatabase myDB = new MyEmbeddedDatabase();

        this.approvedComment = myDB.getComment_bo_1();
    }

    @Test
    public void beanIsSerializable() {
        final byte[] serializedComment = SerializationUtils.serialize(approvedComment);
        final Comment deserializedComment = (Comment) SerializationUtils.deserialize(serializedComment);
        assertEquals(approvedComment, deserializedComment);
    }
}

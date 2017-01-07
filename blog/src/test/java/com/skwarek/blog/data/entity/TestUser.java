package com.skwarek.blog.data.entity;

import com.skwarek.blog.MyEmbeddedDatabase;
import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Michal on 05/01/2017.
 */
public class TestUser {

    private User user;

    @Before
    public void setUp() {
        MyEmbeddedDatabase myDB = new MyEmbeddedDatabase();

        this.user = myDB.getUser_no_1();
    }

    @Test
    public void beanIsSerializable() {
        final byte[] serializedUser = SerializationUtils.serialize(user);
        final User deserializedUser = (User) SerializationUtils.deserialize(serializedUser);
        assertEquals(user, deserializedUser);
    }
}
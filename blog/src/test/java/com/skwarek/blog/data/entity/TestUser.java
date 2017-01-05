package com.skwarek.blog.data.entity;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Michal on 05/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestUser {

    private User firstUser;

    @Before
    public void setUp() {
        this.firstUser = new User();
        this.firstUser.setId(1L);
        this.firstUser.setUsername("user1");
        this.firstUser.setPassword("pass1");
        this.firstUser.setEnabled(true);
        this.firstUser.setRole("ROLE_ADMIN");
    }

    @Test
    public void beanIsSerializable() {
        final byte[] serializedUser = SerializationUtils.serialize(firstUser);
        final User deserializedUser = (User) SerializationUtils.deserialize(serializedUser);
        assertEquals(firstUser, deserializedUser);
    }
}
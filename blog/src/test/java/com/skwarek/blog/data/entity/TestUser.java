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
public class TestUser {

    private final static long USER_ID = 1;

    private User user;

    @Before
    public void setUp() {
        this.user = new User();
        this.user.setId(USER_ID);
        this.user.setUsername("user1");
        this.user.setPassword("pass1");
        this.user.setEnabled(true);
        this.user.setRole("ROLE_ADMIN");
    }

    @Test
    public void beanIsSerializable() {
        final byte[] serializedUser = SerializationUtils.serialize(user);
        final User deserializedUser = (User) SerializationUtils.deserialize(serializedUser);
        assertEquals(user, deserializedUser);
    }
}
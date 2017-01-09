package com.skwarek.blog.data.entity;

import com.skwarek.blog.MyEmbeddedDatabase;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Michal on 05/01/2017.
 */
public class TestUser extends AbstractJavaBeanTest<User> {

    private User user;

    @Before
    public void setUp() {
        MyEmbeddedDatabase myDB = new MyEmbeddedDatabase();

        this.user = myDB.getUser_no_1();
    }

    @Override
    protected User getBeanInstance() {
        return user;
    }

    @Test
    public void toStringCorrectness() throws Exception {
        assertEquals("user1", user.toString());
    }
}
package com.skwarek.blog.data.entity;

import com.skwarek.blog.MyEmbeddedDatabase;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Michal on 05/01/2017.
 */
public class TestComment extends AbstractJavaBeanTest<Comment> {

    private Comment comment;

    @Before
    public void setUp() {
        MyEmbeddedDatabase myDB = new MyEmbeddedDatabase();

        this.comment = myDB.getComment_no_1();
    }

    @Override
    protected Comment getBeanInstance() {
        return comment;
    }

    @Test
    public void toStringCorrectness() throws Exception {
        assertEquals("commentText1", comment.toString());
    }
}

package com.skwarek.blog.data.entity;

import com.skwarek.blog.MyEmbeddedDatabase;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Michal on 06/01/2017.
 */
public class TestPostValidation {

    private Post firstPublishedPost;

    private Validator validator;

    @Before
    public void setUp() {
        MyEmbeddedDatabase myDB = new MyEmbeddedDatabase();

        this.firstPublishedPost = myDB.getPost_no_1();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void titleAndTextPostAreCorrected() {
        Set<ConstraintViolation<Post>> constraintViolations =
                validator.validate(firstPublishedPost);

        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void onlyTitlePostIsEmpty() {
        firstPublishedPost.setTitle("");
        Set<ConstraintViolation<Post>> constraintViolations =
                validator.validate(firstPublishedPost);

        assertEquals(1, constraintViolations.size());
        assertEquals("{notEmpty}", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void onlyTextPostIsEmpty() {
        firstPublishedPost.setText("");
        Set<ConstraintViolation<Post>> constraintViolations =
                validator.validate(firstPublishedPost);

        assertEquals(1, constraintViolations.size());
        assertEquals("{notEmpty}", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void titleAndTextPostAreEmpty() {
        firstPublishedPost.setTitle("");
        firstPublishedPost.setText("");
        Set<ConstraintViolation<Post>> constraintViolations =
                validator.validate(firstPublishedPost);

        assertEquals(2, constraintViolations.size());
        assertEquals("{notEmpty}", constraintViolations.iterator().next().getMessage());
    }
}
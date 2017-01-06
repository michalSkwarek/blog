package com.skwarek.blog.data.entity;

import javafx.geometry.Pos;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Michal on 06/01/2017.
 */
public class TestPostValidation {

    private final static long USER_ID = 1;

    private final static long PUBLISHED_POST_ID = 1;

    private final static Date CREATED_DATE = new GregorianCalendar(2000, Calendar.JANUARY, 11, 11, 22, 33).getTime();
    private final static Date PUBLISHED_DATE = new GregorianCalendar(2000, Calendar.JANUARY, 12, 22, 33, 44).getTime();

    private User user;

    private Post publishedPost;

    private Validator validator;

    @Before
    public void setUp() {
        this.user = new User();
        this.user.setId(USER_ID);

        this.publishedPost = new Post();
        this.publishedPost.setId(PUBLISHED_POST_ID);
        this.publishedPost.setAuthor(user);
        this.publishedPost.setTitle("title1");
        this.publishedPost.setText("text1");
        this.publishedPost.setCreatedDate(CREATED_DATE);
        this.publishedPost.setPublishedDate(PUBLISHED_DATE);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void titleAndTextPostAreCorrected() {
        Set<ConstraintViolation<Post>> constraintViolations =
                validator.validate(publishedPost);

        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void onlyTitlePostIsEmpty() {
        publishedPost.setTitle("");
        Set<ConstraintViolation<Post>> constraintViolations =
                validator.validate(publishedPost);

        assertEquals(1, constraintViolations.size());
        assertEquals("{notEmpty}", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void onlyTextPostIsEmpty() {
        publishedPost.setText("");
        Set<ConstraintViolation<Post>> constraintViolations =
                validator.validate(publishedPost);

        assertEquals(1, constraintViolations.size());
        assertEquals("{notEmpty}", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void titleAndTextPostAreEmpty() {
        publishedPost.setTitle("");
        publishedPost.setText("");
        Set<ConstraintViolation<Post>> constraintViolations =
                validator.validate(publishedPost);

        assertEquals(2, constraintViolations.size());
        assertEquals("{notEmpty}", constraintViolations.iterator().next().getMessage());
    }
}
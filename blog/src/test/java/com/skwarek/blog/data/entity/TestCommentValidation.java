package com.skwarek.blog.data.entity;

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
public class TestCommentValidation {

    private final static long PUBLISHED_POST_ID = 1;

    private final static long APPROVED_COMMENT_ID = 1;

    private final static Date CREATED_DATE = new GregorianCalendar(2000, Calendar.JANUARY, 11, 11, 22, 33).getTime();

    private Post publishedPost;

    private Comment approvedComment;

    private Validator validator;

    @Before
    public void setUp() {
        this.publishedPost = new Post();
        this.publishedPost.setId(PUBLISHED_POST_ID);

        this.approvedComment = new Comment();
        this.approvedComment.setId(APPROVED_COMMENT_ID);
        this.approvedComment.setAuthor("author1");
        this.approvedComment.setText("commentText1");
        this.approvedComment.setCreatedDate(CREATED_DATE);
        this.approvedComment.setApprovedComment(true);
        this.approvedComment.setPost(publishedPost);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void authorAndTextCommentAreCorrected() {
        Set<ConstraintViolation<Comment>> constraintViolations =
                validator.validate(approvedComment);

        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void onlyAuthorCommentIsEmpty() {
        approvedComment.setAuthor("");
        Set<ConstraintViolation<Comment>> constraintViolations =
                validator.validate(approvedComment);

        assertEquals(1, constraintViolations.size());
        assertEquals("{notEmpty}", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void onlyTextCommentIsEmpty() {
        approvedComment.setText("");
        Set<ConstraintViolation<Comment>> constraintViolations =
                validator.validate(approvedComment);

        assertEquals(1, constraintViolations.size());
        assertEquals("{notEmpty}", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void authorAndTextCommentAreEmpty() {
        approvedComment.setAuthor("");
        approvedComment.setText("");
        Set<ConstraintViolation<Comment>> constraintViolations =
                validator.validate(approvedComment);

        assertEquals(2, constraintViolations.size());
        assertEquals("{notEmpty}", constraintViolations.iterator().next().getMessage());
    }
}
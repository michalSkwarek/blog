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
public class TestCommentValidation {

    private Comment approvedComment;

    private Validator validator;

    @Before
    public void setUp() {
        MyEmbeddedDatabase myDB = new MyEmbeddedDatabase();

        this.approvedComment = myDB.getComment_no_1();

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
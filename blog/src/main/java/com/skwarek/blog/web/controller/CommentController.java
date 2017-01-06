package com.skwarek.blog.web.controller;

import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michal on 04/01/2017.
 */
@Controller
@RequestMapping(value = "/comment")
public class CommentController {

    private final static String REDIRECT_TO = "redirect:";
    private final static String POST_PAGE = "/post";

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @RequestMapping(value = "/{commentId}/approve", method = RequestMethod.GET)
    public String approveComment(@PathVariable long commentId) {

        Comment comment = commentService.read(commentId);
        commentService.approve(comment);
        return REDIRECT_TO + POST_PAGE + "/" + comment.getPost().getId();
    }

    @RequestMapping(value = "/{commentId}/remove", method = RequestMethod.GET)
    public String removeComment(@PathVariable long commentId) {

        Comment comment = commentService.read(commentId);
        commentService.removeComment(commentId);
        return REDIRECT_TO + POST_PAGE + "/" + comment.getPost().getId();
    }
}

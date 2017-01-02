package com.skwarek.blog.web.controller;

import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by Michal on 02/01/2017.
 */
@Controller
public class PostController {

    private final static String VIEWS_POSTS_LIST = "posts_list";
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showPosts(Model model) {

        List<Post> posts = postService.findAllPublishedPosts();
        model.addAttribute("posts", posts);
        return VIEWS_POSTS_LIST;
    }
}
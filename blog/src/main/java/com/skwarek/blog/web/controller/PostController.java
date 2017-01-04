package com.skwarek.blog.web.controller;

import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Michal on 02/01/2017.
 */
@Controller
public class PostController {

    private final static String VIEWS_POSTS_LIST = "blog/post_list";
    private final static String VIEWS_DRAFTS = "blog/post_draft_list";
    private final static String VIEWS_POST_DETAIL = "blog/post_detail";
    private final static String VIEWS_POST_FORM = "blog/post_edit";
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showPublishedPosts(Model model) {

        List<Post> posts = postService.findAllPublishedPosts();
        for (Post post : posts) {
            post.setApprovedCommentsCounter(postService.getApprovedCommentsCounter(post));
        }
        model.addAttribute("posts", posts);
        return VIEWS_POSTS_LIST;
    }

    @RequestMapping(value = "/drafts", method = RequestMethod.GET)
    public String showDraftsPosts(Model model) {

        List<Post> posts = postService.findAllDrafts();
        model.addAttribute("posts", posts);
        return VIEWS_DRAFTS;
    }

    @RequestMapping(value = "/post/{postId}", method = RequestMethod.GET)
    public String showPost(Model model, @PathVariable Long postId) {

        Post post = postService.read(postId);
        model.addAttribute("post", post);
        return VIEWS_POST_DETAIL;
    }

    @RequestMapping(value = "/post/new", method = RequestMethod.GET)
    public String initCreatePostForm(Model model) {

        model.addAttribute("post", new Post());
        return VIEWS_POST_FORM;
    }

    @RequestMapping(value = "/post/new", method = RequestMethod.POST)
    public String processCreatePostForm(@Valid Post post, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return VIEWS_POST_FORM;
        }

        postService.createPost(post);
        return "redirect:/";
    }

    @RequestMapping(value = "/post/{postId}/edit", method = RequestMethod.GET)
    public String initEditPostForm(Model model, @PathVariable Long postId) {

        Post post = postService.read(postId);
        model.addAttribute("post", post);
        return VIEWS_POST_FORM;
    }

    @RequestMapping(value = "/post/{postId}/edit", method = RequestMethod.POST)
    public String processEditPostForm(@Valid Post post, BindingResult bindingResult, @PathVariable Long postId) {

        if (bindingResult.hasErrors()) {
            return VIEWS_POST_FORM;
        }

        postService.update(post);
        return "redirect:/";
    }
}

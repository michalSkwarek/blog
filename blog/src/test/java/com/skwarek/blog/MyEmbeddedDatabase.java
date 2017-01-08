package com.skwarek.blog;

import com.skwarek.blog.data.entity.Comment;
import com.skwarek.blog.data.entity.Post;
import com.skwarek.blog.data.entity.User;

import java.util.*;

/**
 * Created by Michal on 07/01/2017.
 */
public class MyEmbeddedDatabase {

    private final static Date CREATED_DATE = new GregorianCalendar(2000, Calendar.JANUARY, 11, 11, 22, 33).getTime();
    private final static Date PUBLISHED_DATE = new GregorianCalendar(2000, Calendar.JANUARY, 12, 22, 33, 44).getTime();

    private User user_no_1;
    private User user_no_2;

    private Post post_no_1;
    private Post post_no_2;
    private Post post_no_3;

    private Comment comment_no_1;
    private Comment comment_no_2;

    public MyEmbeddedDatabase() {

        this.user_no_1 = new User();
        this.user_no_1.setId(1L);
        this.user_no_1.setUsername("user1");
        this.user_no_1.setPassword("pass1");
        this.user_no_1.setEnabled(true);
        this.user_no_1.setRole("ROLE_ADMIN");

        this.user_no_2 = new User();
        this.user_no_2.setId(2L);
        this.user_no_2.setUsername("user2");
        this.user_no_2.setPassword("pass2");
        this.user_no_2.setEnabled(true);
        this.user_no_2.setRole("ROLE_ADMIN");

        this.post_no_1 = new Post();
        this.post_no_1.setId(1L);
        this.post_no_1.setAuthor(user_no_1);
        this.post_no_1.setTitle("title1");
        this.post_no_1.setText("text1");
        this.post_no_1.setCreatedDate(CREATED_DATE);
        this.post_no_1.setPublishedDate(PUBLISHED_DATE);

        this.post_no_2 = new Post();
        this.post_no_2.setId(2L);
        this.post_no_2.setAuthor(user_no_2);
        this.post_no_2.setTitle("title2");
        this.post_no_2.setText("text2");
        this.post_no_2.setCreatedDate(CREATED_DATE);
        this.post_no_2.setPublishedDate(PUBLISHED_DATE);

        this.post_no_3 = new Post();
        this.post_no_3.setId(3L);
        this.post_no_3.setAuthor(user_no_1);
        this.post_no_3.setTitle("title3");
        this.post_no_3.setText("text3");
        this.post_no_3.setCreatedDate(CREATED_DATE);

        this.user_no_1.setPosts(Arrays.asList(post_no_1, post_no_3));
        this.user_no_2.setPosts(Collections.singletonList(post_no_2));

        this.comment_no_1 = new Comment();
        this.comment_no_1.setId(1L);
        this.comment_no_1.setAuthor("author1");
        this.comment_no_1.setText("commentText1");
        this.comment_no_1.setCreatedDate(CREATED_DATE);
        this.comment_no_1.setApprovedComment(true);
        this.comment_no_1.setPost(post_no_1);

        this.comment_no_2 = new Comment();
        this.comment_no_2.setId(2L);
        this.comment_no_2.setAuthor("author2");
        this.comment_no_2.setText("commentText2");
        this.comment_no_2.setCreatedDate(CREATED_DATE);
        this.comment_no_2.setApprovedComment(false);
        this.comment_no_2.setPost(post_no_1);

        this.post_no_1.setComments(Arrays.asList(comment_no_1, comment_no_2));
    }

    public static Date getCreatedDate() {
        return CREATED_DATE;
    }

    public static Date getPublishedDate() {
        return PUBLISHED_DATE;
    }

    public User getUser_no_1() {
        return user_no_1;
    }

    public User getUser_no_2() {
        return user_no_2;
    }

    public Post getPost_no_1() {
        return post_no_1;
    }

    public Post getPost_no_2() {
        return post_no_2;
    }

    public Post getPost_no_3() {
        return post_no_3;
    }

    public Comment getComment_no_1() {
        return comment_no_1;
    }

    public Comment getComment_no_2() {
        return comment_no_2;
    }
}

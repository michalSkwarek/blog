package com.skwarek.blog.data.entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Michal on 02/01/2017.
 */
@Entity
@Table(name = "post")
public class Post extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -7880798132853619385L;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @NotEmpty(message = "{notEmpty}")
    private String title;

    @NotEmpty(message = "{notEmpty}")
    @Type(type = "text")
    private String text;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "published_date")
    private Date publishedDate;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    public Post() { }

    public int approvedCommentCounter() {
        int counter = 0;
        for (Comment comment : comments) {
            if (comment.isApprovedComment()) {
                ++counter;
            }
        }
        return counter;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;

        Post post = (Post) o;

        if (author != null ? !author.equals(post.author) : post.author != null) return false;
        if (title != null ? !title.equals(post.title) : post.title != null) return false;
        if (text != null ? !text.equals(post.text) : post.text != null) return false;
        if (createdDate != null ? !createdDate.equals(post.createdDate) : post.createdDate != null) return false;
        return publishedDate != null ? publishedDate.equals(post.publishedDate) : post.publishedDate == null;

    }

    @Override
    public int hashCode() {
        int result = author != null ? author.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (publishedDate != null ? publishedDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return title;
    }
}

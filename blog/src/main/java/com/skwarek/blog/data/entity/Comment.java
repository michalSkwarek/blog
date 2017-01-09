package com.skwarek.blog.data.entity;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Michal on 04/01/2017.
 */
@Entity
@Table(name = "comment")
public class Comment extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -7098117967486832113L;

    @NotEmpty(message = "{notEmpty}")
    private String author;

    @NotEmpty(message = "{notEmpty}")
    @Type(type = "text")
    private String text;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "approved_comment")
    private boolean approvedComment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment() { }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public boolean isApprovedComment() {
        return approvedComment;
    }

    public void setApprovedComment(boolean approvedComment) {
        this.approvedComment = approvedComment;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;

        Comment comment = (Comment) o;

        if (approvedComment != comment.approvedComment) return false;
        if (author != null ? !author.equals(comment.author) : comment.author != null) return false;
        if (text != null ? !text.equals(comment.text) : comment.text != null) return false;
        if (createdDate != null ? !createdDate.equals(comment.createdDate) : comment.createdDate != null) return false;
        return post != null ? post.equals(comment.post) : comment.post == null;

    }

    @Override
    public int hashCode() {
        int result = author != null ? author.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (approvedComment ? 1 : 0);
        result = 31 * result + (post != null ? post.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return text;
    }
}

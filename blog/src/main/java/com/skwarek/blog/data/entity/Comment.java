package com.skwarek.blog.data.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Michal on 04/01/2017.
 */
@Entity
@Table(name = "comment")
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "author")
    private String author;
    @Column(name = "text")
    private String text;
    @Column(name = "created_date")
    private Date cretaedDate;
    @Column(name = "approved_comment")
    private boolean approvedComment;

    public Comment() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Date getCretaedDate() {
        return cretaedDate;
    }

    public void setCretaedDate(Date cretaedDate) {
        this.cretaedDate = cretaedDate;
    }

    public boolean isApprovedComment() {
        return approvedComment;
    }

    public void setApprovedComment(boolean approvedComment) {
        this.approvedComment = approvedComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;

        Comment comment = (Comment) o;

        if (approvedComment != comment.approvedComment) return false;
        if (author != null ? !author.equals(comment.author) : comment.author != null) return false;
        if (text != null ? !text.equals(comment.text) : comment.text != null) return false;
        return cretaedDate != null ? cretaedDate.equals(comment.cretaedDate) : comment.cretaedDate == null;

    }

    @Override
    public int hashCode() {
        int result = author != null ? author.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (cretaedDate != null ? cretaedDate.hashCode() : 0);
        result = 31 * result + (approvedComment ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return text;
    }
}

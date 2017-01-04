package com.skwarek.blog.data.dao.impl;

import com.skwarek.blog.data.dao.PostDao;
import com.skwarek.blog.data.dao.generic.GenericDaoImpl;
import com.skwarek.blog.data.entity.Post;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Michal on 02/01/2017.
 */
@Repository
public class PostDaoImpl extends GenericDaoImpl<Post, Long> implements PostDao {

    @Override
    public List findAllPublishedPosts() {
        Query listOfPosts = getSession().createQuery("from Post p where p.publishedDate is not null order by p.publishedDate asc");
        return listOfPosts.list();
    }

    @Override
    public List findAllDrafts() {
        Query listOfPosts = getSession().createQuery("from Post p where p.publishedDate is null order by p.createdDate asc");
        return listOfPosts.list();
    }
}

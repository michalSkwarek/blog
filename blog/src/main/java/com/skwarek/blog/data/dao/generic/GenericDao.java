package com.skwarek.blog.data.dao.generic;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Michal on 26.09.2016.
 */
public interface GenericDao<E, PK extends Serializable> {

    void create(E entity);

    E read(PK id);

    void update(E entity);

    void delete(E entity);

    List<E> findAll();
}
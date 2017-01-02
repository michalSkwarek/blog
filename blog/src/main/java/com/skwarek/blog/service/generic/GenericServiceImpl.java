package com.skwarek.blog.service.generic;

import com.skwarek.blog.data.dao.generic.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Michal on 26.09.2016.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public abstract class GenericServiceImpl<E, PK extends Serializable> implements GenericService<E, PK> {

    @Autowired
    private GenericDao<E, PK> genericDao;

    @Override
    public void create(E entity) {
        genericDao.create(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public E read(PK id) {
        return genericDao.read(id);
    }

    @Override
    public void update(E entity) {
        genericDao.update(entity);
    }

    @Override
    public void delete(E entity) {
        genericDao.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<E> findAll() {
        return genericDao.findAll();
    }
}

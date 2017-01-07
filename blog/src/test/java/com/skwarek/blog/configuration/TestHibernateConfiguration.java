package com.skwarek.blog.configuration;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import static junit.framework.Assert.assertNotNull;

@ContextConfiguration(classes = HibernateConfiguration.class)
public class TestHibernateConfiguration extends AbstractJUnit4SpringContextTests {

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void testHibernateConfiguration() {
        assertNotNull (sessionFactory);
    }
}

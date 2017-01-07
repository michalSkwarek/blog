package com.skwarek.blog.data.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.commons.lang.SerializationUtils;
import org.junit.Test;
import org.meanbean.test.BeanTester;

import java.io.Serializable;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Michal on 07/01/2017.
 */
public abstract class AbstractJavaBeanTest<T> {

    protected abstract T getBeanInstance();

    @Test
    @SuppressWarnings("unchecked")
    public void beanIsSerializable() throws Exception {
        final T myBean = getBeanInstance();
        final byte[] serializedMyBean = SerializationUtils.serialize((Serializable) myBean);
        final T deserializedMyBean = (T) SerializationUtils.deserialize(serializedMyBean);
        assertEquals(myBean, deserializedMyBean);
    }

    @Test
    public void equalsAndHashCodeContract() throws Exception {
        EqualsVerifier.forClass(getBeanInstance().getClass()).suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void getterAndSetterCorrectness() throws Exception {
        final BeanTester beanTester = new BeanTester();
        beanTester.testBean(getBeanInstance().getClass());
    }
}
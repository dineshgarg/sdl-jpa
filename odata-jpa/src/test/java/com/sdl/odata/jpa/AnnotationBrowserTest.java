package com.sdl.odata.jpa;

import com.sdl.odata.jpa.model.edm.Person;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationBrowserTest {

    @Test
    public void testEdmToJpaConversion() throws Exception {
        Class<?> jpaClass = AnnotationBrowser.toJpa(Person.class);
        Assert.assertEquals(com.sdl.odata.jpa.model.jpa.Person.class, jpaClass);
    }
}

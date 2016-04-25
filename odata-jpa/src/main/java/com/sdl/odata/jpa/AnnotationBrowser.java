package com.sdl.odata.jpa;

import com.sdl.odata.jpa.annotation.JPAEntity;

import java.lang.annotation.Annotation;

/**
 * Helps to convert EDM entity to JPA and vice versa.
 */
public class AnnotationBrowser {


    /**
     * For given EDM entity class, find corresponding JPA entity class.
     *
     * @param edmClass EDM entity class.
     * @return JPA entity class.
     */
    public static Class<?> toJpa(Class<?> edmClass) throws ODataJpaException {
        for (Annotation ann : edmClass.getAnnotations()) {
            if (ann instanceof JPAEntity) {
                return ((JPAEntity) ann).jpaEntityClass();
            }
        }
        throw new ODataJpaException("Can't find JPA entity for " + edmClass.getName());
    }
}

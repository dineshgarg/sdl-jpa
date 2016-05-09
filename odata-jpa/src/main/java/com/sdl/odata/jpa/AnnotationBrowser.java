package com.sdl.odata.jpa;

import com.sdl.odata.jpa.annotation.JPAEntity;

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
        JPAEntity jpaEntity = edmClass.getAnnotation(JPAEntity.class);
        if (jpaEntity != null) {
            return jpaEntity.clazz();
        }
        throw new ODataJpaException("Can't find JPA entity for " + edmClass.getName());
    }
}

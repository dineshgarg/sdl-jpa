package com.sdl.odata.jpa.model.edm;

import com.sdl.odata.api.edm.annotations.EdmEntity;
import com.sdl.odata.jpa.annotation.JPAEntity;

@EdmEntity
@JPAEntity(clazz = com.sdl.odata.jpa.model.jpa.Person.class)
public class Person {
}

package com.sdl.odata.jpa.model.edm;

import com.sdl.odata.api.edm.annotations.EdmEntity;
import com.sdl.odata.api.edm.annotations.EdmEntitySet;
import com.sdl.odata.api.edm.annotations.EdmProperty;
import com.sdl.odata.jpa.annotation.JPAEntity;

@EdmEntity(namespace = "SDL.OData.Jpa.Test", key = "id", containerName = "JpaTest")
@EdmEntitySet
@JPAEntity(clazz = com.sdl.odata.jpa.model.jpa.Person.class)
public class Person {
    @EdmProperty(name = "id", nullable = false)
    private String id;

    @EdmProperty(name = "firstName", nullable = false)
    private String firstName;

    @EdmProperty(name = "lastName", nullable = false)
    private String lastName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

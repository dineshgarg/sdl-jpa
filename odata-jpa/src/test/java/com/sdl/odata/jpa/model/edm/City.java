package com.sdl.odata.jpa.model.edm;

import com.sdl.odata.api.edm.annotations.EdmEntity;
import com.sdl.odata.api.edm.annotations.EdmEntitySet;
import com.sdl.odata.api.edm.annotations.EdmNavigationProperty;
import com.sdl.odata.api.edm.annotations.EdmProperty;
import com.sdl.odata.jpa.annotation.JPAEntity;

import java.util.List;

@EdmEntity(namespace = "SDL.OData.Jpa.Test", key = "id", containerName = "JpaTest")
@EdmEntitySet(name="Cities")
@JPAEntity(clazz = com.sdl.odata.jpa.model.jpa.City.class)
public class City {
    @EdmProperty(name = "id", nullable = false)
    private String id;

    @EdmProperty(name = "name", nullable = false)
    private String name;

    @EdmProperty(name = "zipCode", nullable = false)
    private String zipCode;

    @EdmProperty(name = "state", nullable = false)
    private String state;

    @EdmNavigationProperty(containsTarget = true)
    private List<Person> persons;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}

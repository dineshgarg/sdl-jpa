package com.sdl.odata.jpa.model.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class City {

    @Id
    @Column(unique = true, nullable = false, length = 22)
    private String id;

    @Column(length=256)
    private String name;

    @Column(length=32)
    private String state;

    @Column(length=16)
    private String zipCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this. id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}

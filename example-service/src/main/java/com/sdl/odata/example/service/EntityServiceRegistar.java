/**
 * Copyright (c) 2015 SDL Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sdl.odata.example.service;

import com.google.common.collect.Lists;
import com.sdl.odata.api.ODataException;
import com.sdl.odata.api.edm.registry.ODataEdmRegistry;
import com.sdl.odata.example.datasource.PersistentDataSource;
import com.sdl.odata.example.persistent.entities.City;
import com.sdl.odata.example.persistent.entities.CityRepo;
import com.sdl.odata.example.persistent.entities.Person;
import com.sdl.odata.example.persistent.entities.PersonRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author rdevries
 */
@Component
public class EntityServiceRegistar {
    private static final Logger LOG = LoggerFactory.getLogger(EntityServiceRegistar.class);

    @Autowired
    private ODataEdmRegistry oDataEdmRegistry;

    @Autowired
    private PersistentDataSource persistedDS;

    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private PersonRepo personRepo;

    @PostConstruct
    public void registerEntities() throws ODataException {
        LOG.debug("Registering example entities");

        oDataEdmRegistry.registerClasses(Lists.newArrayList(
                com.sdl.odata.example.edm.entities.City.class,
                com.sdl.odata.example.edm.entities.Person.class
        ));

        City c1 = new City();
        c1.setId("City-1");
        c1.setName("Redwood City");
        c1.setState("CA");
        c1.setZipCode("94063");

        City c2 = new City();
        c2.setId("City-2");
        c2.setName("San Mateo");
        c2.setState("CA");
        c2.setZipCode("94001");

        City savedCity1 = cityRepo.save(c1);
        City savedCity2 = cityRepo.save(c2);

        Person p1 = new Person();
        p1.setId("Dinesh");
        p1.setFirstName("Dinesh");
        p1.setLastName("Garg");
        p1.setEmailId("dg@dg.com");
        p1.setCity(savedCity1);

        Person p2 = new Person();
        p2.setId("Oleg");
        p2.setFirstName("Oleg");
        p2.setLastName("Burykin");
        p2.setEmailId("ob@ob.com");
        p2.setCity(savedCity1);

        Person p3 = new Person();
        p3.setId("Rohan");
        p3.setFirstName("Rohan");
        p3.setLastName("Arora");
        p3.setEmailId("ra@ar.com");
        p3.setCity(savedCity2);

        personRepo.save(p1);
        personRepo.save(p2);
        personRepo.save(p3);
    }
}

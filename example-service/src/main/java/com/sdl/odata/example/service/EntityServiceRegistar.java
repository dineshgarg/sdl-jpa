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

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.sdl.odata.api.ODataException;
import com.sdl.odata.api.edm.registry.ODataEdmRegistry;
import com.sdl.odata.example.datasource.PersistentDataSource;
import com.sdl.odata.example.edm.entities.City;
import com.sdl.odata.example.edm.entities.Person;
import com.sdl.odata.example.persistent.entities.CityRepo;
import com.sdl.odata.example.persistent.entities.PersonRepo;

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
                City.class,
                Person.class
        ));

        com.sdl.odata.example.persistent.entities.City c = new com.sdl.odata.example.persistent.entities.City();
        c.setName("Redwood City");
        c.setState("CA");
        c.setZipCode("94063");
        com.sdl.odata.example.persistent.entities.City savedCity = cityRepo.save(c);
        
        com.sdl.odata.example.persistent.entities.Person p = new com.sdl.odata.example.persistent.entities.Person();
        p.setFirstName("Dinesh");
        p.setLastName("Garg");
        p.setEmailId("dg@dg.com");
        p.setCity(savedCity);
        com.sdl.odata.example.persistent.entities.Person p1 = new com.sdl.odata.example.persistent.entities.Person();
        p1.setFirstName("Oleg");
        p1.setLastName("Burykin");
        p1.setEmailId("ob@ob.com");
        p1.setCity(savedCity);
        personRepo.save(p);
        personRepo.save(p1);


    }
}

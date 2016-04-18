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
package com.sdl.odata.example.datasource;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sdl.odata.api.ODataException;
import com.sdl.odata.api.parser.TargetType;
import com.sdl.odata.api.processor.datasource.DataSource;
import com.sdl.odata.api.processor.datasource.DataSourceProvider;
import com.sdl.odata.api.processor.datasource.ODataDataSourceException;
import com.sdl.odata.api.processor.query.QueryOperation;
import com.sdl.odata.api.processor.query.strategy.QueryOperationStrategy;
import com.sdl.odata.api.service.ODataRequestContext;
import com.sdl.odata.example.edm.entities.City;
import com.sdl.odata.example.edm.entities.Person;
import com.sdl.odata.example.persistent.entities.CityRepo;
import com.sdl.odata.example.persistent.entities.PersonRepo;

@Component
public class PersistentDataSourceProvider implements DataSourceProvider {

	private static final Logger LOG = LoggerFactory.getLogger(PersistentDataSourceProvider.class);
	
	@Autowired
	private PersistentDataSource persistentDS;
	
	@Autowired
	private CityRepo cityRepo;
	
	@Autowired
	private PersonRepo personRepo;
	
	@Override
	public DataSource getDataSource(ODataRequestContext arg0) {
		return persistentDS;
	}

	@Override	
    public QueryOperationStrategy getStrategy(ODataRequestContext oDataRequestContext, QueryOperation queryOperation, TargetType targetType) throws ODataException {
        
        if (targetType.typeName().equals("SDL.OData.Example.Person")) {
        	
	        return () -> {
	            LOG.debug("Executing query against in memory data");
	            List<com.sdl.odata.example.persistent.entities.Person> persons = personRepo.findAll();
	            
	            List<Person> personEntities = new ArrayList<>();
	            for (com.sdl.odata.example.persistent.entities.Person p : persons) {
	            	Person personEntity = new Person();
	            	personEntity.setFirstName(p.getFirstName());
	            	personEntity.setLastName(p.getLastName());
	            	personEntity.setEmailId(p.getEmailId());
	            	personEntity.setId(p.getId());
	            	personEntity.setCity(p.getCity().getName());
	            	personEntities.add(personEntity);
	            }
	
	            LOG.debug("Found {} persons matching query", personEntities.size());
	
	            return personEntities;
	        };
        } else {
	
	        return () -> {
	            LOG.debug("Executing query against in memory data");
	            List<com.sdl.odata.example.persistent.entities.City> cities = cityRepo.findAll();
	            
	            List<City> cityEntities = new ArrayList<>();
	            for (com.sdl.odata.example.persistent.entities.City c : cities) {
	            	City cityEntity = new City();
	            	cityEntity.setName(c.getName());
	            	cityEntity.setId(c.getId());
	            	cityEntity.setZipCode(c.getZipCode());
	            	cityEntity.setState(c.getState());
	            	cityEntities.add(cityEntity);
	            }
	            
	            LOG.debug("Found {} persons matching query", cityEntities.size());
	
	            return cityEntities;
	        };
        }
    }

	@Override
    public boolean isSuitableFor(ODataRequestContext oDataRequestContext, String entityType) throws ODataDataSourceException {
        return oDataRequestContext.getEntityDataModel().getType(entityType).getJavaType().equals(Person.class) || oDataRequestContext.getEntityDataModel().getType(entityType).getJavaType().equals(City.class);
    }

}

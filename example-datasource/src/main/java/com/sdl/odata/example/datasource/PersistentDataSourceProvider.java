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
import com.sdl.odata.jpa.JpaStrategyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class PersistentDataSourceProvider implements DataSourceProvider {

	private static final Logger LOG = LoggerFactory.getLogger(PersistentDataSourceProvider.class);

	@Autowired
	private PersistentDataSource persistentDS;

	@PersistenceContext
	private EntityManager em;

	@Override
	public DataSource getDataSource(ODataRequestContext ctx) {
		return persistentDS;
	}

	@Override
    public QueryOperationStrategy getStrategy(ODataRequestContext requestContext,
                                              QueryOperation queryOperation,
                                              TargetType targetType) throws ODataException {
		return JpaStrategyBuilder.
                create(em).
                withContext(requestContext).
                withOperation(queryOperation).
                expecting(targetType).
                build();
    }

	@Override
    public boolean isSuitableFor(ODataRequestContext oDataRequestContext, String entityType) throws ODataDataSourceException {
        return oDataRequestContext.getEntityDataModel().getType(entityType).getJavaType().equals(Person.class) ||
                oDataRequestContext.getEntityDataModel().getType(entityType).getJavaType().equals(City.class);
    }
}

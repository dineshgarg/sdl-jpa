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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sdl.odata.api.ODataException;
import com.sdl.odata.api.ODataSystemException;
import com.sdl.odata.api.parser.TargetType;
import com.sdl.odata.api.processor.query.ComparisonCriteria;
import com.sdl.odata.api.processor.query.Criteria;
import com.sdl.odata.api.processor.query.CriteriaFilterOperation;
import com.sdl.odata.api.processor.query.ExpandOperation;
import com.sdl.odata.api.processor.query.JoinOperation;
import com.sdl.odata.api.processor.query.LimitOperation;
import com.sdl.odata.api.processor.query.LiteralCriteriaValue;
import com.sdl.odata.api.processor.query.OrderByOperation;
import com.sdl.odata.api.processor.query.PropertyCriteriaValue;
import com.sdl.odata.api.processor.query.QueryOperation;
import com.sdl.odata.api.processor.query.SelectByKeyOperation;
import com.sdl.odata.api.processor.query.SelectOperation;
import com.sdl.odata.api.processor.query.SelectPropertiesOperation;
import com.sdl.odata.api.processor.query.SkipOperation;
import com.sdl.odata.example.Message;
import com.sdl.odata.example.Topic;

/**
 *
 */
public class StrategyBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(StrategyBuilder.class);

    private List<Predicate<Topic>> predicatesTopic = new ArrayList<>();
    private List<Predicate<Message>> predicatesMsg = new ArrayList<>();
    
    private int limit = Integer.MAX_VALUE;

    public List<Predicate<Topic>> buildCriteriaTopic(QueryOperation queryOperation, TargetType targetType) throws ODataException {
        buildFromOperation(queryOperation, targetType);

        return predicatesTopic;
    }
    
    public List<Predicate<Message>> buildCriteriaMessage(QueryOperation queryOperation, TargetType targetType) throws ODataException {
        buildFromOperation(queryOperation, targetType);

        return predicatesMsg;
    }

    public int getLimit() {
        return limit;
    }

    private void buildFromOperation(QueryOperation operation, TargetType targetType) throws ODataException {
        if (operation instanceof SelectOperation) {
            buildFromSelect((SelectOperation) operation);
        } else if (operation instanceof SelectByKeyOperation) {
            buildFromSelectByKey((SelectByKeyOperation) operation, targetType);
        } else if (operation instanceof CriteriaFilterOperation) {
            buildFromFilter((CriteriaFilterOperation)operation, targetType);
        } else if (operation instanceof LimitOperation) {
            buildFromLimit((LimitOperation) operation, targetType);
        } else if (operation instanceof SkipOperation) {
            //not supported for now
        } else if (operation instanceof ExpandOperation) {
            //not supported for now
        } else if (operation instanceof OrderByOperation) {
            //not supported for now
        } else if (operation instanceof SelectPropertiesOperation) {
            //not supported for now
        } else if (operation instanceof JoinOperation) {
        	buildFromJoin((JoinOperation) operation, targetType);
        } else {
            throw new ODataSystemException("Unsupported query operation: " + operation);
        }
    }

    private void buildFromLimit(LimitOperation operation, TargetType targetType) throws ODataException {
        this.limit = operation.getCount();
        LOG.debug("Limit has been set to: {}", limit);
        buildFromOperation(operation.getSource(), targetType);
    }

    private void buildFromSelect(SelectOperation selectOperation) {
        LOG.debug("Selecting all persons, no predicates needed");
    }
    
    private void buildFromJoin(JoinOperation joinOperation, TargetType targetType) {
    	System.out.println(joinOperation);
    }

    private void buildFromSelectByKey(SelectByKeyOperation selectByKeyOperation, TargetType targetType) {
        Map<String, Object> keys = selectByKeyOperation.getKeyAsJava();
        String id = (String)keys.get("id");
        LOG.debug("Selecting by key: {}", id);

        if (targetType.typeName().equals(Topic.class.getName())) {
        	predicatesTopic.add(topic -> topic.getId().equalsIgnoreCase(id));
        } else {
        	predicatesMsg.add(msg -> msg.getId().equalsIgnoreCase(id));
        }
    }

    private void buildFromFilter(CriteriaFilterOperation criteriaFilterOperation, TargetType targetType) {
        Criteria criteria = criteriaFilterOperation.getCriteria();
        if(criteria instanceof ComparisonCriteria) {
            ComparisonCriteria comparisonCriteria = (ComparisonCriteria) criteria;

            //For now we only support here property key/value comparisons, just to keep the example simple
            if(comparisonCriteria.getLeft() instanceof PropertyCriteriaValue
                    && comparisonCriteria.getRight() instanceof LiteralCriteriaValue) {

                PropertyCriteriaValue propertyCriteriaValue = (PropertyCriteriaValue) comparisonCriteria.getLeft();
                LiteralCriteriaValue literalCriteriaValue = (LiteralCriteriaValue) comparisonCriteria.getRight();

                if (targetType.typeName().equals(Topic.class.getName())) {
                	Predicate<Topic> t = topic -> {
                        Object fieldValue = getTopicField(topic, propertyCriteriaValue.getPropertyName());
                        Object queryValue = literalCriteriaValue.getValue();

                        LOG.debug("Comparing equality on value: {} to queried value: {}", fieldValue, queryValue);

                        return fieldValue != null && fieldValue.equals(literalCriteriaValue.getValue());
                    };

                    predicatesTopic.add(t);
                } else {
                	Predicate<Message> t = msg -> {
                        Object fieldValue = getMessageField(msg, propertyCriteriaValue.getPropertyName());
                        Object queryValue = literalCriteriaValue.getValue();

                        LOG.debug("Comparing equality on value: {} to queried value: {}", fieldValue, queryValue);

                        return fieldValue != null && fieldValue.equals(literalCriteriaValue.getValue());
                    };

                    predicatesMsg.add(t);
                }
                
            }
        }
    }

    private Object getTopicField(Topic topic, String propertyName) {
        try {
            Field field = topic.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            return field.get(topic);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LOG.debug("Could not load property: " + propertyName);
            return null;
        }
    }

    private Object getMessageField(Message msg, String propertyName) {
        try {
            Field field = msg.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            return field.get(msg);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LOG.debug("Could not load property: " + propertyName);
            return null;
        }
    }

}

package com.sdl.odata.jpa;

import com.sdl.odata.api.parser.TargetType;
import com.sdl.odata.api.processor.query.QueryOperation;
import com.sdl.odata.api.processor.query.strategy.QueryOperationStrategy;
import com.sdl.odata.api.service.ODataRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * Given Edm and Jpa entities, execute query on database
 * and provide QueryOperation for odata framework.
 */
public class JpaStrategyBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(JpaStrategyBuilder.class);

    private ODataRequestContext requestContext;
    private QueryOperation queryOperation;
    private TargetType targetType;

    private JpaStrategyBuilder() { }

    public static JpaStrategyBuilder create() {
        return new JpaStrategyBuilder();
    }

    public JpaStrategyBuilder withContext(ODataRequestContext requestContext) {
        this.requestContext = requestContext;
        return this;
    }

    public JpaStrategyBuilder withOperation(QueryOperation queryOperation) {
        this.queryOperation = queryOperation;
        return this;
    }

    public JpaStrategyBuilder expecting(TargetType targetType) {
        this.targetType = targetType;
        return this;
    }

    public QueryOperationStrategy build() {
        LOG.debug("Building JPA query for odata request");

        if (targetType.isCollection()) {
            // Get edm entity
            // Get jpa entity
            // Get jpa repo
            // do query (all for now)
            return () -> Collections.emptyList();
        } else {
            // Same, find and return entity or value
            return () -> null;
        }
    }
}

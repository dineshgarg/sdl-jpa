package com.sdl.odata.jpa;

import com.sdl.odata.api.parser.TargetType;
import com.sdl.odata.api.processor.query.QueryOperation;
import com.sdl.odata.api.processor.query.strategy.QueryOperationStrategy;
import com.sdl.odata.api.service.ODataRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

/**
 * Given Edm and Jpa entities, execute query on database
 * and provide QueryOperation for odata framework.
 */
public class JpaStrategyBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(JpaStrategyBuilder.class);

    private EntityManager em;

    private ODataRequestContext requestContext;
    private QueryOperation queryOperation;
    private TargetType targetType;

    private JpaStrategyBuilder(EntityManager em) {
        this.em = em;
    }

    public static JpaStrategyBuilder create(EntityManager em) {
        return new JpaStrategyBuilder(em);
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

        String query;
        if (targetType.isCollection()) {
            // Get edm entity
            // Get jpa entity
            // create query (all for now)
            // Convert jpa to edm
            query = "from Person";
        } else {
            // Same, find and return entity or value
            query = "from Person where id=123";
        }

        return () -> em.createQuery(query).getResultList();
    }
}

package com.sdl.odata.jpa;

import com.sdl.odata.api.ODataException;
import com.sdl.odata.api.parser.TargetType;
import com.sdl.odata.api.processor.datasource.ODataDataSourceException;
import com.sdl.odata.api.processor.query.QueryOperation;
import com.sdl.odata.api.processor.query.strategy.QueryOperationStrategy;
import com.sdl.odata.api.service.ODataRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

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

    public JpaStrategyBuilder withTargetType(TargetType targetType) {
        this.targetType = targetType;
        return this;
    }

    public QueryOperationStrategy build() throws ODataException {
        LOG.debug("Building JPA query for odata request");

        CriteriaQuery cq;
        try {
            cq = new OdataJpaQueryBuilder(requestContext, queryOperation, targetType, em).build();
        } catch (ClassNotFoundException e) {
            LOG.error("Failed to create JPA query", e);
            throw new ODataDataSourceException("Failed to create JPA query", e);
        }

        Query query = em.createQuery(cq);

        final Class<?> edmEntityClass = EdmUtil.getEdmEntityClass(requestContext, targetType);
        final Class<?> jpaEntityClass = AnnotationBrowser.toJpa(edmEntityClass);

        return () -> new JpaToEdmAdapter(jpaEntityClass, edmEntityClass).toEdmEntities(query.getResultList());
    }
}

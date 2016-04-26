package com.sdl.odata.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.sdl.odata.api.edm.model.EntityDataModel;
import com.sdl.odata.api.parser.TargetType;
import com.sdl.odata.api.processor.query.QueryOperation;
import com.sdl.odata.api.processor.query.SelectByKeyOperation;
import com.sdl.odata.api.processor.query.SelectOperation;
import com.sdl.odata.api.service.ODataRequestContext;

/**
 * Will build Jpa query from odata query for you.
 */
public class OdataJpaQueryBuilder {

    private final ODataRequestContext requestContext;
    private final QueryOperation queryOperation;
	private final TargetType targetType;

    public OdataJpaQueryBuilder(ODataRequestContext requestContext, QueryOperation queryOperation, TargetType targetType) {
        this.requestContext = requestContext;
        this.queryOperation = queryOperation;
        this.targetType = targetType;
    }

    public CriteriaQuery build(CriteriaBuilder criteriaBuilder) throws ClassNotFoundException, ODataJpaException {
        return fromOperation(queryOperation, criteriaBuilder);
    }

    private CriteriaQuery fromOperation(QueryOperation operation, CriteriaBuilder cb)
            throws ClassNotFoundException, ODataJpaException {

        if (operation instanceof SelectOperation) {
            return buildFromSelect((SelectOperation) operation, cb);
        } else if (operation instanceof SelectByKeyOperation) {
            buildFromSelectByKey((SelectByKeyOperation) operation, cb);
        }
        return null;
    }

    private CriteriaQuery buildFromSelect(SelectOperation operation, CriteriaBuilder cb)
            throws ClassNotFoundException, ODataJpaException {

        Class<?> edmClass = EdmUtil.getEdmEntityClass(requestContext, targetType);
        Class<?> jpaClass = AnnotationBrowser.toJpa(edmClass);

        CriteriaQuery cq = cb.createQuery(jpaClass);
        Root root = cq.from(jpaClass);
        cq.select(root);

        return cq;
    }

    private void buildFromSelectByKey(SelectByKeyOperation operation, CriteriaBuilder cb) {
    }
}
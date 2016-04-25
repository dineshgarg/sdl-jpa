package com.sdl.odata.jpa;

import com.sdl.odata.api.processor.query.QueryOperation;
import com.sdl.odata.api.processor.query.SelectByKeyOperation;
import com.sdl.odata.api.processor.query.SelectOperation;
import com.sdl.odata.api.service.ODataRequestContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Will build Jpa query from odata query for you.
 */
public class OdataJpaQueryBuilder {

    private final ODataRequestContext requestContext;
    private final QueryOperation queryOperation;

    public OdataJpaQueryBuilder(ODataRequestContext requestContext, QueryOperation queryOperation) {
        this.requestContext = requestContext;
        this.queryOperation = queryOperation;
    }

    public CriteriaQuery build(CriteriaBuilder criteriaBuilder) {
        fromOperation(queryOperation, criteriaBuilder);
        return null;
    }

    private void fromOperation(QueryOperation operation, CriteriaBuilder cb) {
        if (operation instanceof SelectOperation) {
            buildFromSelect((SelectOperation) operation, cb);
        } else if (operation instanceof SelectByKeyOperation) {
            buildFromSelectByKey((SelectByKeyOperation) operation, cb);
        }
    }

    private void buildFromSelect(SelectOperation operation, CriteriaBuilder sb) {
//        sb.append(" from ").append(operation.entitySetName());
    }

    private void buildFromSelectByKey(SelectByKeyOperation operation, CriteriaBuilder sb) {
//        sb.append(" where ").append(operation.entitySetName());
    }
}
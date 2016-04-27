package com.sdl.odata.jpa;

import com.sdl.odata.api.parser.TargetType;
import com.sdl.odata.api.processor.query.QueryOperation;
import com.sdl.odata.api.processor.query.SelectByKeyOperation;
import com.sdl.odata.api.processor.query.SelectOperation;
import com.sdl.odata.api.service.ODataRequestContext;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Map;

/**
 * Will build Jpa query from OData query for you.
 */
public class OdataJpaQueryBuilder {

    private final ODataRequestContext requestContext;
    private final QueryOperation queryOperation;
	private final TargetType targetType;
    private final EntityManager em;

    public OdataJpaQueryBuilder(ODataRequestContext requestContext, QueryOperation queryOperation,
                                TargetType targetType, EntityManager em) {
        this.requestContext = requestContext;
        this.queryOperation = queryOperation;
        this.targetType = targetType;
        this.em = em;
    }

    public CriteriaQuery build()
            throws ClassNotFoundException, ODataJpaException {

        Class<?> edmClass = EdmUtil.getEdmEntityClass(requestContext, targetType);
        Class<?> jpaClass = AnnotationBrowser.toJpa(edmClass);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        fromOperation(queryOperation, cb, cq, jpaClass);
        return cq;
    }

    private void fromOperation(QueryOperation operation, CriteriaBuilder cb, CriteriaQuery cq, Class<?> jpaClass)
            throws ClassNotFoundException, ODataJpaException {

        if (operation instanceof SelectOperation) {
            buildFromSelect((SelectOperation) operation, cq, jpaClass);
        } else if (operation instanceof SelectByKeyOperation) {
            buildFromSelectByKey((SelectByKeyOperation) operation, cb, cq, jpaClass);
        }
    }

    private void buildFromSelect(SelectOperation operation, CriteriaQuery cq, Class<?> jpaClass)
            throws ClassNotFoundException, ODataJpaException {

        cq.select(cq.from(jpaClass));
    }

    private void buildFromSelectByKey(SelectByKeyOperation operation, CriteriaBuilder cb, CriteriaQuery cq,
                                      Class<?> jpaClass) {
        Map<String, Object> keys = operation.getKeyAsJava();

        Root<?> root = cq.from(jpaClass);
        cq.select(root);

        // TODO: Need to find what is Id field for given EDM entity
        // TODO: And what is ID field for JPA entity.
        // FIXME Hardcoded ID field names.

        cq.where(cb.equal(root.get("id"), keys.get("id")));
    }
}

package com.sdl.odata.jpa;

import com.sdl.odata.api.parser.TargetType;
import com.sdl.odata.api.processor.query.JoinOperation;
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

    private Class<?> toJpa(String entitySet) throws ODataJpaException {
        Class<?> edmClass = EdmUtil.getEdmEntityClass(requestContext,
                requestContext.getEntityDataModel().getEntityContainer().getEntitySet(entitySet).getTypeName());
        return AnnotationBrowser.toJpa(edmClass);
    }

    public CriteriaQuery build() throws ClassNotFoundException, ODataJpaException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        fromOperation(queryOperation, cb, cq);
        return cq;
    }

    private void fromOperation(QueryOperation operation, CriteriaBuilder cb, CriteriaQuery cq)
            throws ClassNotFoundException, ODataJpaException {

        if (operation instanceof SelectOperation) {
            buildFromSelect((SelectOperation) operation, cq);
        } else if (operation instanceof SelectByKeyOperation) {
            buildFromSelectByKey((SelectByKeyOperation) operation, cb, cq);
        } else if (operation instanceof JoinOperation) {
            buildFromJoin((JoinOperation) operation, cb, cq);
        }
    }

    private void buildFromJoin(JoinOperation operation, CriteriaBuilder cb, CriteriaQuery cq)
            throws ODataJpaException, ClassNotFoundException {

        fromOperation(operation.getRightSource(), cb, cq);
        fromOperation(operation.getLeftSource(), cb, cq);
    }

    private void buildFromSelect(SelectOperation operation, CriteriaQuery cq)
            throws ClassNotFoundException, ODataJpaException {

        if (cq.getSelection() == null) {
            Root<?> root = cq.from(toJpa(operation.entitySetName()));
            cq.select(root);
        }
    }

    private void buildFromSelectByKey(SelectByKeyOperation operation, CriteriaBuilder cb, CriteriaQuery cq)
            throws ODataJpaException {

        Root<?> root = cq.from(toJpa(operation.entitySetName()));
        if (cq.getSelection() == null) {
            cq.select(root);
        }

        // TODO: Need to find what is Id field for given EDM entity
        // TODO: And what is ID field for JPA entity.
        // FIXME Hardcoded ID field names.

        Map<String, Object> keys = operation.getKeyAsJava();
        cq.where(cb.equal(root.get("id"), keys.get("id")));
    }
}

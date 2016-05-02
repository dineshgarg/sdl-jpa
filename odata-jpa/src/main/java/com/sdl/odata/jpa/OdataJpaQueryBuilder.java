package com.sdl.odata.jpa;

import com.sdl.odata.api.processor.query.JoinOperation;
import com.sdl.odata.api.processor.query.QueryOperation;
import com.sdl.odata.api.processor.query.SelectByKeyOperation;
import com.sdl.odata.api.processor.query.SelectOperation;
import com.sdl.odata.api.service.ODataRequestContext;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Will build Jpa query from OData query for you.
 */
public class OdataJpaQueryBuilder {

    private final ODataRequestContext requestContext;
    private final QueryOperation queryOperation;
    private final EntityManager em;

    private Root<?> selectRoot;
    private Collection<Predicate> andPredicates;

    public OdataJpaQueryBuilder(ODataRequestContext requestContext, QueryOperation queryOperation, EntityManager em) {
        this.requestContext = requestContext;
        this.queryOperation = queryOperation;
        this.em = em;
    }

    private Class<?> toJpa(String entitySet) throws ODataJpaException {
        Class<?> edmClass = EdmUtil.getEdmEntityClass(requestContext,
                requestContext.getEntityDataModel().getEntityContainer().getEntitySet(entitySet).getTypeName());
        return AnnotationBrowser.toJpa(edmClass);
    }

    public CriteriaQuery build() throws ClassNotFoundException, ODataJpaException {
        andPredicates = new ArrayList<>();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        fromOperation(queryOperation, cb, cq);

        if (selectRoot != null) {
            cq.select(selectRoot);
        }
        cq.where(andPredicates.toArray(new Predicate[andPredicates.size()]));

        return cq;
    }

    private Root<?> fromOperation(QueryOperation operation, CriteriaBuilder cb, CriteriaQuery cq)
            throws ClassNotFoundException, ODataJpaException {

        if (operation instanceof SelectOperation) {
            return buildFromSelect((SelectOperation) operation, cb, cq);
        } else if (operation instanceof SelectByKeyOperation) {
            return buildFromSelectByKey((SelectByKeyOperation) operation, cb, cq);
        } else if (operation instanceof JoinOperation) {
            return buildFromJoin((JoinOperation) operation, cb, cq);
        }

        throw new UnsupportedOperationException(operation.getClass().getSimpleName() + " is not supported.");
    }

    private Root<?> buildFromJoin(JoinOperation operation, CriteriaBuilder cb, CriteriaQuery cq)
            throws ODataJpaException, ClassNotFoundException {

        Root<?> rootR = fromOperation(operation.getRightSource(), cb, cq);
        Root<?> rootL = fromOperation(operation.getLeftSource(), cb, cq);

        andPredicates.add(cb.equal(rootL.get("id"), rootR.get("city").get("id")));
        return rootL; // Not sure what to return here
    }

    private Root<?> buildFromSelect(SelectOperation operation, CriteriaBuilder cb, CriteriaQuery cq)
            throws ClassNotFoundException, ODataJpaException {

        Root<?> from = cq.from(toJpa(operation.entitySetName()));
        selectRoot = selectRoot == null ? from : selectRoot;
        return from;
    }

    private Root<?> buildFromSelectByKey(SelectByKeyOperation operation, CriteriaBuilder cb, CriteriaQuery cq)
            throws ODataJpaException {

        Root<?> from = cq.from(toJpa(operation.entitySetName()));
        selectRoot = selectRoot == null ? from : selectRoot;

        // FIXME Hardcoded ID field names.
        //       Need to find what is Id field for given EDM entity.
        //       And which ID field correspond to JPA entity.

        Map<String, Object> keys = operation.getKeyAsJava();
        andPredicates.add(cb.equal(from.get("id"), keys.get("id")));

        return from;
    }
}

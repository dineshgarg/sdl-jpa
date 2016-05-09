package com.sdl.odata.jpa;

import com.sdl.odata.api.edm.annotations.EdmEntity;
import com.sdl.odata.api.edm.model.EntityDataModel;
import com.sdl.odata.api.parser.TargetType;
import com.sdl.odata.api.processor.query.QueryOperation;
import com.sdl.odata.api.processor.query.SelectOperation;
import com.sdl.odata.api.processor.query.strategy.QueryOperationStrategy;
import com.sdl.odata.api.service.ODataRequestContext;
import com.sdl.odata.edm.factory.annotations.AnnotationEntityDataModelFactory;
import com.sdl.odata.jpa.ds.DbConfiguration;
import com.sdl.odata.jpa.ds.JpaTestConfig;
import com.sdl.odata.jpa.model.edm.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scala.Option;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test generated query for different OData requests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaTestConfig.class, DbConfiguration.class})
public class JpaQueryTest {

    @PersistenceContext
    private EntityManager em;

    private static ODataRequestContext requestContext;

    private JpaStrategyBuilder builder;

    @BeforeClass
    public static void createEntityManagerFactory() throws Exception {
        EntityDataModel entityDataModel = new AnnotationEntityDataModelFactory()
                .addClass(Person.class)
                .buildEntityDataModel();

        requestContext = new ODataRequestContext(null, null, entityDataModel);
    }

    @Before
    public void setup() throws Exception {
        builder = JpaStrategyBuilder.create(em).withContext(requestContext);
    }

    @Test
    public void testQueryPersons() throws Exception {
        QueryOperation operation = new SelectOperation("Persons", false);
        TargetType targetType = new TargetType(getEdmType(Person.class), true, Option.empty());
        QueryOperationStrategy strategy = builder.withOperation(operation).withTargetType(targetType).build();

        List<?> result = strategy.execute();
        assertEquals(3, result.size());
    }

    private String getEdmType(Class<?> edmEntity) {
        EdmEntity edmEntityAnn = edmEntity.getAnnotation(EdmEntity.class);
        return edmEntityAnn.namespace() + "." + edmEntity.getSimpleName();
    }

    @After
    public void after() {
        if (em.isOpen()) {
            em.close();
        }
    }
}

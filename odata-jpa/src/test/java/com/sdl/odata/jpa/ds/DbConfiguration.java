package com.sdl.odata.jpa.ds;

import com.sdl.odata.api.ODataException;
import com.sdl.odata.jpa.model.jpa.City;
import com.sdl.odata.jpa.model.jpa.Person;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Configuration
public class DbConfiguration {

    @PersistenceUnit
    private EntityManagerFactory emf;

    private static City redwoodCity;
    private static City sanMateo;

    private static Person dinesh;
    private static Person oleg;
    private static Person rohan;

    @PostConstruct
    @Transactional
    public void registerEntities() throws ODataException {
        redwoodCity = new City();
        redwoodCity.setId("City-1");
        redwoodCity.setName("Redwood City");
        redwoodCity.setState("CA");
        redwoodCity.setZipCode("94063");

        sanMateo = new City();
        sanMateo.setId("City-2");
        sanMateo.setName("San Mateo");
        sanMateo.setState("CA");
        sanMateo.setZipCode("94001");

        dinesh = new Person();
        dinesh.setId("Dinesh");
        dinesh.setFirstName("Dinesh");
        dinesh.setLastName("Garg");
        dinesh.setCity(redwoodCity);

        oleg = new Person();
        oleg.setId("Oleg");
        oleg.setFirstName("Oleg");
        oleg.setLastName("Burykin");
        oleg.setCity(redwoodCity);

        rohan = new Person();
        rohan.setId("Rohan");
        rohan.setFirstName("Rohan");
        rohan.setLastName("Arora");
        rohan.setCity(sanMateo);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        em.persist(redwoodCity);
        em.persist(sanMateo);

        em.persist(dinesh);
        em.persist(oleg);
        em.persist(rohan);

        em.getTransaction().commit();
    }

    public static City getRedwoodCity() {
        return redwoodCity;
    }

    public static City getSanMateo() {
        return sanMateo;
    }

    public static Person getDinesh() {
        return dinesh;
    }

    public static Person getOleg() {
        return oleg;
    }

    public static Person getRohan() {
        return rohan;
    }
}

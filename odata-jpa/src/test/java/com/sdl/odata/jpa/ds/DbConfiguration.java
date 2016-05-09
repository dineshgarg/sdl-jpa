package com.sdl.odata.jpa.ds;

import com.sdl.odata.api.ODataException;
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

    @PostConstruct
    @Transactional
    public void registerEntities() throws ODataException {
//        City c1 = new City();
//        c1.setId("City-1");
//        c1.setName("Redwood City");
//        c1.setState("CA");
//        c1.setZipCode("94063");
//
//        City c2 = new City();
//        c2.setId("City-2");
//        c2.setName("San Mateo");
//        c2.setState("CA");
//        c2.setZipCode("94001");
//
//        City savedCity1 = cityRepo.save(c1);
//        City savedCity2 = cityRepo.save(c2);

        Person p1 = new Person();
        p1.setId("Dinesh");
        p1.setFirstName("Dinesh");
        p1.setLastName("Garg");
//        p1.setEmailId("dg@dg.com");
//        p1.setCity(savedCity1);

        Person p2 = new Person();
        p2.setId("Oleg");
        p2.setFirstName("Oleg");
        p2.setLastName("Burykin");
//        p2.setEmailId("ob@ob.com");
//        p2.setCity(savedCity1);

        Person p3 = new Person();
        p3.setId("Rohan");
        p3.setFirstName("Rohan");
        p3.setLastName("Arora");
//        p3.setEmailId("ra@ar.com");
//        p3.setCity(savedCity2);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(p1);
        em.persist(p2);
        em.persist(p3);
        em.getTransaction().commit();
    }
}

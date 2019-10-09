/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dto.HobbyDTO;
import dto.PersonDTO;
import dto.PhoneDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.InfoEntity;
import entities.Person;
import entities.Phone;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

/**
 *
 * @author Annika
 */
public class DevData {
        public static void main(String[] args) {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.DROP_AND_CREATE);

        EntityManager em = emf.createEntityManager();
        PersonFacade personfacade = PersonFacade.getPersonFacade(emf);

        try {
            em.getTransaction().begin();
            Person p = new Person("Peter", "Petersen");
            Person p1 = new Person("Lars", "Larsen");
            Person p2 = new Person("Hans", "Hansen");

            InfoEntity pi = new InfoEntity("peter@mail.dk");
            InfoEntity p1i = new InfoEntity("lars@mail.dk");
            InfoEntity p2i = new InfoEntity("hans@mail.dk");

            Phone phone = new Phone("12341", "Home");
            Phone phone1 = new Phone("12342", "Home");
            Phone phone2 = new Phone("12343", "Home");

            Hobby h = new Hobby("Badminton", "Det er virkelig kedeligt");
            Hobby h1 = new Hobby("Ridning", "Meget sjovere end badminton!");

            Address pa = new Address("Sømoseparken", "80, st., 37");
            Address p1a = new Address("Sorrentovej", "1");
            Address p2a = new Address("Engvej", "40");

            CityInfo p12ac = new CityInfo(2300, "København");
            CityInfo pac = new CityInfo(2750, "Ballerup");

            List<Hobby> phobbies = new ArrayList<>();
            phobbies.add(h);
            phobbies.add(h1);

            List<Hobby> p1h = new ArrayList<>();
            p1h.add(h);

            List<Hobby> p2h = new ArrayList<>();
            p2h.add(h1);

            pi.setPerson(p);
            p1i.setPerson(p1);
            p2i.setPerson(p2);

            phone.setInfoEntity(pi);
            phone1.setInfoEntity(pi);
            phone2.setInfoEntity(p1i);

            p.setHobbies(phobbies);
            p1.setHobbies(p1h);
            p2.setHobbies(p2h);

            pa.setCityInfo(pac);
            p1a.setCityInfo(p12ac);
            p2a.setCityInfo(p12ac);

            pi.setAddress(pa);
            p1i.setAddress(p1a);
            p2i.setAddress(p2a);

            em.persist(phone);
            em.persist(phone1);
            em.persist(phone2);
            em.persist(p);
            em.persist(p1);
            em.persist(p2);
            em.persist(pi);
            em.persist(p1i);
            em.persist(p2i);
            em.persist(h);
            em.persist(h1);
            em.persist(pa);
            em.persist(p1a);
            em.persist(p2a);
            em.persist(p12ac);
            em.persist(pac);

            em.getTransaction().commit();

            
        } finally {
            em.close();
        }
        
        
        /*
        List<HobbyDTO> h = new ArrayList<>();
        h.add(new HobbyDTO(new Hobby("Ridning", "Yay")));
        List<PhoneDTO> phodeto = new ArrayList<>();
        phodeto.add(new PhoneDTO(new Phone("4321", "Lortetelefon")));
        
        PersonDTO p = new PersonDTO(new Person("Lotte", "Flottesen"), 
                h, new InfoEntity("lotte@mail.dk"),
                new Address("Lottevej", "100"), new CityInfo(2600, "Lotteby")
                , phodeto);
        
        personfacade.createPerson(p);
        
        personfacade.createPerson("Annika", "Ehlers", h, "annika@mail.dk", "Hejvej", "80", "Ballerup", 2750, phodeto);
        */
    }
    
}

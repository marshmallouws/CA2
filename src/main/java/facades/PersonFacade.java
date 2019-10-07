package facades;

import dto.PersonDTO;
import entities.Address;
import entities.CityInfo;
import entities.InfoEntity;
import entities.Hobby;
import entities.Person;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public long getPersonCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long renameMeCount = (long) em.createQuery("SELECT COUNT(r) FROM Person r").getSingleResult();
            return renameMeCount;
        } finally {
            em.close();
        }
    }

    // Should be changed to not be a TypedQuery + try-with-resources
    public List<PersonDTO> findByHobby(CityInfo h) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Person> query
                    = em.createQuery("SELECT person, hobby, address, info, cityinfo, phone "
                            + "FROM Person person, Hobby hobby, Address address, InfoEntity info, CityInfo cityinfo, Phone phone "
                            + "WHERE cityinfo.zip = :zip", Person.class);
                            
            query.setParameter("zip", h.getZip());
            
            List<Person> q = query.getResultList();
            List<PersonDTO> res = new ArrayList<>();

            for (Person p : q) {
                res.add(new PersonDTO(p));
            }

            return res;

        } finally {
            em.close();
        }
    }

    /*
    public List<PersonDTO> findByHobby(Hobby h) {
        EntityManager em = getEntityManager();
        List<PersonDTO> res = new ArrayList<>();
        try {
            TypedQuery<Person> query
                    = em.createQuery("SELECT p FROM Person p JOIN p.hobbies h WHERE h = :hobbyName", Person.class);
            query.setParameter("hobbyName", h);
            List<Person> persons = query.getResultList();
            
            for(Person p: persons) {
                res.add(new PersonDTO(p));
            }
        } finally {
            em.close();
        }
        
        return res;
    }*/
    public List<Person> findByZip(CityInfo cityInfo) {
        EntityManager em = getEntityManager();
        //List<PersonDTO> res = new ArrayList<>();

        try {
            TypedQuery<Person> query
                    = em.createQuery("SELECT p FROM Person p JOIN p.address.cityInfo c WHERE c =:cityinfo ", Person.class);
            query.setParameter("cityinfo", cityInfo);
            return query.getResultList();

            /*for(Person p: persons) {
            
                res.add(new PersonDTO(p));
            } */
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.DROP_AND_CREATE);

        EntityManager em = emf.createEntityManager();
        PersonFacade personfacade = new PersonFacade();

        try {
            em.getTransaction().begin();
            Person p = new Person("Peter", "Petersen");
            Person p1 = new Person("Lars", "Larsen");
            Person p2 = new Person("Hans", "Hansen");

            InfoEntity pi = new InfoEntity("peter@mail.dk");
            InfoEntity p1i = new InfoEntity("lars@mail.dk");
            InfoEntity p2i = new InfoEntity("hans@mail.dk");

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

//            p.setInfoEntity(pi);
//            p1.setInfoEntity(p1i);
//            p2.setInfoEntity(p2i);

            p.setHobbies(phobbies);
            p1.setHobbies(p1h);
            p2.setHobbies(p2h);

            pa.setCityInfo(pac);
            p1a.setCityInfo(p12ac);
            p2a.setCityInfo(p12ac);

            pi.setAddress(pa);
            p1i.setAddress(p1a);
            p2i.setAddress(p2a);

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
            
            List<PersonDTO> persondto = personfacade.findByHobby(pac);
            System.out.println(persondto.size());
            
            for(PersonDTO dto: persondto) {
                System.out.println(dto.getName());
            }
        } finally {
            em.close();
        }
        
        
    }

}

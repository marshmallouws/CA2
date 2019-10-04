package facades;

import dto.PersonDTO;
import entities.Address;
import entities.CityInfo;
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
    private PersonFacade() {}
    
    
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
    
    public long getPersonCount(){
        EntityManager em = emf.createEntityManager();
        try{
            long renameMeCount = (long)em.createQuery("SELECT COUNT(r) FROM Person r").getSingleResult();
            return renameMeCount;
        }finally{  
            em.close();
        }
    }
    
    public List<Person> findByHobby(Hobby h) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Person> query
                    = em.createQuery("SELECT p FROM Person p JOIN p.hobbies h WHERE h = :hobbyName", Person.class);
            query.setParameter("hobbyName", h);
            return query.getResultList();
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
            Person p = new Person("peter@mail.dk", "Peter", "Petersen");
            Person p1 = new Person("lars@mail.dk", "Lars", "Larsen");
            Person p2 = new Person("hans@mail.dk", "Hans", "Hansen");
            
            Hobby h = new Hobby("Badminton", "Det er virkelig kedeligt");
            Hobby h1 = new Hobby("Ridning", "Meget sjovere end badminton!");
            
            Address a = new Address("Sømoseparken", "80, st., 37");
            Address a1 = new Address("Sorrentovej", "1");
            Address a2 = new Address("Engvej", "40");
            
            CityInfo c = new CityInfo(2300, "København");
            CityInfo c1 = new CityInfo(2750, "Ballerup");
            
            a1.setCityInfo(c);
            a2.setCityInfo(c);
            a.setCityInfo(c1);
            
            p.setAddress(a);
            p1.setAddress(a1);
            p2.setAddress(a2);
            
            p.addHobby(h);
            p.addHobby(h1);
            p2.addHobby(h);
            p1.addHobby(h1);
            
            h.setPersons(p);
            h.setPersons(p2);
            h1.setPersons(p);
            h1.setPersons(p1);
            
            em.persist(a);
            em.persist(a1);
            em.persist(a2);
            em.persist(c);
            em.persist(c1);
            em.persist(p);
            em.persist(p1);
            em.persist(p2);
            em.persist(h);
            em.persist(h1);
            em.getTransaction().commit();
            
            List<Person> persons = personfacade.findByZip(c);
            
            for(Person person: persons) {
                System.out.println(person.getFirstname());
            }
            
            /*List<PersonDTO> persons = personfacade.findByHobby(h);
            
            for(PersonDTO person: persons) {
                System.out.println(person.getName());
            } */
        } finally {
            em.close();
        }
    }
    
}


/*
    public Person(String email, String firstname, String lastname) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
    }
*/
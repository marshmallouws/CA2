package facades;

import dto.HobbyDTO;
import dto.PersonDTO;
import dto.PhoneDTO;
import entities.Address;
import entities.CityInfo;
import entities.InfoEntity;
import entities.Hobby;
import entities.Phone;
import entities.Person;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;
import java.lang.reflect.*;
import java.util.Arrays;

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

    /*
    The query in the following method returns the same person multiple times
    (once for each phone number the person has). Therefore, there's a check
    on whether the given id already exists in the res-list to avoid duplicates.
    The query should be changed, however, I couldn't find out how.
    */
    public List<PersonDTO> findByHobby(String name) {
        EntityManager em = getEntityManager();
        try {
            Query query
                    = em.createQuery("SELECT p, h, i, a, c, ph FROM Person p "
                            + "JOIN p.hobbies h "
                            + "JOIN p.infoEntity i "
                            + "JOIN i.address a "
                            + "JOIN a.cityInfo c "
                            + "JOIN i.phones ph "
                            + "WHERE h.name = :name");

            query.setParameter("name", name);
            List<Object[]> q = query.getResultList();
            List<PersonDTO> res = new ArrayList<>();
            int lastSeenID = 0;
            for (Object o[] : q) {
                Person p = (Person) o[0];
                Hobby l = (Hobby) o[1];
                InfoEntity e = (InfoEntity) o[2];
                Address a = (Address) o[3];
                CityInfo c = (CityInfo) o[4];
                Phone ph = (Phone) o[5];
               
                
                List<PhoneDTO> phones = new ArrayList<>();
                List<HobbyDTO> hobbies = new ArrayList<>();
                
                phones.add(new PhoneDTO(ph));
                hobbies.add(new HobbyDTO(l));

                if (lastSeenID == p.getId()) {
                    res.get(res.size()-1).getPhones().add(new PhoneDTO(ph));
                } else {
                    res.add(new PersonDTO(p, hobbies, e, a, c, phones));
                }

                lastSeenID = p.getId();
            }

            return res;

        } finally {
            em.close();
        }
    }
    
    /*
    Should be altered to no enter duplicate hobbies, address and cityinfo
    */
    public void createPerson(String firstname, String lastname, List<HobbyDTO> hdto, String email, String street, String additional, String city, int zip, List<PhoneDTO> pdto) {
        EntityManager em = getEntityManager(); 
        try {
            em.getTransaction().begin();
            Person p = new Person(firstname, lastname);
            InfoEntity e = new InfoEntity(email);
            Address a = new Address(street, additional);
            CityInfo c = new CityInfo(zip, city);
            List<Hobby> hobbies = new ArrayList<>();
            
            for(HobbyDTO h: hdto) {
                Hobby hobby = new Hobby(h.getName(), h.getDescription());
                hobbies.add(hobby);
                em.persist(hobby);
            }
            
            for(PhoneDTO ph: pdto) {
                Phone phone = new Phone(ph.getNumber(), ph.getDescription());
                phone.setInfoEntity(e);
                em.persist(phone);
            }
            
            e.setPerson(p);
            e.setAddress(a);
            a.setCityInfo(c);
            p.setHobbies(hobbies);
            
            em.persist(p);
            em.persist(e);
            em.persist(a);
            em.persist(c);
            
            em.getTransaction().commit();
            
        } finally {
            em.close();
        }
    }

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
}

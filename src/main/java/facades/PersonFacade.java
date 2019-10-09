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
    
     public List<PersonDTO> getAllPersons() {
        EntityManager em = getEntityManager();
        try {
            List<PersonDTO> dto = new ArrayList();
            for(Person person : em.createQuery("SELECT p FROM Person p", Person.class).getResultList()){
                dto.add(new PersonDTO(person));
            }
            return dto;
        } finally {
            em.close();
        }
    }

    public List<PersonDTO> findByHobby(String hobbyname) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.hobbies h WHERE h.name = :hobbyname", Person.class);
            query.setParameter("hobbyname", hobbyname);
            List<Person> persons = query.getResultList();
            List<PersonDTO> res = new ArrayList<>();
            
            for(Person p: persons) {
                InfoEntity inf = p.getInfoEntity();
                Address add = inf.getAddress();
                CityInfo city = add.getCityInfo();
                List<Hobby> hobbies = p.getHobbies();
                List<HobbyDTO> ho = new ArrayList<>();
                List<Phone> phones = inf.getPhones();
                List<PhoneDTO> pdto = new ArrayList<>();
                
                for(Hobby h: hobbies) {
                    ho.add(new HobbyDTO(h));
                }
                
                for(Phone po: phones) {
                    pdto.add(new PhoneDTO(po));
                }
                
                res.add(new PersonDTO(p, ho, inf, add, city, pdto));
                
            }

            return res;

        } finally {
            em.close();
        }
    }

    /*
    Should be altered to no enter duplicate hobbies, address and cityinfo
     */
    public PersonDTO createPerson(PersonDTO p) {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();
            Person person = new Person(p.getFirstname(), p.getLastname());
            InfoEntity info = new InfoEntity(p.getEmail());
            Address address = new Address(p.getStreet(), p.getAdditionalinfo());
            CityInfo city = new CityInfo(p.getZip(), p.getCity());

            List<HobbyDTO> hobbies = p.getHobbies();
            List<PhoneDTO> phones = p.getPhones();
            List<Hobby> hobb = new ArrayList<>();

            for (HobbyDTO h : hobbies) {
                Hobby hobby = new Hobby(h.getName(), h.getDescription());
                hobb.add(hobby);
                em.persist(hobby);
            }

            for (PhoneDTO ph : phones) {
                Phone phone = new Phone(ph.getNumber(), ph.getDescription());
                phone.setInfoEntity(info);
                em.persist(phone);
            }

            info.setPerson(person);
            info.setAddress(address);
            address.setCityInfo(city);
            person.setHobbies(hobb);

            em.persist(person);
            em.persist(info);
            em.persist(address);
            em.persist(city);

            em.getTransaction().commit();
            
            return new PersonDTO(person);
        } finally {

        }
    }

    public void deletePerson(int id) {

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

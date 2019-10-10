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
            List<Person> persons = em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
            for (Person person : persons) {
                dto.add(new PersonDTO(person));
            }
            return dto;
        } finally {
            em.close();
        }
    }

    public List<PersonDTO> findByHo(String hobbyname) {
        EntityManager em = getEntityManager();
        try {

            TypedQuery<Person> query
                    = em.createQuery("SELECT p FROM Person p JOIN p.hobbies h WHERE h.name = :hobbyname", Person.class);
            query.setParameter("hobbyname", hobbyname);
            List<Person> persons = query.getResultList();
            List<PersonDTO> res = new ArrayList<>();

            for (Person p : persons) {
                res.add(new PersonDTO(p));
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
            Address address = new Address(p.getStreet(), p.getAdditionalinfo(),new CityInfo(p.getZip(), p.getCity()));
            List<Phone> phones = new ArrayList();
            List<Hobby> hobbies = new ArrayList();

            for (HobbyDTO h : p.getHobbies()) {
                hobbies.add(new Hobby(h.getName(), h.getDescription()));
            }

            for (PhoneDTO ph : p.getPhones()) {
                phones.add(new Phone(ph.getNumber(), ph.getDescription()));
            }

            Person person = new Person(p.getEmail(), p.getFirstname(), p.getLastname(),hobbies,phones,address);
            em.persist(person);
            em.getTransaction().commit();

            return new PersonDTO(person);
        } finally {

        }
    }

    public PersonDTO updatePerson(PersonDTO p) {
        EntityManager em = emf.createEntityManager();

        try {
            Person person = (Person) em.createQuery("SELECT p FROM Person p WHERE p.id = " + p.getId()).getSingleResult();

            List<Hobby> hobbyList = new ArrayList();
            for (HobbyDTO h : p.getHobbies()) {
                hobbyList.add(new Hobby(h.getName(), h.getDescription()));
            }

      
            List<Phone> phoneList = new ArrayList();
            for (PhoneDTO pdto : p.getPhones()) {
                phoneList.add(new Phone(pdto.getNumber(), pdto.getDescription()));
            }
            
            em.getTransaction().begin();
            person.setFirstname(p.getFirstname());
            person.setLastname(p.getLastname());
            person.setEmail(p.getEmail());
            person.setHobbies(hobbyList);
            person.setPhones(phoneList);
            person.getAddress().setStreet(p.getStreet());
            person.getAddress().setAdditionalInfo(p.getAdditionalinfo());
            person.getAddress().getCityInfo().setCity(p.getCity());
            person.getAddress().getCityInfo().setZip(p.getZip());

            em.getTransaction().commit();

            return new PersonDTO(person);
        } finally {
            em.close();
        }
    }

    public void deletePerson(int id) {

    }

    public List<PersonDTO> findByZip(int zip) {
        EntityManager em = getEntityManager();

        try {
            TypedQuery<Person> query
                    = em.createQuery("SELECT p FROM Person p JOIN p.infoEntity.address.cityInfo c WHERE c.zip = :zip", Person.class);
            query.setParameter("zip", zip);
            List<Person> persons = query.getResultList();
            List<PersonDTO> res = new ArrayList<>();
            for (Person p : persons) {
                res.add(new PersonDTO(p));
            }
            return res;
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        EntityManagerFactory emf2 = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
        List<PersonDTO> persons = getPersonFacade(emf2).findByHo("Ridning");
        for (PersonDTO p : persons) {
            System.out.println(p.getHobbies() + " " + p.getCity());
        }
    }
}

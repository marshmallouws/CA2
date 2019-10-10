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

    public long getPersonHobbyCount(String hobbyname) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT COUNT(p) FROM Person p JOIN p.hobbies h WHERE h.name = :hobbyname");
            query.setParameter("hobbyname", hobbyname);
            Long res = (Long) query.getSingleResult();
            
            return res;
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

    public List<PersonDTO> findByHobby(String hobbyname) {
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
            person.setInfoEntity(info);
            em.persist(person);
            em.persist(info);
            em.persist(address);
            em.persist(city);

            em.getTransaction().commit();

            return new PersonDTO(person);
        } finally {

        }
    }

    public PersonDTO updatePerson(PersonDTO p) {
        EntityManager em = emf.createEntityManager();

        try {
            InfoEntity IE = (InfoEntity) em.createQuery("SELECT i FROM InfoEntity i WHERE i.person.id = " + p.getId()).getSingleResult();

            List<Hobby> hobbyList = new ArrayList();

            for (HobbyDTO h : p.getHobbies()) {
                hobbyList.add(new Hobby(h.getName(), h.getDescription()));
            }

            em.getTransaction().begin();

            for (PhoneDTO pdto : p.getPhones()) {
                Phone phone = new Phone(pdto.getNumber(), pdto.getDescription());
                phone.setInfoEntity(IE);
                em.persist(phone);
            }

            IE.getPerson().setFirstname(p.getFirstname());
            IE.getPerson().setLastname(p.getLastname());
            IE.setEmail(p.getEmail());
            IE.getPerson().setHobbies(hobbyList);
            IE.getAddress().setStreet(p.getStreet());
            IE.getAddress().setAdditionalInfo(p.getAdditionalinfo());
            IE.getAddress().getCityInfo().setCity(p.getCity());
            IE.getAddress().getCityInfo().setZip(p.getZip());

            IE.getPerson().setInfoEntity(IE);

            em.getTransaction().commit();

            return new PersonDTO(IE.getPerson());
        } finally {
            em.close();
        }
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
}

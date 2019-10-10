package facades;

import dto.HobbyDTO;
import dto.PersonDTO;
import dto.PhoneDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Phone;
import entities.Person;
import exceptions.CityInfoNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.NoResultException;
import utils.EMF_Creator;

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

    public PersonDTO createPerson(PersonDTO p) throws CityInfoNotFoundException {
        EntityManager em = getEntityManager();

        try {
            CityInfo city = null;
            try {
                city = CityFacade.getCityFacade(emf).getCity(p.getZip());
            } catch (NoResultException e) {
                throw new CityInfoNotFoundException("Given zip is not found");
            }
            
            
            Address address = new Address(p.getStreet(), p.getAdditionalinfo());
            address.setCityInfo(city);
            List<Phone> phones = new ArrayList();
            List<Hobby> hobbies = new ArrayList();
            
            for (HobbyDTO h : p.getHobbies()) {
                try {
                    Hobby hobby = HobbyFacade.getHobbyFacade(emf).getHobby(h.getName());
                    hobbies.add(hobby);
                } catch (NoResultException e) {
                    hobbies.add(new Hobby(h.getName(), h.getDescription()));
                } 
            }

            for (PhoneDTO ph : p.getPhones()) {
                phones.add(new Phone(ph.getNumber(), ph.getDescription()));
            }
            
            for(Hobby fuck: hobbies) {
                System.out.println(fuck.getName());
            }
            
            Person person = new Person(p.getEmail(), p.getFirstname(), p.getLastname(), hobbies, phones, address);
            em.getTransaction().begin();
            // Using merge to ensure that there won't be duplicate hobbies.
            em.merge(person);
            em.getTransaction().commit();
            
            return new PersonDTO(person);
        } finally {

        }
    }

    public PersonDTO updatePerson(PersonDTO p) throws CityInfoNotFoundException {
        EntityManager em = emf.createEntityManager();
        CityInfo city = null;

        try {
            try {
                city = CityFacade.getCityFacade(emf).getCity(p.getZip());
            } catch (NoResultException e) {
                throw new CityInfoNotFoundException("Given zip is not found");
            }
            
            Person person = (Person) em.createQuery("SELECT p FROM Person p WHERE p.id = " + p.getId()).getSingleResult();
            
            List<Hobby> hobbyList = new ArrayList();
            List<HobbyDTO> hobbies = p.getHobbies();
            for (HobbyDTO h : hobbies) {
                try {
                    Hobby hobby = HobbyFacade.getHobbyFacade(emf).getHobby(h.getName());
                    //hobbyList.add(hobby);
                } catch (NoResultException e) {
                    hobbyList.add(new Hobby(h.getName(), h.getDescription()));
                }
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
            person.getAddress().setCityInfo(city);
            em.getTransaction().commit();

            return new PersonDTO(person);
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
    
    public static void main(String[] args) throws CityInfoNotFoundException {
        EntityManagerFactory emf2 = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
        PersonDTO person = getPersonFacade(emf2).getAllPersons().get(0);
        /*List<HobbyDTO> hobbies = new ArrayList<>();
        hobbies.add(new HobbyDTO(new Hobby("Ridning", "Something")));
        hobbies.add(new HobbyDTO(new Hobby("Klatring", "#klatretøsen")));
        
        
        person.setHobbies(hobbies);
        person.setFirstname("Gokke Jokke");
        getPersonFacade(emf2).updatePerson(person);
        */
        Person p = new Person("lotte@mail.dk", "Frau", "Lotte");
        List<Hobby> h = new ArrayList<>();
        h.add(new Hobby("Ridning", "Somethigbn"));
        h.add(new Hobby("Dykning", "Hajer er da meget søde"));
        List<Phone> ph = new ArrayList<>();
        p.setHobbies(h);
        p.setPhones(ph);
        p.setAddress(new Address("vingevje", "230", new CityInfo(9900, "Ballerup")));
        p.setId(1);
        getPersonFacade(emf2).createPerson(new PersonDTO(p));
    }
}

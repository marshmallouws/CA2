package facades;

import dto.PersonDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.InfoEntity;
import utils.EMF_Creator;
import entities.Person;
import entities.Phone;
import exceptions.CityInfoNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.Settings;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static CityFacade cf;

    public PersonFacadeTest() {
    }

    //@BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/ca2_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.DROP_AND_CREATE);
        facade = PersonFacade.getPersonFacade(emf);
    }

    /*   **** HINT **** 
        A better way to handle configuration values, compared to the UNUSED example above, is to store those values
        ONE COMMON place accessible from anywhere.
        The file config.properties and the corresponding helper class utils.Settings is added just to do that. 
        See below for how to use these files. This is our RECOMENDED strategy
     */
    static Person ptest;
    
    @BeforeAll
    public static void setUpClassV2() throws IOException {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        facade = PersonFacade.getPersonFacade(emf);
        cf = CityFacade.getCityFacade(emf);
        
        EntityManager em = emf.createEntityManager();
        try {
            cf.addCities();
        } catch (IOException ex) {
        }
//        try {
//            cf.getCity(3000);
//        } catch (NoResultException e) {
//            try{
//            cf.addCities();
//            }catch(Exception ex){
//                
//            }
//        }
        try {
            
            
            CityInfo city = cf.getCity(2300);
            em.getTransaction().begin();
            Phone phone = new Phone("12341", "Home");
            Hobby h = new Hobby("Badminton", "Det er virkelig kedeligt");
            Address pa = new Address("Sømoseparken", "80, st., 37", city);
            List<Hobby> phobbies = new ArrayList();
            phobbies.add(h);
            List<Phone> phones = new ArrayList();
            phones.add(phone);
  
            ptest = new Person("test@test.dk","Stallone", "Stalloni", phobbies, phones, pa);
            em.persist(ptest);
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }


    @Test
    public void getAll_personsInDB_returnsPersonListNotEmpty() {
        List<PersonDTO> list = facade.getAllPersons();
        assertThat(list, is(not(empty())));
    }
    
    @Test
    public void createPerson_validPerson_idNotNull() throws CityInfoNotFoundException {
        PersonDTO dto = new PersonDTO(ptest);
        assertThat(facade.createPerson(dto), is(not(nullValue())));
    }
    
    @Test
    public void editPerson_validPerson_newValueAdded() throws CityInfoNotFoundException {
        PersonDTO edited = new PersonDTO(ptest); 
        edited.setFirstname("Lars");
        edited = facade.updatePerson(edited);
        assertThat(edited.getFirstname(), is("Lars"));
    }
    

}

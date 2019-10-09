package facades;

import dto.PersonDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.InfoEntity;
import utils.EMF_Creator;
import entities.Person;
import entities.Phone;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Settings;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;

    public PersonFacadeTest() {
    }

    //@BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/ca2_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
        facade = PersonFacade.getPersonFacade(emf);
    }

    /*   **** HINT **** 
        A better way to handle configuration values, compared to the UNUSED example above, is to store those values
        ONE COMMON place accessible from anywhere.
        The file config.properties and the corresponding helper class utils.Settings is added just to do that. 
        See below for how to use these files. This is our RECOMENDED strategy
     */
    static Person ptest = new Person("Stallone", "Stalloni");
    
    @BeforeAll
    public static void setUpClassV2() {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        facade = PersonFacade.getPersonFacade(emf);

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Person p = new Person("Peter", "Petersen");
            
            InfoEntity pi = new InfoEntity("peter@mail.dk");


            Phone phone = new Phone("12341", "Home");


            Hobby h = new Hobby("Badminton", "Det er virkelig kedeligt");
     

            Address pa = new Address("Sømoseparken", "80, st., 37");
        

            CityInfo p12ac = new CityInfo(2300, "København");
           

            List<Hobby> phobbies = new ArrayList<>();
            phobbies.add(h);


           

            p.setInfoEntity(pi);
            
            phone.setInfoEntity(pi);
 

            p.setHobbies(phobbies);
          

            pa.setCityInfo(p12ac);
  

            pi.setAddress(pa);
            
            ptest.setInfoEntity(pi);
            ptest.setHobbies(phobbies);

            em.persist(phone);
  
            em.persist(p);
      
            em.persist(pi);
       
            em.persist(h);
          
            em.persist(pa);
     
            em.persist(p12ac);
      

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
//    @BeforeEach
//    public void setUp() {
//        EntityManager em = emf.createEntityManager();
//        try {
//            em.getTransaction().begin();
//            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
//            em.persist(new Person("Some txt", "More text", "last"));
//            em.persist(new Person("aaa", "bbb", "ccc"));
//
//            em.getTransaction().commit();
//        } finally {
//            em.close();
//        }
//    }
//    @AfterEach
//    public void tearDown() {
////        Remove any data after each test was run
//    }
//
//    // TODO: Delete or change this method 
//    @Test
//    public void testAFacadeMethod() {
//        assertEquals(2, facade.getPersonCount(), "Expects two rows in the database");
//    }
    @Test
    public void getAll_personsInDB_returnsPersonListNotEmpty() {
        List<PersonDTO> list = facade.getAllPersons();
        assertThat(list, is(not(empty())));
    }
    
    @Test
    public void createPerson_validPerson_idNotNull() {
        PersonDTO dto = new PersonDTO(ptest);
        assertThat(facade.createPerson(dto), is(not(nullValue())));
    }
    
    

}

package facades;

import dto.CompanyDTO;
import dto.PersonDTO;
import entities.Address;
import entities.CityInfo;
import entities.Company;
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
import static org.hamcrest.Matchers.greaterThan;
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
public class CompanyFacadeTest {

    private static EntityManagerFactory emf;
    private static CompanyFacade facade;
    private static CityFacade cf;

    public CompanyFacadeTest() {
    }

    //@BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/ca2_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.DROP_AND_CREATE);
        facade = CompanyFacade.getCompanyFacade(emf);
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
        facade = CompanyFacade.getCompanyFacade(emf);
        cf = CityFacade.getCityFacade(emf);
        
        EntityManager em = emf.createEntityManager();
        try {
            cf.getCity(3000);
        } catch (NoResultException e) {
            try{
            cf.addCities();
            }catch(Exception ex){
                
            }
        }
        try {
           
            
            CityInfo c = cf.getCity(2300);
            CityInfo c3 = cf.getCity(6520);
            CityInfo c4 = cf.getCity(8220);
            Phone phone3 = new Phone("12341", "Home");
            Phone phone4 = new Phone("12342", "Home");

            Phone phone7 = new Phone("12342", "Home");
            Phone phone8 = new Phone("12343", "Home");
            
            List<Phone> fbphone = new ArrayList();
            fbphone.add(phone3);
            fbphone.add(phone4);
            
            List<Phone> onep = new ArrayList();
            onep.add(phone7);
            onep.add(phone8);
            
            Company co = new Company("facebook@mail.dk", "Facebook", "We don't stalk you", "1234", 1000, 90000000, fbphone, new Address("facebookvej", "22", c));
            Company co1 = new Company("arla@mail.dk", "Arla", "We take calves from their mother so you can get milk", "4321", 231, 8000000, fbphone, new Address("arlavej", "103", c3));
            Company co2 = new Company("moccamaster@mail.dk", "Moccamaster", "Helping you through the day", "1234", 10, 90000000, fbphone, new Address("facebookvej", "22", c4));
            Company co3 = new Company("oneplus@mail.dk", "OnePlus", "China runs the world", "1234", 150, 90000000, onep, new Address("facebookvej", "22", c4));
            
            em.getTransaction().begin();
            em.persist(co);
            em.persist(co1);
            em.persist(co2);
            em.persist(co3);
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
    public void getCompanies_input10_returnsCompanyWithMoreThan10Employees() {
        List<CompanyDTO> list = facade.getCompanies(10);
        assertThat(list,is(not(empty())));
        for(CompanyDTO dto : list){
            assertThat(dto.getNumOfEmployees(),is(greaterThan(10)));
        }
    }
    
    
    

}

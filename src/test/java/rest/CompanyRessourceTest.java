package rest;

import entities.Address;
import entities.CityInfo;
import entities.Company;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import facades.CityFacade;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class CompanyRessourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    
    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    static Person ptest;
    
    @BeforeAll
    public static void setUpClass() throws IOException {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        
        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
        
        CityFacade cf = CityFacade.getCityFacade(emf);
        try {
            cf.addCities();
        } catch (IOException ex) {
        }
        
//        try {
//            cf.getCity(3000);
//        } catch (NoResultException e) {
//            cf.addCities();
//        }
        
        EntityManager em = emf.createEntityManager();
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
    public static void closeTestServer(){
        //System.in.read();
         //Don't forget this, if you called its counterpart in @BeforeAll
         EMF_Creator.endREST_TestWithDB();
         httpServer.shutdownNow();
    }
    
    @Test
    public void getCompanies_input10_returnsListSizeGreaterThan0() throws Exception {
        given()
                .contentType("application/json")
                .get("/company/employees/10").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size()", is(greaterThan(0)));
    }
    
}

package rest;

import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.InfoEntity;
import entities.Person;
import entities.Phone;
import facades.PersonFacade;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
public class PersonRessourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    
    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    static Person ptest = new Person("Stallone", "Stalloni");
    
    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        
        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;

        
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
    public static void closeTestServer(){
        //System.in.read();
         //Don't forget this, if you called its counterpart in @BeforeAll
         EMF_Creator.endREST_TestWithDB();
         httpServer.shutdownNow();
    }
    
    @Test
    public void getAll_PersonsInDB_returnsListSizeGreaterThan0() throws Exception {
        given()
                .contentType("application/json")
                .get("/person/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size()", is(greaterThan(0)));
    }
    
    
    @Test
    public void createPerson_validPerson_idNotNull() throws Exception {
        String payload = "{\"firstname\":\"Hans\",\"lastname\":\"Hansen\",\"email\":\"test@test.dk\",\"street\": \"Sømoseparken\", \"additionalinfo\": \"80, st., 37\", \"city\": \"Ballerup\",\"zip\": 2750, \"phones\": [{ \"number\": \"12341\", \"description\": \"Home\" }, { \"number\": \"12342\", \"description\": \"Home\" }],\"hobbies\": [ { \"name\": \"Ridning\", \"description\": \"Meget sjovere end badminton!\" }, { \"name\": \"Badminton\", \"description\": \"Det er virkelig kedeligt\" } ]}";
        given().contentType(ContentType.JSON)
                .body(payload)
                .post("/person")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", is(not(nullValue())));
    }
    
    @Test
    public void updatePerson_validPerson_nameChanged() throws Exception {
        String payload = "{\"id\":\"1\",\"firstname\":\"Søren\",\"lastname\":\"Hansen\",\"email\":\"test@test.dk\",\"street\": \"Sømoseparken\", \"additionalinfo\": \"80, st., 37\", \"city\": \"Ballerup\",\"zip\": 2750, \"phones\": [{ \"number\": \"12341\", \"description\": \"Home\" }, { \"number\": \"12342\", \"description\": \"Home\" }],\"hobbies\": [ { \"name\": \"Ridning\", \"description\": \"Meget sjovere end badminton!\" }, { \"name\": \"Badminton\", \"description\": \"Det er virkelig kedeligt\" } ]}";
        given().contentType(ContentType.JSON)
                .body(payload)
                .put("/person")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstname", is("Søren"));
    }
}

package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonDTO;
import entities.Person;
import exceptions.CityInfoNotFoundException;
import exceptions.CityNotFoundExceptionMapper;
import facades.CityFacade;
import facades.CompanyFacade;
import utils.EMF_Creator;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//Todo Remove or change relevant parts before ACTUAL use
@Path("company")
public class CompanyRessource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV,EMF_Creator.Strategy.CREATE);
    private static final CompanyFacade FACADE =  CompanyFacade.getCompanyFacade(EMF);
    //private static final CityInfo CFACADE = CityFacade.getCityFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Path("/employees/{number}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getNumberOfEmployees(@PathParam("number") int num) {
        return Response.ok().entity(GSON.toJson(FACADE.getCompanies(num))).build();
    }

 
}

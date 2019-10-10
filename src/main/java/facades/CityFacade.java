/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CityInfo;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;

/**
 *
 * @author Annika
 */
public class CityFacade {
    private static CityFacade instance;
    private static EntityManagerFactory emf;
    
    private CityFacade() {}
    
    public static CityFacade getCityFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CityFacade();
        }
        return instance;
    } 
    
    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public List<CityInfo> getAllZip() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<CityInfo> inf = 
                    em.createQuery("SELECT c FROM CityInfo c", CityInfo.class);
            return inf.getResultList();
        } finally {
            em.close();
        }
    }
    
    
    public void addCities() throws FileNotFoundException, IOException {
        EntityManager em = getEntityManager();
        System.out.println();
        BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\zipcodes.txt"));
        
        String row = "";
        while((row = reader.readLine()) != null) {
            String[] data = row.split(" ", 2);
            em.getTransaction().begin();
            em.persist(new CityInfo(Integer.parseInt(data[0]), data[1]));
            em.getTransaction().commit();
        }
        em.close();
        reader.close();
    }
    
    public static void main(String[] args) throws IOException {
        EntityManagerFactory emf2 = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
        getCityFacade(emf2).addCities();
    } 
}

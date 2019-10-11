/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import data.Zip;
import entities.CityInfo;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
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
    
    public CityInfo getCity(int zip) throws NoResultException {
        EntityManager em = getEntityManager();
        try {
            //if(em.createQuery("SELECT c FROM CityInfo c WHERE c.zip = :zip", CityInfo.class).getSingleResult() != null)
            TypedQuery<CityInfo> inf =
                    em.createQuery("SELECT c FROM CityInfo c WHERE c.zip = :zip", CityInfo.class);
            inf.setParameter("zip", zip);
            return inf.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    
    public void addCities() throws FileNotFoundException, IOException {
        EntityManager em = getEntityManager();
        HashMap<Integer, String> zips = Zip.zipcodes;
        try {
            em.getTransaction().begin();
            //zips.forEach((k, v) -> em.persist(new CityInfo(k, v)));
            for(Map.Entry<Integer, String> kv: zips.entrySet()) {
                em.persist(new CityInfo(kv.getKey(), kv.getValue()));
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}

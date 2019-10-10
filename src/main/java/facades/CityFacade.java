/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CityInfo;
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
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<CityInfo> inf = 
                    em.createQuery("SELECT c FROM CityInfo c", CityInfo.class);
            return inf.getResultList();
        } finally {
            em.close();
        }
    }
    
    public static void main(String[] args) {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
        List<CityInfo> zip = getCityFacade(emf).getAllZip();
        for(CityInfo z: zip) {
            System.out.println(z.getZip() + " " +z.getCity());
        }
    }
    
}

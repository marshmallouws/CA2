/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Hobby;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;

/**
 *
 * @author Annika
 */
public class HobbyFacade {
    private static HobbyFacade instance;
    private static EntityManagerFactory emf;
    
    private HobbyFacade(){}
    
    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static HobbyFacade getHobbyFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HobbyFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public Hobby getHobby(String hobbyname) throws NoResultException {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h WHERE h.name = :hobbyname", Hobby.class);
            return query.setParameter("hobbyname", hobbyname).getSingleResult();
        } finally {
            em.close();
        }
    }
    
    public static void main(String[] args) {
        EntityManagerFactory emf2 = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
        //getHobbyFacade(emf2).test();
    }
}

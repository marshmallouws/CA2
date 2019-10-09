/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.InfoEntity;
import entities.Person;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Victor
 */
public class DatabaseFacade {
    private static DatabaseFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private DatabaseFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static DatabaseFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DatabaseFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public Object addToDB(Object o){
        Class type = o.getClass();
        return type.cast(o);
    }
    
    //  Not used - ended up not returning the actual object ref. Known problem.. I don't know how to fix it
    public InfoEntity getInfoEntity(int id) {
        EntityManager em = emf.createEntityManager();
        
        try {
            InfoEntity IE = (InfoEntity)em.createQuery("SELECT i FROM InfoEntity i WHERE i.person.id = " + id).getSingleResult();
            return IE;
        } finally {
            em.close();
        }
    }
}

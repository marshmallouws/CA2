/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dto.CompanyDTO;
import entities.Company;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import utils.EMF_Creator;

/**
 *
 * @author Annika
 */
public class CompanyFacade {
    private static CompanyFacade instance;
    private static EntityManagerFactory emf;
    
    private static CompanyFacade getCompanyFacade(EntityManagerFactory _emf) {
        if(instance == null) {
            emf = _emf;
            instance = new CompanyFacade();
        }
        return instance;
    }
    
    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public List<CompanyDTO> getCompanies(int num) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT c FROM Company c WHERE c.numOfEmployees > :num");
            query.setParameter("num", num);
            List<CompanyDTO> res = new ArrayList<>();
            List<Company> companies = query.getResultList();
            
            for(Company c: companies) {
                res.add(new CompanyDTO(c));
            }
            
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}

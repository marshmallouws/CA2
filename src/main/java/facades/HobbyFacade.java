package facades;

import dto.HobbyDTO;
import entities.Hobby;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class HobbyFacade {

    private static HobbyFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private HobbyFacade() {
    }

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
    
    public List<HobbyDTO> getAllHobbies() {
        EntityManager em = getEntityManager();
        try {
            List<HobbyDTO> dto = new ArrayList<>();
            List<Hobby> hobbies = em.createQuery("SELECT h FROM Hobby h", Hobby.class).getResultList();
            for(Hobby h: hobbies) {
                dto.add(new HobbyDTO(h));
            }
            
            return dto;
        } finally {
            em.close();
        }
    }
}
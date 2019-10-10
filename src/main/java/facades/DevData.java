///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package facades;
//
//import dto.HobbyDTO;
//import dto.PersonDTO;
//import dto.PhoneDTO;
//import entities.Address;
//import entities.CityInfo;
//import entities.Hobby;
//import entities.InfoEntity;
//import entities.Person;
//import entities.Phone;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import utils.EMF_Creator;
//
///**
// *
// * @author Annika
// */
//public class DevData {
//        public static void main(String[] args) {
//        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.DROP_AND_CREATE);
//
//        EntityManager em = emf.createEntityManager();
//        PersonFacade personfacade = PersonFacade.getPersonFacade(emf);
//        CityFacade cityf = CityFacade.getCityFacade(emf);
//        try {
//            try {
//                cityf.addCities();
//            } catch (IOException e) {
//                
//            }
//            
//            em.getTransaction().begin();
//            
//            CityInfo c = cityf.getCity(2300);
//            CityInfo c1 = cityf.getCity(2750);
//            
//
//            Phone phone = new Phone("12341", "Home");
//            Phone phone1 = new Phone("12342", "Home");
//            Phone phone2 = new Phone("12343", "Home");
//
//            Hobby h = new Hobby("Badminton", "Det er virkelig kedeligt");
//            Hobby h1 = new Hobby("Ridning", "Meget sjovere end badminton!");
//
//            Address pa = new Address("Sømoseparken", "80, st., 37", c);
//            Address p1a = new Address("Sorrentovej", "1", c);
//            Address p2a = new Address("Engvej", "40", c1);
//
//            List<Hobby> phobbies = new ArrayList<>();
//            phobbies.add(h);
//            phobbies.add(h1);
//
//            List<Hobby> p1h = new ArrayList<>();
//            p1h.add(h);
//
//            List<Hobby> p2h = new ArrayList<>();
//            p2h.add(h1);
//            
//            List<Phone> plist1 = new ArrayList();
//            plist1.add(phone);
//
//            List<Phone> plist2 = new ArrayList();
//            plist2.add(phone1);
//            plist2.add(phone2);
//
//            
//            Person p = new Person("test1@test1.dk","Peter", "Petersen", phobbies, plist1,pa);
//            Person p1 = new Person("test2@test2.dk","Lars", "Larsen", p1h, plist2,p1a);
//            Person p2 = new Person("test3@test3.dk","Hans", "Hansen", p2h, plist1, p2a);
//            
//            em.persist(p);
//            em.persist(p1);
//            em.persist(p2);
//
//            em.getTransaction().commit();
//
//         
//        } finally {
//            em.close();
//        }
//        
//        
//        /*
//        List<HobbyDTO> h = new ArrayList<>();
//        h.add(new HobbyDTO(new Hobby("Ridning", "Yay")));
//        List<PhoneDTO> phodeto = new ArrayList<>();
//        phodeto.add(new PhoneDTO(new Phone("4321", "Lortetelefon")));
//        
//        PersonDTO p = new PersonDTO(new Person("Lotte", "Flottesen"), 
//                h, new InfoEntity("lotte@mail.dk"),
//                new Address("Lottevej", "100"), new CityInfo(2600, "Lotteby")
//                , phodeto);
//        
//        personfacade.createPerson(p);
//        
//        personfacade.createPerson("Annika", "Ehlers", h, "annika@mail.dk", "Hejvej", "80", "Ballerup", 2750, phodeto);
//        */
//        /*
//        List<PhoneDTO> pdto = new ArrayList();
//        pdto.add(new PhoneDTO(new Phone("12312313", "fake number lol")));
//        List<HobbyDTO> hdto = new ArrayList();
//        hdto.add(new HobbyDTO(new Hobby("Hash", "Basket hobby alligevel")));
//        
//       
//        PersonDTO pDTO = new PersonDTO(1, "Jim", "Daggerthuggert", "jim@daggerthuggert.dk", "HUggertvej", "info info", "Lyngby", 2800, pdto, hdto);
//        personfacade.updatePerson(pDTO);*/
//        }
//    
//}

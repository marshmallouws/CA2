/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Hobby;
import entities.Person;
import entities.Phone;
import java.util.List;

/**
 *
 * @author Bitten
 */
public class PersonDTO {
    private String name;
    private String email;
    private String address;
    private List<PhoneDTO> phones;
    private List<HobbyDTO> hobbies;
    
    
//    public PersonDTO(Person p) {
//        this.name = p.getFirstname() + " " + p.getLastname();
//        this.email = p.getEmail();
//        this.address = p.getAddress().getStreet() 
//                + " " + p.getAddress().getCityInfo().getZip() 
//                + " " + p.getAddress().getCityInfo().getCity();
//        List<Phone> phons = p.getPhones();
//        List<Hobby> hobbs = p.getHobbies();
//        
//        for(Phone ph: phons) {
//            phones.add(new PhoneDTO(ph));
//        }
//        
//        for(Hobby h: hobbs) {
//            hobbies.add(new HobbyDTO(h));
//        }
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<PhoneDTO> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDTO> phones) {
        this.phones = phones;
    }

    public List<HobbyDTO> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<HobbyDTO> hobbies) {
        this.hobbies = hobbies;
    }
}

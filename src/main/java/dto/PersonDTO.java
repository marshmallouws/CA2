/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Hobby;
import entities.Person;
import entities.Phone;
import entities.InfoEntity;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Bitten
 */
public class PersonDTO {
    private String name;
    private String email;
    private String address;
    private List<PhoneDTO> phones = new ArrayList<>();
    private List<HobbyDTO> hobbies = new ArrayList<>();
    
    
    public PersonDTO(Person p) {
        InfoEntity info = p.getInfoEntity();
        
        this.name = p.getFirstname() + " " + p.getLastname();
        this.email = info.getEmail();
        this.address = info.getAddress().getStreet() 
                + " " + info.getAddress().getAdditionalInfo() 
                + " " + info.getAddress().getCityInfo().getZip() 
                + " " + info.getAddress().getCityInfo().getCity();
        List<Phone> ph = info.getPhones();
        List<Hobby> ho = p.getHobbies();
        
        for(Phone phone: ph) {
            phones.add(new PhoneDTO(phone));
        }
        
        for(Hobby h: ho) {
            hobbies.add(new HobbyDTO(h));
        }
        
        
    }

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

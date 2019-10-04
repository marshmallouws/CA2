/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Person;
import java.util.List;

/**
 *
 * @author Bitten
 */
public class PersonDTO {
    private String name;
    private String email;
    private String address;
    private List<String> phones;
    private List<String> hobbies;
    
    
    public PersonDTO(Person p) {
        this.name = p.getFirstname() + " " + p.getLastname();
        this.email = p.getEmail();
        this.address = p.getAddress().getStreet() 
                + " " + p.getAddress().getCityInfo().getZip() 
                + " " + p.getAddress().getCityInfo().getCity();
    }
    
}

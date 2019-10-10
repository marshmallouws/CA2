/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Martin
 */
@Entity
@Table(name = "company")
public class Company extends InfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private String cvr;
    private int numOfEmployees;
    private int marketValue;

    public Company() {
    }

    public Company(String email, String name, String description, String cvr, int numOfEmployees, int marketValue) {
        setEmail(email);
        this.name = name;
        this.description = description;
        this.cvr = cvr;
        this.numOfEmployees = numOfEmployees;
        this.marketValue = marketValue;
    }
    
    public Company(String email, String name, String description, String cvr, int numOfEmployees, int marketValue, List<Phone> phones, Address address) {
        setEmail(email);
        setPhones(phones);
        setAddress(address);
        this.name = name;
        this.description = description; 
        this.cvr = cvr;
        this.numOfEmployees = numOfEmployees;
        this.marketValue = marketValue;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCvr() {
        return cvr;
    }

    public void setCvr(String cvr) {
        this.cvr = cvr;
    }

    public int getNumOfEmployees() {
        return numOfEmployees;
    }

    public void setNumOfEmployees(int numOfEmployees) {
        this.numOfEmployees = numOfEmployees;
    }

    public int getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(int marketValue) {
        this.marketValue = marketValue;
    }
    
}

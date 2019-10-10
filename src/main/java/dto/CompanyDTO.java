/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Company;
import entities.Phone;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Annika
 */
public class CompanyDTO {
    private String name;
    private String description;
    private String cvr;
    private int numOfEmployees;
    private int markedValue;
    private String street;
    private String additionalInfo;
    private String city;
    private String zip;
    private List<PhoneDTO> phones;
    private int id;
    
    public CompanyDTO() {
    }
    
    public CompanyDTO(Company c) {
        this.name = c.getName();
        this.description = c.getDescription();
        this.cvr = c.getCvr();
        this.numOfEmployees = c.getNumOfEmployees();
        this.markedValue = c.getMarketValue();
        this.street = c.getAddress().getStreet();
        this.additionalInfo = c.getAddress().getAdditionalInfo();
        this.city = c.getAddress().getCityInfo().getCity();
        this.zip = c.getAddress().getCityInfo().getCity();
        List<PhoneDTO> dto = new ArrayList<>();
        for(Phone phone: c.getPhones()) {
            dto.add(new PhoneDTO(phone));
        }
        this.id = c.getId();
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

    public int getMarkedValue() {
        return markedValue;
    }

    public void setMarkedValue(int markedValue) {
        this.markedValue = markedValue;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public List<PhoneDTO> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDTO> phones) {
        this.phones = phones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

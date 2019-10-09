/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Phone;

/**
 *
 * @author Annika
 */
public class PhoneDTO {
    private String number;
    private String description;

    public PhoneDTO() {
    }
    
    
    
    public PhoneDTO(Phone p) {
        this.number = p.getNumber();
        this.description = p.getDescription();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String phone) {
        this.number = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

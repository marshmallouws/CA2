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
    private String phone;
    private String description;
    
    public PhoneDTO(Phone p) {
        this.phone = p.getNumber();
        this.description = p.getDescription();
    }
}

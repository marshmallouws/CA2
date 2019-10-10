package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Martin
 */
@MappedSuperclass
public abstract class InfoEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Phone> phones;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Address address;

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public Address getAddress() {
        return address;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    
    

    
}

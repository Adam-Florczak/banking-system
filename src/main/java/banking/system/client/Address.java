package banking.system.client;

import banking.system.common.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(mappedBy = "address",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
            orphanRemoval = true)
    private Client client;

    @NotNull
    private String country;

    @NotNull
    private String city;

    @NotNull
    private String zipCode;

    @NotNull
    private String street;

    @NotNull
    private String number;

    public Address() {
    }

    public Address(String country, String city, String zipCode, String street, String number) {
        this.country = country;
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}


package banking.system.client;

public class AddressCreateDTO {

    private String country;
    private String city;
    private String zipCode;
    private String street;
    private String number;

    public AddressCreateDTO() {
    }

    public AddressCreateDTO(Address address) {
        this.country = address.getCountry();
        this.city=address.getCity();
        this.zipCode=address.getZipCode();
        this.street=address.getStreet();
        this.number=address.getNumber();
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

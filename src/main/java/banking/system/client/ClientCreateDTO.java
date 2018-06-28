package banking.system.client;

public class ClientCreateDTO {
    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private Long addressId;

    public ClientCreateDTO() {
    }

    public ClientCreateDTO(Client client){
        this.email=client.getEmail();
        this.firstName=client.getFirstName();
        this.lastName=client.getLastName();
        this.password=client.getPassword();
        this.addressId=client.getAddress().getId();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

}
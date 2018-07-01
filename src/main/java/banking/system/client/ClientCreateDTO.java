package banking.system.client;

import java.util.Set;

public class ClientCreateDTO {
    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private Long addressId;

    private Set<Role> roles;

    public ClientCreateDTO() {
    }

    public ClientCreateDTO(Client client){
        this.email=client.getEmail();
        this.firstName=client.getFirstName();
        this.lastName=client.getLastName();
        this.password=client.getPassword();
        this.addressId=client.getAddress().getId();
        this.roles=client.getRoles();
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}

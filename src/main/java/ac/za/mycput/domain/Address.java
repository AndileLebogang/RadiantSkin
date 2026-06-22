package ac.za.mycput.domain;
import jakarta.persistence.*;
/*
* Name: Siphokazi Malingatshoni
* Student Number: 222868708
* Date:2026/06/21
*/

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long addressId;

    private String street;
    private String city;
    private String province;
    private String postalCode;
    private String country ;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;// link with customer class when available

    public Address() {
    }

    public Address(Builder builder){
        this.addressId = builder.addressId;
        this.street = builder.street;
        this.city = builder.city;
        this.province = builder.province;
        this.postalCode = builder.postalCode;
        this.country = builder.country;
        this.customer = builder.customer;
    }

    public Long getAddressId() {
        return addressId;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public Customer getCustomer() {
        return customer;
    }

    public static class Builder {
        private Long addressId;
        private String street;
        private String city;
        private String province;
        private String postalCode;
        private String country ;
        private Customer customer;

        public Builder setAddressId(Long addressId){
            this.addressId = addressId;
            return this;
        }
        public Builder setStreet(String street){
            this.street = street;
            return this;
        }
        public Builder setCity(String city){
            this.city = city;
            return this;
        }
        public Builder setProvince(String province){
            this.province =province;
            return this;
        }
        public Builder setPostalCode(String postalCode){
            this.postalCode = postalCode;
            return this;
        }
        public Builder setCountry(String country){
            this.country = country;
            return this;
        }
        public Builder setCustomer(Customer customer){
            this.customer = customer;
            return this;
        }

        public Builder copy(Address address){
        this.addressId = address.addressId;
        this.street = address.street;
        this.city = address.city;
        this.province = address.postalCode;
        this.country = address.country;
        this.customer = address.customer;
        return this;

        }
        public Address build(){
            return new Address(this);
        }
    }

}

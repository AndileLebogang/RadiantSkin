/* Admin.java

     Admin POJO class

     Author: Nonhlanhla Hazel Mahlangu (222687290)

     Date: 21 June 2026 */

package ac.za.mycput.domain;
import jakarta.persistence.Entity;

@Entity
public class Admin extends User{

    private String employeeNumber;

    public Admin(){
        super();
    }

    private Admin(Builder builder){
        this.employeeNumber = builder.employeeNumber;
        this.userId = builder.userId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.password = builder.password;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }


    @Override
    public String toString() {
        return "Admin [employeeNumber=" + employeeNumber + ", userId=" + userId + ", firstName=" + firstName
                + ", lastName=" + lastName + ", email=" + email + ", password=" + password + "]";
    }

    public static class Builder{
        private String employeeNumber;
        private Long userId;
        private String firstName;
        private String lastName;
        private String email;
        private String password;

        public Builder setEmployeeNumber(String employeeNumber) {
            this.employeeNumber = employeeNumber;
            return this;
        }
        public Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }
        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }
        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder copy(Admin admin){
            this.userId = admin.userId;
            this.employeeNumber = admin.employeeNumber;
            this.firstName = admin.firstName;
            this.lastName = admin.lastName;
            this.email = admin.email;
            this.password = admin.password;
            return this;
        }

        public Admin build(){
            return new Admin(this);
        }

        
    }
    
}

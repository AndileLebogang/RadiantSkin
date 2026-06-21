/* User.java

     User POJO class

     Author: Nonhlanhla Hazel Mahlangu (222687290)

     Date: 21 June 2026 */


package ac.za.mycput.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    protected Long userId;

    protected String firstName;

    protected String lastName;

    protected String email;

    protected String password;

    public User(){}

    public User(Long userId, String firstName, String lastName, String email, String password) {

        this.userId = userId;

        this.firstName = firstName;

        this.lastName = lastName;

        this.email = email;

        this.password = password;

    }

    public Long getUserId() {

        return userId;

    }

    public void setUserId(Long userId) {

        this.userId = userId;

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

    public String getEmail() {

        return email;

    }

    public void setEmail(String email) {

        this.email = email;

    }

    public String getPassword() {

        return password;

    }

    public void setPassword(String password) {

        this.password = password;

    }


    @Override

    public String toString() {

        return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email

                + ", password=" + password + "]";

    }

} 


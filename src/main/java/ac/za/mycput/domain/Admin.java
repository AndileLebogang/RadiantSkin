package ac.za.mycput.domain;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User {

    private String employeeNumber;

    protected Admin() {

    }

    public Admin(Long userId, String firstName, String lastName, String email,
                 String password, String employeeNumber) {
        super(userId, firstName, lastName, email, password);
        this.employeeNumber = employeeNumber;
    }

    public String getEmployeeNumber() { return employeeNumber; }

    @Override
    public String toString() {
        return "Admin{" +
                "employeeNumber='" + employeeNumber + '\'' +
                "} " + super.toString();
    }
}

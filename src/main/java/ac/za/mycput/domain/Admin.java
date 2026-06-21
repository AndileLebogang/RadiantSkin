package ac.za.mycput.domain;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User {

    private String employeeNumber;

    protected Admin() {
    }

    private Admin(Builder builder) {
        super(builder);
        this.employeeNumber = builder.employeeNumber;
    }

    public String getEmployeeNumber() { return employeeNumber; }

    @Override
    public String toString() {
        return "Admin{" + super.toString() +
                ", employeeNumber='" + employeeNumber + '\'' +
                '}';
    }

    public static class Builder extends User.Builder<Builder> {
        private String employeeNumber;

        public Builder setEmployeeNumber(String employeeNumber) {
            this.employeeNumber = employeeNumber;
            return this;
        }

        @Override
        public Builder copy(User user) {
            super.copy(user);
            if (user instanceof Admin) {
                Admin admin = (Admin) user;
                this.employeeNumber = admin.employeeNumber;
            }
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        public Admin build() {
            return new Admin(this);
        }
    }
}

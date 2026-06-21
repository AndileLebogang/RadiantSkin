package ac.za.mycput.domain;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {

    @Id
    protected Long userId;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;

    protected User() {

    }

    protected User(Builder<?> builder) {
        this.userId = builder.userId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.password = builder.password;
    }

    public Long getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    @Override
    public String toString() {
        return "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'';
    }

    public static abstract class Builder<T extends Builder<T>> {
        private Long userId;
        private String firstName;
        private String lastName;
        private String email;
        private String password;

        public T setUserId(Long userId) {
            this.userId = userId;
            return self();
        }
        public T setFirstName(String firstName) {
            this.firstName = firstName;
            return self();
        }
        public T setLastName(String lastName) {
            this.lastName = lastName;
            return self();
        }
        public T setEmail(String email) {
            this.email = email;
            return self();
        }
        public T setPassword(String password) {
            this.password = password;
            return self();
        }

        public T copy(User user) {
            this.userId = user.userId;
            this.firstName = user.firstName;
            this.lastName = user.lastName;
            this.email = user.email;
            this.password = user.password;
            return self();
        }

        protected abstract T self();
    }
}

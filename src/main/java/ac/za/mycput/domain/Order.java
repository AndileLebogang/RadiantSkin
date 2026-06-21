/* Order.java

        Order POJO class

        Author: La-eeq Lewis (240696255)

        Date: 21 June 2026 */

package ac.za.mycput.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private BigDecimal totalAmount;

    @ManyToOne
    private Customer customer; // Link with the customer class when available

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany
    private List<Payment> payments = new ArrayList<>();

    protected Order() {
    }

    private Order(Builder builder) {
        this.orderId = builder.orderId;
        this.orderDate = builder.orderDate;
        this.status = builder.status;
        this.totalAmount = builder.totalAmount;
        this.customer = builder.customer;
        this.orderItems = builder.orderItems;
        this.payments = builder.payments;
    }

    public Long getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderDate=" + orderDate +
                ", status=" + status +
                ", totalAmount=" + totalAmount +
                ", customer=" + customer +
                ", orderItems=" + orderItems +
                ", payments=" + payments +
                '}';
    }

    public static class Builder {
        private Long orderId;
        private LocalDateTime orderDate;
        private OrderStatus status;
        private BigDecimal totalAmount;
        private Customer customer;
        private List<OrderItem> orderItems = new ArrayList<>();
        private List<Payment> payments = new ArrayList<>();

        public Builder setOrderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder setOrderDate(LocalDateTime orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public Builder setStatus(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder setCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder setOrderItems(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
            return this;
        }

        public Builder setPayments(List<Payment> payments) {
            this.payments = payments;
            return this;
        }

        public Builder copy(Order order) {
            this.orderId = order.orderId;
            this.orderDate = order.orderDate;
            this.status = order.status;
            this.totalAmount = order.totalAmount;
            this.customer = order.customer;
            this.orderItems = order.orderItems;
            this.payments = order.payments;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
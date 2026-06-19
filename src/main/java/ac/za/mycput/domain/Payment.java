package ac.za.mycput.domain;


import jakarta.persistence.*;

/*
Makgato Tebogo 230116086
*/
@Entity
public class Payment {
    @Id
    private Long paymentId;
    private double amount;
    private String transactionReference;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    protected Payment() {
    }

    public Payment(Builder builder) {
        this.paymentId = builder.paymentId;
        this.amount = builder.amount;
        this. transactionReference = builder.transactionReference;
        this.paymentStatus = builder.paymentStatus;
        this.paymentMethod = builder.paymentMethod;
    }

    public Long getPaymentID() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", amount=" + amount +
                ", transactionReference='" + transactionReference + '\'' +
                ", paymentStatus=" + paymentStatus +
                ", paymentMethod=" + paymentMethod +
                '}';
    }

    public static class Builder{
        private Long paymentId;
        private double amount;
        private String transactionReference;
        private PaymentStatus paymentStatus;
        private PaymentMethod paymentMethod;

        public Builder setPaymentId(Long paymentId){
            this.paymentId = paymentId;
            return this;
        }
        public Builder setAmount(double amount){
            this.amount = amount;
            return this;
        }
        public Builder setTransactionReference(String transactionReference){
            this.transactionReference = transactionReference;
            return this;
        }
        public Builder setPaymentStatus(PaymentStatus paymentStatus){
            this.paymentStatus = paymentStatus;
            return this;
        }
        public Builder setPaymentMethod(PaymentMethod paymentMethod){
            this.paymentMethod = paymentMethod;
            return this;
        }
        public Builder copy(Payment payment){
            this.paymentId = payment.paymentId;
            this.amount = payment.amount;
            this. transactionReference = payment.transactionReference;
            this.paymentStatus = payment.paymentStatus;
            this.paymentMethod = payment.paymentMethod;
            return this;
        }
        public Payment build(){
            return new Payment(this);
        }
    }
}

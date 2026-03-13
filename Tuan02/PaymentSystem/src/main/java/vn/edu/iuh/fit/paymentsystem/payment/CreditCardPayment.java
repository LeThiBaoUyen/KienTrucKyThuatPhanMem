package vn.edu.iuh.fit.paymentsystem.payment;

public class CreditCardPayment implements payment.Payment {
    @Override
    public void pay(double amount) {
        System.out.println("Thanh toán bằng Credit Card: " + amount);
    }
}

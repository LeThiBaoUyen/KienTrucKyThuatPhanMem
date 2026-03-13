package vn.edu.iuh.fit.paymentsystem.payment;

public class MomoPayment implements payment.Payment {
    @Override
    public void pay(double amount) {
        System.out.println("Thanh toán bằng Momo: " + amount);
    }
}

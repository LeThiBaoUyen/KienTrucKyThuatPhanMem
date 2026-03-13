package vn.edu.iuh.fit.paymentsystem.payment;

public class PaymentManager {
    private static PaymentManager instance;

    private PaymentManager() {
    }

    public static PaymentManager getInstance() {

        if (instance == null) {
            instance = new PaymentManager();
        }

        return instance;
    }

    public void processPayment(String type, double amount) {

        payment.Payment payment = PaymentFactory.createPayment(type);
        payment.pay(amount);
    }
}

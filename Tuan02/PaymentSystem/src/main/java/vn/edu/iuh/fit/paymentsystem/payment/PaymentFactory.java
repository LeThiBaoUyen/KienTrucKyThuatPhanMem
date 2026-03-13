package vn.edu.iuh.fit.paymentsystem.payment;

public class PaymentFactory {

    public static payment.Payment createPayment(String type) {

        if (type.equalsIgnoreCase("credit")) {
            return new CreditCardPayment();
        }

        if (type.equalsIgnoreCase("paypal")) {
            return new PaypalPayment();
        }

        if (type.equalsIgnoreCase("momo")) {
            return new MomoPayment();
        }

        throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ");
    }
}

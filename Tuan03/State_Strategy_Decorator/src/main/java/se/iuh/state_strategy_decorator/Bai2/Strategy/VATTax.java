package se.iuh.state_strategy_decorator.Bai2.Strategy;

public class VATTax implements TaxStrategy {

    public double calculate(double price) {

        return price * 0.1;
    }
}
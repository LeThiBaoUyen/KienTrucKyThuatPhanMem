package se.iuh.state_strategy_decorator.Bai3.Strategy;

public class Payment {

    PaymentStrategy strategy;

    public void setStrategy(
            PaymentStrategy strategy){

        this.strategy=strategy;
    }

    public void pay(int amount){

        strategy.pay(amount);
    }
}

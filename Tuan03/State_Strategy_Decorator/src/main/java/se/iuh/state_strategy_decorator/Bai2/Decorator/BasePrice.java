package se.iuh.state_strategy_decorator.Bai2.Decorator;

public class BasePrice implements Price {

    double price;

    public BasePrice(double price){

        this.price=price;
    }

    public double getPrice(){

        return price;
    }
}
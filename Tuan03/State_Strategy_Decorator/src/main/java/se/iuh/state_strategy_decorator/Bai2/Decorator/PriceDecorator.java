package se.iuh.state_strategy_decorator.Bai2.Decorator;

public abstract class PriceDecorator implements Price{

    protected Price price;

    PriceDecorator(Price price){

        this.price=price;
    }

    public double getPrice(){

        return price.getPrice();
    }
}
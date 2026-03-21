package se.iuh.state_strategy_decorator.Bai2.Decorator;

public class InsuranceDecorator
        extends PriceDecorator{

    public InsuranceDecorator(Price price){

        super(price);
    }

    public double getPrice(){

        return super.getPrice()+50;
    }
}
package se.iuh.state_strategy_decorator.Bai2;

import se.iuh.state_strategy_decorator.Bai2.Decorator.BasePrice;
import se.iuh.state_strategy_decorator.Bai2.Decorator.InsuranceDecorator;
import se.iuh.state_strategy_decorator.Bai2.Decorator.Price;
import se.iuh.state_strategy_decorator.Bai2.Strategy.Product;
import se.iuh.state_strategy_decorator.Bai2.Strategy.VATTax;

public class Main {

    public static void main(String[] args) {

        Product p = new Product();

        p.setTax(new VATTax());

        p.calculate(1000);

        Price price =
                new InsuranceDecorator(
                        new BasePrice(1000)
                );

        System.out.println(price.getPrice());
    }
}
package se.iuh.state_strategy_decorator.Bai2.Strategy;

public class Product {

    private TaxStrategy taxStrategy;

    public void setTax(TaxStrategy taxStrategy) {

        this.taxStrategy = taxStrategy;
    }

    public void calculate(double price) {

        System.out.println(
                "Tax = " +
                        taxStrategy.calculate(price)
        );
    }
}
package se.iuh.state_strategy_decorator.Bai1.Decorator;

public abstract class OrderDecorator implements OrderService {

    protected OrderService service;

    public OrderDecorator(OrderService service) {

        this.service = service;
    }

    public void process() {

        service.process();
    }
}

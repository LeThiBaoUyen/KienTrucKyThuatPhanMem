package se.iuh.state_strategy_decorator.Bai1.Decorator;

public class EmailDecorator extends OrderDecorator {

    public EmailDecorator(OrderService service) {

        super(service);
    }

    public void process() {

        super.process();

        System.out.println("Send email");
    }
}

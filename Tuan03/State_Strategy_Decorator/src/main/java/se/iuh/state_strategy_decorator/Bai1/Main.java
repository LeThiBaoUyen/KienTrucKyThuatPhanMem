package se.iuh.state_strategy_decorator.Bai1;

import se.iuh.state_strategy_decorator.Bai1.Decorator.BasicOrderService;
import se.iuh.state_strategy_decorator.Bai1.Decorator.EmailDecorator;
import se.iuh.state_strategy_decorator.Bai1.Decorator.OrderService;
import se.iuh.state_strategy_decorator.Bai1.State.NewOrderState;
import se.iuh.state_strategy_decorator.Bai1.State.Order;

public class Main {

    public static void main(String[] args) {

        Order order = new Order();

        order.setState(new NewOrderState());

        order.process();

        order.process();

        OrderService service =
                new EmailDecorator(
                        new BasicOrderService()
                );

        service.process();
    }
}

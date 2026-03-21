package se.iuh.state_strategy_decorator.Bai1.State;

public class ProcessingState implements OrderState {

    public void handle(Order order) {
        System.out.println("Đóng gói và vận chuyển");

        order.setState(new DeliveredState());
    }
}

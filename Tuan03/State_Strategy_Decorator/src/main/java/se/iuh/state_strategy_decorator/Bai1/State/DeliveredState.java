package se.iuh.state_strategy_decorator.Bai1.State;

public class DeliveredState implements OrderState {

    public void handle(Order order) {
        System.out.println("Đơn hàng đã giao");
    }
}

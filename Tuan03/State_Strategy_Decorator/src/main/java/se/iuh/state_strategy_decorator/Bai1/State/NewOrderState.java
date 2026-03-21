package se.iuh.state_strategy_decorator.Bai1.State;

public class NewOrderState implements OrderState {

    public void handle(Order order) {
        System.out.println("Kiểm tra thông tin đơn hàng");

        order.setState(new ProcessingState());
    }
}

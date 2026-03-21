package se.iuh.state_strategy_decorator.Bai1.State;

public class CancelState implements OrderState {

    public void handle(Order order) {
        System.out.println("Hủy đơn và hoàn tiền");
    }
}

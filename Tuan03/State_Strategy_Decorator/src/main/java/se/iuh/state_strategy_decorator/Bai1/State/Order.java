package se.iuh.state_strategy_decorator.Bai1.State;

public class Order {
    private OrderState state;

    public void setState(OrderState state) {
        this.state = state;
    }

    public void process() {
        state.handle(this);
    }
}
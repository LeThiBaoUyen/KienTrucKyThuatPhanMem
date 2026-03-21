package se.iuh.tuan03.Bai2.Decorator;

public abstract class BorrowDecorator implements Borrow{

    Borrow borrow;

    BorrowDecorator(Borrow b){

        borrow=b;
    }

    public void borrow(){

        borrow.borrow();
    }
}
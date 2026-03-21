package se.iuh.tuan03.Bai2.Decorator;

public class ExtendBorrow extends BorrowDecorator{

    public ExtendBorrow(Borrow b){

        super(b);
    }

    public void borrow(){

        super.borrow();

        System.out.println(

                "Extend time"

        );
    }
}
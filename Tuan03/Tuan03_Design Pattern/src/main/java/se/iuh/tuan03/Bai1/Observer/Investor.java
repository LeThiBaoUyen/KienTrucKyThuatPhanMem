package se.iuh.tuan03.Bai1.Observer;

public class Investor implements Observer{

    String name;

    Investor(String name){

        this.name=name;
    }

    public void update(double price){

        System.out.println(

                name+" new price "+price

        );
    }
}
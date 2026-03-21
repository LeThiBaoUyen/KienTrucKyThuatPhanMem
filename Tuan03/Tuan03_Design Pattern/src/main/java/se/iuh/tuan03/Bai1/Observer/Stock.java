package se.iuh.tuan03.Bai1.Observer;

import java.util.*;

public class Stock implements Subject{

    List<Observer> list =
            new ArrayList<>();

    double price;

    public void add(Observer o){

        list.add(o);
    }

    public void notifyObservers(){

        for(Observer o:list){

            o.update(price);
        }
    }

    void setPrice(double price){

        this.price=price;

        notifyObservers();
    }
}
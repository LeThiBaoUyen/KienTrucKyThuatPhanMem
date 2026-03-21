package se.iuh.tuan03.Bai1.Observer;

public class Main{

    public static void main(String[] args){

        Stock s = new Stock();

        Investor a = new Investor("A");

        Investor b = new Investor("B");

        s.add(a);

        s.add(b);

        s.setPrice(200);

    }
}
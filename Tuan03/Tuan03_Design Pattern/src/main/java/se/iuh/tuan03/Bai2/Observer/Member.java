package se.iuh.tuan03.Bai2.Observer;

public class Member implements User{

    public void update(String msg){

        System.out.println(msg);
    }
}
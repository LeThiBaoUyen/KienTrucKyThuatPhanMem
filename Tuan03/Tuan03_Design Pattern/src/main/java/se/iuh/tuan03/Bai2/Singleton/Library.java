package se.iuh.tuan03.Bai2.Singleton;

public class Library{

    private static Library instance;

    private Library(){}

    public static Library getInstance(){

        if(instance==null)

            instance=new Library();

        return instance;
    }
}
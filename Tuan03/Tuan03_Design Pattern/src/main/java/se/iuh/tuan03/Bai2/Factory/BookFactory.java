package se.iuh.tuan03.Bai2.Factory;

public class BookFactory{

    public static Book create(String type){

        if(type.equals("paper"))

            return new PaperBook();

        if(type.equals("ebook"))

            return new Ebook();

        return new AudioBook();
    }
}
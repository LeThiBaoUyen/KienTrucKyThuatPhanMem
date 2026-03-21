package se.iuh.tuan03.Bai1.Adapter;

public class Main{

    public static void main(String[] args){

        XMLData xml =
                new XMLData();

        JSONData adapter =
                new Adapter(xml);

        System.out.println(

                adapter.getJSON()

        );
    }
}
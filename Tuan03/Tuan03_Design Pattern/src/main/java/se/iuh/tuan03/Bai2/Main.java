package se.iuh.tuan03.Bai2;


import se.iuh.tuan03.Bai2.Decorator.BasicBorrow;
import se.iuh.tuan03.Bai2.Decorator.Borrow;
import se.iuh.tuan03.Bai2.Decorator.ExtendBorrow;
import se.iuh.tuan03.Bai2.Factory.Book;
import se.iuh.tuan03.Bai2.Factory.BookFactory;
import se.iuh.tuan03.Bai2.Observer.Member;
import se.iuh.tuan03.Bai2.Singleton.Library;
import se.iuh.tuan03.Bai2.Strategy.SearchByTitle;
import se.iuh.tuan03.Bai2.Strategy.SearchStrategy;

public class Main {

    public static void main(String[] args){

        // Singleton
        Library library =
                Library.getInstance();

        System.out.println(
                "Library created"
        );


        // Factory
        Book book1 =
                BookFactory.create("paper");

        Book book2 =
                BookFactory.create("ebook");

        System.out.println(
                "Books created"
        );


        // Strategy
        SearchStrategy search =
                new SearchByTitle();

        search.search();


        // Observer
        Member member =
                new Member();

        member.update(
                "New book added"
        );


        // Decorator
        Borrow borrow =
                new ExtendBorrow(
                        new BasicBorrow()
                );

        borrow.borrow();

    }
}
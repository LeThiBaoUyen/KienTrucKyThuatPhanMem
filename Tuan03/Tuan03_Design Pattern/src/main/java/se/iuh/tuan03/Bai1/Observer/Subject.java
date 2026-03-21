package se.iuh.tuan03.Bai1.Observer;

public interface Subject {

    void add(Observer o);

    void notifyObservers();
}
package Model;

import java.io.Serializable;

public class Order implements Serializable {
    private String orderId;
    private Book book;
    private int quantity;
    private double total;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }


    public Order() {

    }

    public Order(String orderId, Book book, int quantity, double total) {
        this.orderId = orderId;
        this.book = book;
        this.quantity = quantity;
        this.total = total;
    }
}

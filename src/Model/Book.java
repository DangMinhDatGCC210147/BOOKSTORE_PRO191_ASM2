package Model;

import java.io.Serializable;

public class Book implements Serializable {
    private String bookId;
    private String bookTitle;
    private double price;
    private int quantity;
    private boolean bestSellerStatus;
    private Category category;

    public Book(String bookId, String bookTitle, double price, int quantity, boolean bestSellerStatus, Category category) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.price = price;
        this.quantity = quantity;
        this.bestSellerStatus = bestSellerStatus;
        this.category = category;
    }

    public Book() {

    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isBestSellerStatus() {
        return bestSellerStatus;
    }

    public void setBestSellerStatus(boolean bestSellerStatus) {
        this.bestSellerStatus = bestSellerStatus;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

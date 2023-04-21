package Controller;

import Model.Book;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class BookController {

    DefaultTableModel tableBookModel;
    List<Book> bookList;

    public DefaultTableModel getTableBookModel() {
        return tableBookModel;
    }

    public void setTableBookModel(DefaultTableModel tableBookModel) {
        this.tableBookModel = tableBookModel;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public BookController(DefaultTableModel tableBookModel, List<Book> bookList) {
        this.tableBookModel = tableBookModel;
        this.bookList = bookList;
        if (this.bookList == null || this.bookList.size() == 0) {
            this.bookList = new ArrayList<>();
        }
    }
    public void fillToTable() {
        tableBookModel.setRowCount(0);
        String status = null;

        for (Book b : bookList) {
            if (b.isBestSellerStatus()){
                status = "Yes";
            }else{
                status = "No";
            }
            Object[] row = new Object[] {
                    b.getBookId(), b.getBookTitle(), b.getPrice(), b.getQuantity(), status, b.getCategory().getCategoryName()
            };
            tableBookModel.addRow(row);
        }
    }
    public void insert(Book book){
        bookList.add(book);
    }
    public void remove(String id){
        for (Book bo: bookList) {
            if (id.equals(bo.getBookId())){
                bookList.remove(bo);
                break;
            }
        }
    }
    public void removeAll(List<Book> bookList){
        bookList.removeAll(bookList);
    }
    public BookController() {

    }
}

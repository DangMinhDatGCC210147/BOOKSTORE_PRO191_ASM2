package View;

import Controller.BookController;
import Lib.XFile;
import Model.Book;
import Model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BookGUI extends JFrame {
    private JTextField bookId_txt;
    private JTextField bookPrice_txt;
    private JTextField bookTitle_txt;
    private JSpinner quantityBook_spin;
    private JRadioButton yesBook_rd;
    private JRadioButton noBook_rd;
    private JTable book_tb;
    private JButton addBook_btn;
    private JButton updateBook_btn;
    private JButton deleteBook_btn;
    private JPanel bookPanel;
    private JButton exitBook_btn;
    private JComboBox categoryBook_cb;
    private JButton clear_btn;
    private JButton deleteAll_btn;
    private JButton searchButton;
    private JTextField search_txt;
    int row = -1;
    List<Category> cateList;
    DefaultTableModel tableBookModel;
    BookController bookController;
    String filePath = "src\\File\\Book.dat";
    String getFilePathCate = "src\\File\\category.dat";

    public BookGUI() {
        this.setTitle("Manage Book");
        this.setResizable(false);
        this.setContentPane(bookPanel);
        this.setMinimumSize(new Dimension(850, 530));
        this.setLocationRelativeTo(bookPanel);
        this.setVisible(true);
        this.book_tb.setRowHeight(30);

        this.bookCategory();
        gotCategory();

        book_tb.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[] {
                        "Book ID", "Book Title", "Price", "Quantity", "Bestseller", "Category",
                }
        )
             {
                 public boolean isCellEditable(int row, int column) {
                     return false;
                 }
             }
        );


        bookController = new BookController(
                (DefaultTableModel) book_tb.getModel(),
                (List<Book>) XFile.readObject(filePath)
        );

        bookController.fillToTable();

        addBook_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                checkInput();
            }
        });
        exitBook_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int choice = JOptionPane.showConfirmDialog(bookPanel, "Do you want to exit?","Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    MainPageGUI mainpage = new MainPageGUI();
                    mainpage.setVisible(true);
                    dispose();
                }
            }
        });
        deleteBook_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int  getSelectedRowForDeletion = book_tb.getSelectedRow();

                int answer = JOptionPane.showConfirmDialog(bookPanel, "Do you want to remove this data?", "Remove",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (answer == JOptionPane.YES_OPTION){
                    if(getSelectedRowForDeletion >= 0){
                        bookController.remove(bookId_txt.getText());
                        bookController.fillToTable();
                        XFile.writeObject(filePath,  bookController.getBookList());
                        JOptionPane.showMessageDialog(null, "Row is deleted!");
                    }else{
                        JOptionPane.showMessageDialog(null, "Unable to delete, please choose one row");
                    }
                }
            }
        });
        clear_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearBook();
            }
        });
        updateBook_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateBook();
            }
        });

        //fill in txt box and so forth
        book_tb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                row = book_tb.getSelectedRow();

                TableModel bookModel = book_tb.getModel();
                String bookId = (String) bookModel.getValueAt(row, 0);
                bookId_txt.setText(bookId);

                String bookTitle = (String) bookModel.getValueAt(row, 1);
                bookTitle_txt.setText(bookTitle);

                double bookPrice = (double) bookModel.getValueAt(row, 2);
                bookPrice_txt.setText(String.valueOf(bookPrice));

                int quantityBook = (int) bookModel.getValueAt(row, 3);
                quantityBook_spin.setValue(quantityBook);

                String bestSeller = (String) bookModel.getValueAt(row, 4);
                if (bestSeller.equals("Yes")){
                    yesBook_rd.setSelected(true);
                }else{
                    noBook_rd.setSelected(true);
                }
                String comboCate = bookModel.getValueAt(row, 5).toString();
                for (int a = 0; a < categoryBook_cb.getItemCount(); a++) {
                    if (categoryBook_cb.getItemAt(a).equals(comboCate)) {
                        categoryBook_cb.setSelectedItem(comboCate);
                        break;
                    }
                }
            }
        });
        deleteAll_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int answer = JOptionPane.showConfirmDialog(bookPanel, "Do you want to remove all data in this table?", "Remove",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (answer == JOptionPane.YES_OPTION){
                    bookController.removeAll(bookController.getBookList());
                    bookController.fillToTable();
                    XFile.writeObject(filePath,  bookController.getBookList());
                    JOptionPane.showMessageDialog(null, "All row have been deleted!");
                }
            }
        });
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String query = search_txt.getText();
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(book_tb.getModel());
                sorter.setRowFilter(RowFilter.regexFilter(query));
                book_tb.setRowSorter(sorter);
            }
        });
    }

    private void clearBook() {
        bookId_txt.setText("");
        bookTitle_txt.setText("");
        bookPrice_txt.setText("");
        categoryBook_cb.setSelectedIndex(0);
        quantityBook_spin.setValue(0);
        yesBook_rd.isSelected();
        search_txt.setText("");
        row = -1;
    }

    private void checkID() {
        boolean flag = false;
        for (Book b :  bookController.getBookList()) {
            if (b.getBookId().equals(bookId_txt.getText())) {
                flag = true;
                break;
            }
        }
        if (flag) {
            JOptionPane.showMessageDialog(null, "Book ID is not allowed duplicate");
        }else {
            createBook();
        }
    }
    private void updateBook() {
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Please choose the row you want to edit!!");
        }else{
            editBook();
            bookController.fillToTable();
            XFile.writeObject(filePath, bookController.getBookList());
        }

    }

    private void editBook() {
        for (Book b :  bookController.getBookList()) {
            if (b.getBookId().equals(bookId_txt.getText())){
                b.setBookTitle(bookTitle_txt.getText());
                b.setPrice(Double.parseDouble(bookPrice_txt.getText()));
                b.setQuantity((Integer) quantityBook_spin.getValue());
                b.setBestSellerStatus(yesBook_rd.isSelected());
                b.getCategory().setCategoryName(categoryBook_cb.getSelectedItem().toString());
                break;
            }
        }
    }
    //Show data in comboBox
    private void bookCategory() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        ArrayList<String> categoryBook = new ArrayList<>();
        ArrayList<Category> list = (ArrayList<Category>) gotCategory();
        if (list != null) {
            for (Category t : list) {
                categoryBook.add(t.getCategoryName());
            }
            for (String book : categoryBook) {
                model.addElement(book);
            }
            categoryBook_cb.setModel(model);
        }
    }
    private List<Category> gotCategory() {
        cateList = (List<Category>) XFile.readObject(getFilePathCate);
            return cateList;
    }

    private void createBook() {
        saveFromForm();
        bookController.fillToTable();
        XFile.writeObject(filePath, bookController.getBookList());
    }



    private void saveFromForm() {
        Category category = null;
        for (Category c: cateList) {
            if (c.getCategoryName().equals(categoryBook_cb.getSelectedItem().toString())){
                category = c;
            }
        }
        Book book = new Book(
                bookId_txt.getText(),
                bookTitle_txt.getText(),
                Double.parseDouble(bookPrice_txt.getText()),
                (Integer) quantityBook_spin.getValue(),
                yesBook_rd.isSelected(),
                category
        );
        bookController.insert(book);
    }
    //Check ID with BOK####
    private static boolean checkSpecificID(String input){
        // Compile the regular expression pattern
        Pattern pattern = Pattern.compile("BOK\\d{4}");
        // Create a Matcher object to match the pattern against the input string
        Matcher matcher = pattern.matcher(input);
        // Use the matches() method to check if the input string matches the pattern
        return matcher.matches();
    }

    private void checkInput(){
        StringBuffer sb = new StringBuffer();
        //Check the BookID that it is a specific id
        String input = bookId_txt.getText();
        boolean inputValid = checkSpecificID(input); //Use to check specific category ID
        //Check the price that it is a number
        boolean temp = true;
        try{
            double checkNum1 = Double.parseDouble(bookPrice_txt.getText());
        }
        catch(Exception ex){
            temp = false;
        }
        //=================================================================

        if (bookId_txt.getText().equals("")){
            sb.append("\nBook ID is not allowed empty!!!");
        }
        if(bookTitle_txt.getText().equals("")){
            sb.append("\nBook Title is not allowed empty!!!");
        }
        if(bookPrice_txt.getText().equals("")){
            sb.append("\nBook Price is not allowed empty!!!");
        }
        if(quantityBook_spin.getValue().equals(0)){
            sb.append("\nBook Quantity is not equal zero!!!");
        }
        if ((Integer)quantityBook_spin.getValue() <= 0){
            sb.append("\nNumber of books must be a positive number!!!");
        }
        if (!inputValid){
            sb.append("\nPlease enter Book ID with BOK####!!!");
        }
        if (!temp){
            sb.append("\nPlease enter positive number in Price box and Quantity box!!");
        }
        if (sb.length() > 0){
            JOptionPane.showMessageDialog(bookPanel, sb.toString(), "Invalidation", JOptionPane.ERROR_MESSAGE);
        }else {
            checkID();
            clearBook();
        }
    }
}

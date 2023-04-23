package View;

import Controller.OrderController;
import Lib.XFile;
import Model.Book;
import Model.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderGUI extends JFrame {
    private JTextField orderId_txt;
    private JComboBox bookTitleOrder_cb;
    private JSpinner qtyOrder_snip;
    private JButton insertButton;
    private JButton eraseButton;
    private JButton clearButton;
    private JButton eraseAllButton;
    private JButton exitButton;
    private JTable order_tb;
    private JPanel orderPanel;
    private JTextField total_txt;
    OrderController orderController;
    List<Book> bookList;
    DefaultTableModel tableOrderModel;
    String filePathOrder = "src\\File\\order.dat";
    String filePathBook = "src\\File\\book.dat";
    int row = -1;


    public OrderGUI(){
        setTitle("Manage Order");
        this.setResizable(false);
        this.setContentPane(orderPanel);
        setMinimumSize(new Dimension(1000, 350));
        setLocationRelativeTo(orderPanel);
        setVisible(true);
        order_tb.setRowHeight(30);


        //show bookName data into comboBox
        this.bookTitle();
        gotBookTitle();


        order_tb.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "Order ID", "Book Title", "Quantity", "Price", "Total"
                }
        )
                  {
                      public boolean isCellEditable(int row, int column) {
                          return false;
                      }
                  }
        );

        orderController = new OrderController(
                (DefaultTableModel) order_tb.getModel(),
                (List<Order>) XFile.readObject(filePathOrder)
        );

        orderController.fillToTable();

        insertButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (insertButton.getText().equals("Checkout") && (int) qtyOrder_snip.getValue() != 0) {
                    double sum = 0;
                    for (Book bk: bookList) {
                        if (bk.getBookTitle().equals(bookTitleOrder_cb.getSelectedItem())) {
                            sum = (int) qtyOrder_snip.getValue() * bk.getPrice();
                            break;
                        }
                    }
                    if (sum <= 0){
                        JOptionPane.showMessageDialog(null, "Please enter the number of book is a positive number!!");
                    }else {
                        total_txt.setText(String.valueOf(sum));
                        insertButton.setText("Insert");
                    }
                }else{
                    checkInput();
            }
            }
        });

        clearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearName();
            }
        });

        //Display in txt, cb and snip
        order_tb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                row = order_tb.getSelectedRow();
                TableModel orderModel = order_tb.getModel();

                //Fill in txt
                String orderId = (String) orderModel.getValueAt(row, 0);
                orderId_txt.setText(orderId);

                //fill in comboBox
                String comboCate = orderModel.getValueAt(row, 1).toString();
                for (int a = 0; a < bookTitleOrder_cb.getItemCount(); a++) {
                    if (bookTitleOrder_cb.getItemAt(a).equals(comboCate)) {
                        bookTitleOrder_cb.setSelectedItem(comboCate);
                        break;
                    }
                }
                //fill in snipper
                int quantityBook = (int) orderModel.getValueAt(row, 2);
                qtyOrder_snip.setValue(quantityBook);

                //fill in total
                Double total = (Double) orderModel.getValueAt(row, 4);
                total_txt.setText(String.valueOf(total));
            }
        });
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int choice = JOptionPane.showConfirmDialog(orderPanel, "Do you want to exit?","Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    MainPageGUI mainpage = new MainPageGUI();
                    mainpage.setVisible(true);
                    dispose();
                }
            }
        });
        eraseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int  getSelectedRowForDeletion = order_tb.getSelectedRow();

                int answer = JOptionPane.showConfirmDialog(orderPanel, "Do you want to remove this data?", "Remove",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (answer == JOptionPane.YES_OPTION){
                    if(getSelectedRowForDeletion >= 0){
                        orderController.remove(orderId_txt.getText());
                        orderController.fillToTable();
                        XFile.writeObject(filePathOrder, orderController.getOrderList());
                        JOptionPane.showMessageDialog(null, "Row is deleted!");
                    }else{
                        JOptionPane.showMessageDialog(null, "Unable to delete please choose one row");
                    }
                }
            }
        });
        eraseAllButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int answer = JOptionPane.showConfirmDialog(orderPanel, "Do you want to remove all data in this table?", "Remove",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (answer == JOptionPane.YES_OPTION){
                    orderController.removeAll(orderController.getOrderList());
                    orderController.fillToTable();
                    XFile.writeObject(filePathOrder, orderController.getOrderList());
                    JOptionPane.showMessageDialog(null, "All row have been deleted!");
                }
            }
        });
//        renewButton.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                updateOrder();
//            }
//        });
    }

    private void clearName() {
        orderId_txt.setText("");
        qtyOrder_snip.setValue(0);
        bookTitleOrder_cb.setSelectedIndex(0);
        insertButton.setText("Checkout");

        row = -1;
    }

    private void checkID() {
        boolean flag = false;
        for (Order o : orderController.getOrderList()) {
            if (o.getOrderId().equals(orderId_txt.getText())) {
                flag = true;
                break;
            }
        }
        if (flag) {
            JOptionPane.showMessageDialog(null, "Order ID is not allowed duplicate");
        }else {
            createOrder();
        }
    }

//    private void updateOrder() {
//        editOrder();
//        fillToTable();
//        XFile.writeObject(filePathOrder,orderList);
//    }
//
//    private void editOrder() {
//        for (Order o : orderList) {
//            if (o.getOrderId().equals(orderId_txt.getText())){
//                o.getBook().setBookTitle(bookTitleOrder_cb.getSelectedItem().toString());
//                o.getBook().setQuantity((int) qtyOrder_snip.getValue());
//                o.setTotal(Double.parseDouble(total_txt.getText()));
//                break;
//            }
//        }
//
//    }

    private void createOrder() {
        saveFromForm();
        orderController.fillToTable();
        XFile.writeObject(filePathOrder,orderController.getOrderList());
        clearName();
    }

    private void saveFromForm() {
        Book book = new Book();
        int newQuantity = 0;
        for (Book b: bookList) {
            if (b.getBookTitle().equals(bookTitleOrder_cb.getSelectedItem().toString())){
                book = b;

                int updateQuantity = b.getQuantity() - (Integer) qtyOrder_snip.getValue();

                if (updateQuantity <= 0) {
                    JOptionPane.showMessageDialog(null, "Out of product in stock");
                    newQuantity = 1;
                    break;
                }else if (updateQuantity < 10) {
                    JOptionPane.showMessageDialog(null, "Attention, a number of books in stock is less than 10 products!");
                    b.setQuantity(updateQuantity);
                    orderController.fillToTable();
                    XFile.writeObject(filePathBook,bookList);
                }else {
                    b.setQuantity(updateQuantity);
                    orderController.fillToTable();
                    XFile.writeObject(filePathBook,bookList);
                }

            }
        }
        if (newQuantity == 0) {
            Order order = new Order(
                    orderId_txt.getText(),
                    book,
                    (Integer) qtyOrder_snip.getValue(),
                    Double.parseDouble(total_txt.getText())
            );
            orderController.insert(order);
        }
    }

    //Read data from BookList
    private List<Book> gotBookTitle(){
        bookList = (List<Book>) XFile.readObject(filePathBook);
        return bookList;
    }

    //Show in comboBox
    private void bookTitle() {
        DefaultComboBoxModel<String> model1 = new DefaultComboBoxModel<>();
        ArrayList<String> bookTitle = new ArrayList<>();
        ArrayList<Book> list = (ArrayList<Book>) gotBookTitle();
        if ( list != null){
            for(Book b: list){
                bookTitle.add(b.getBookTitle());
            }
            for (String order: bookTitle) {
                model1.addElement(order);
            }bookTitleOrder_cb.setModel(model1);
        }
    }

    //Check ID with OR#####
    private static boolean checkSpecificID(String input){
        // Compile the regular expression pattern
        Pattern pattern = Pattern.compile("OR\\d{5}");
        // Create a Matcher object to match the pattern against the input string
        Matcher matcher = pattern.matcher(input);
        // Use the matches() method to check if the input string matches the pattern
        return matcher.matches();
    }

    private void checkInput(){
        StringBuffer sb = new StringBuffer();
        String input = orderId_txt.getText();
        boolean inputValid = checkSpecificID(input); //Use to call a check Order category ID function

        if (orderId_txt.getText().equals("")){
            sb.append("\nOrder ID is not allowed empty!!!");
        }
        if(qtyOrder_snip.getValue().equals(0)){
            sb.append("\nBook Quantity is not equal zero!!!");
        }
        if ((Integer)qtyOrder_snip.getValue() <= 0){
            sb.append("\nNumber of books must be a positive number!!!");
        }
        if (!inputValid){
            sb.append("\nPlease enter Order ID with OR#####!!!");
        }
        if (sb.length() > 0){
            JOptionPane.showMessageDialog(orderPanel, sb.toString(), "Invalidation", JOptionPane.ERROR_MESSAGE);
        }else {
            checkID();
        }
    }
}

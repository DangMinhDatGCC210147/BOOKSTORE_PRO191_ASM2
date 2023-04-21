package Controller;

import Model.Order;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderController extends IOException {
    DefaultTableModel tableOrderModel;
    List<Order> orderList;
    public DefaultTableModel getTableOrderModel() {
        return tableOrderModel;
    }

    public void setTableOrderModel(DefaultTableModel tableOrderModel) {
        this.tableOrderModel = tableOrderModel;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public OrderController(DefaultTableModel tableOrderModel, List<Order> orderList) {
        this.tableOrderModel = tableOrderModel;
        this.orderList = orderList;
        if (this.orderList == null || this.orderList.size() == 0){
            this.orderList = new ArrayList<>();
        }
    }
    public void fillToTable() {
        tableOrderModel.setRowCount(0);
        for (Order o : orderList) {
            Object[] row = new Object[] {
                    o.getOrderId(), o.getBook().getBookTitle(),o.getQuantity(), o.getBook().getPrice(), o.getTotal()
            };
            tableOrderModel.addRow(row);
        }
    }
    public void insert(Order order){
        orderList.add(order);
    }
    public void remove(String id){
        for (Order or: orderList) {
            if (id.equals(or.getOrderId())){
                orderList.remove(or);
                break;
            }
        }
    }
    public void removeAll(List<Order> orderList){
        orderList.removeAll(orderList);
    }
}

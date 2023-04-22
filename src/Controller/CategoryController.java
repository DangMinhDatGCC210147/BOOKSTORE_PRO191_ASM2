package Controller;

import Model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class CategoryController {
    DefaultTableModel tableCategoryModel;
    List<Category> categories;

    public DefaultTableModel getTableCategoryModel() {
        return tableCategoryModel;
    }

    public void setTableCategoryModel(DefaultTableModel tableCategoryModel) {
        this.tableCategoryModel = tableCategoryModel;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public CategoryController() {

    }

    public CategoryController(DefaultTableModel tableCategoryModel, List<Category> categories) {
        this.tableCategoryModel = tableCategoryModel;
        this.categories = categories;

            if (this.categories == null || this.categories.size() == 0){
                this.categories = new ArrayList<>();
            }
    }

    public void fillToTable() {
        tableCategoryModel.setRowCount(0);
        for (Category c : categories) {
            Object[] row = new Object[] {
                    c.getCategoryId(), c.getCategoryName()
            };
            tableCategoryModel.addRow(row);
        }
    }

    public void insert(Category category){
        categories.add(category);
    }
    public void remove(String id){
        for (Category ca: categories) {
            if (id.equals(ca.getCategoryId())){
                categories.remove(ca);
                break;
            }
        }
    }
    public void removeAll(List<Category> categories){
        categories.removeAll(categories);
    }
    public void read() throws ClassNotFoundException {

        try {
            FileInputStream fi = new FileInputStream("category.dat");
            ObjectInputStream iStream = new ObjectInputStream(fi);

            Category temp;

            while (iStream.read() <  0) {
                temp = (Category)iStream.readObject();
                System.out.println("" + temp.toString());
            }

        }  catch(EOFException eof)
        {
            JOptionPane.showMessageDialog(null,"Database is empty!!");
        }catch(IOException e ){
            JOptionPane.showMessageDialog(null,"Database is empty!!");
        }
    }


}

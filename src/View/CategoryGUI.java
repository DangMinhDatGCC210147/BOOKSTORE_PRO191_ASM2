package View;

import Controller.CategoryController;
import Lib.XFile;
import Model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CategoryGUI extends JFrame {
    private JTextField categoryId_txt;
    private JButton addCate_btn;
    private JButton updateCate_btn;
    private JButton deleteCate_btn;
    private JTable category_tb;
    private JTextField categoryName_txt;
    private JPanel categoryPanel;
    private JButton exitCate_btn;
    private JScrollPane categoryTable;
    private JButton deleteAll_btn;
    private JButton clearCategory_btn;
    private JTextField search_txt;
    private JButton searchButton;
    CategoryController categoryController;
    DefaultTableModel tableCategoryModel;
    String filePath = "src\\File\\category.dat";
    int row = -1;

    public CategoryGUI() throws HeadlessException{
        setTitle("Manage Category");
        this.setResizable(false);
        this.setContentPane(categoryPanel);
        setMinimumSize(new Dimension(650, 350));
        setLocationRelativeTo(categoryPanel);
        setVisible(true);
        category_tb.setRowHeight(30);

        category_tb.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[] {
                        "Category ID", "Category Name"
                }
        )
        { // not allow for user edit in the table
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        }
        );

        categoryController = new CategoryController(
                (DefaultTableModel) category_tb.getModel(),
                (List<Category>) XFile.readObject(filePath)
        );

        categoryController.fillToTable();

        addCate_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                StringBuffer sb = new StringBuffer();
                String input = categoryId_txt.getText();
                boolean inputValid = checkSpecificID(input); //Use to check specific category ID

                if (categoryId_txt.getText().equals("")){
                    sb.append("\nCategory ID is not allowed empty!!!");
                }
                if(categoryName_txt.getText().equals("")){
                    sb.append("\nCategory Name is not allowed empty!!!");
                }
                if (!inputValid){
                    sb.append("\nPlease enter category ID with CAT###!!!");
                }
                if (sb.length() > 0){
                    JOptionPane.showMessageDialog(categoryPanel, sb.toString(), "Invalidation", JOptionPane.ERROR_MESSAGE);
                }else{
                    checkID();
                    clearCate();
                }
            }
        });
        updateCate_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateCate();
            }
        });
        exitCate_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int choice = JOptionPane.showConfirmDialog(categoryPanel, "Do you want to exit?","Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION){
                    MainPageGUI window2 = new MainPageGUI();
                    window2.setVisible(true);
                    dispose();
                }
            }
        });
        deleteCate_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int  getSelectedRowForDeletion = category_tb.getSelectedRow();

                int answer = JOptionPane.showConfirmDialog(categoryPanel, "Do you want to remove this data?", "Remove",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (answer == JOptionPane.YES_OPTION){
                    if(getSelectedRowForDeletion >= 0){
                        categoryController.remove(categoryId_txt.getText());
                        categoryController.fillToTable();
                        XFile.writeObject(filePath, categoryController.getCategories());
                        JOptionPane.showMessageDialog(null, "Row is deleted!");
                    }else{
                        JOptionPane.showMessageDialog(null, "Unable to delete");
                    }
                }
            }
        });
        //Fill to JText from grid table
        category_tb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                row = category_tb.getSelectedRow();
                TableModel cateModel = category_tb.getModel();
                categoryId_txt.setText(cateModel.getValueAt(row, 0).toString());
                categoryName_txt.setText(cateModel.getValueAt(row, 1).toString());
            }
        });
        clearCategory_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearCate();
            }
        });
        deleteAll_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int answer = JOptionPane.showConfirmDialog(categoryPanel, "Do you want to remove all data in this table?", "Remove",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (answer == JOptionPane.YES_OPTION){
                    categoryController.removeAll(categoryController.getCategories());
                        categoryController.fillToTable();
                        XFile.writeObject(filePath, categoryController.getCategories());
                        JOptionPane.showMessageDialog(null, "All row have been deleted!");
                    }
            }
        });
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String query = search_txt.getText();
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(category_tb.getModel());
                sorter.setRowFilter(RowFilter.regexFilter(query));
                category_tb.setRowSorter(sorter);
            }
        });
    }

    private void clearCate() {
        categoryId_txt.setText("");
        categoryName_txt.setText("");
        row = -1;
    }


    private void checkID() {
        boolean flag = false;
        for (Category c : categoryController.getCategories()) {
            if (c.getCategoryId().equals(categoryId_txt.getText())) {
                flag = true;
                break;
            }
        }
        if (flag) {
            JOptionPane.showMessageDialog(null, "Category ID is not allowed duplicate");
        }else {
            createCategory();
        }
    }

    private void updateCate() {
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Please choose the row you want to edit!!");
        }else{
            //edit list
            editCate();
            //fill to table
            categoryController.fillToTable();
            //save file
            XFile.writeObject(filePath, categoryController.getCategories());
        }
    }

    private void editCate() {
        for (Category c : categoryController.getCategories()) {
            if (c.getCategoryId().equals(categoryId_txt.getText())){
                c.setCategoryName(categoryName_txt.getText());
                break;
            }
        }
    }
    private void createCategory() {
        saveFromForm();
        categoryController.fillToTable();
        XFile.writeObject(filePath,categoryController.getCategories());
    }

    private void saveFromForm() {
        Category category = new Category(
                categoryId_txt.getText(),
                categoryName_txt.getText()
        );
    categoryController.insert(category);
    }


    //Check ID with CAT###
    private static boolean checkSpecificID(String input){
        // Compile the regular expression pattern
        Pattern pattern = Pattern.compile("CAT\\d{3}");
        // Create a Matcher object to match the pattern against the input string
        Matcher matcher = pattern.matcher(input);
        // Use the matches() method to check if the input string matches the pattern
        return matcher.matches();
    }

}

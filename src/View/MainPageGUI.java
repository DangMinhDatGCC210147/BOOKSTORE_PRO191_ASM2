package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainPageGUI extends JFrame {
    private JButton BOOKButton;
    private JButton CATEGORYButton;
    private JButton ORDERButton;
    private JPanel mainPagePanel;
    private JButton exit_btn;

    public MainPageGUI() throws HeadlessException {
        setTitle("MAIN PAGE");
        this.setResizable(false);
        this.setContentPane(mainPagePanel);
        setMinimumSize(new Dimension(450, 250));
        setLocationRelativeTo(mainPagePanel);
        setVisible(true);
        BOOKButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BookGUI book = new BookGUI();
                book.setVisible(true);
                dispose();
            }
        });
        CATEGORYButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CategoryGUI category = new CategoryGUI();
                category.setVisible(true);//show view
                dispose(); //close previous view
            }
        });
        exit_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Login login = new Login();
                login.setVisible(true);
                dispose();
            }
        });
        ORDERButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                // Code to read data from database
                OrderGUI order = new OrderGUI();
                order.setVisible(true);
                dispose();
            }
        });
    }
}

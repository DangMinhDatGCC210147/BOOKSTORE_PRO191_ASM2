package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JFrame{
    private JTextField userId_txt;
    private JTextField pass_txt;
    private JButton login_btn;
    private JButton cancel_btn;
    private JPanel loginPanel;

    public Login() {
        this.setResizable(false);
        this.setContentPane(loginPanel);
        this.pack();
        this.setLocationRelativeTo(loginPanel);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(450, 250));

        login_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                StringBuffer sb = new StringBuffer();
                if (userId_txt.getText().equals("")){
                    sb.append("\nUser ID is not allowed empty!!!");
                }
                if(pass_txt.getText().equals("")){
                    sb.append("\nPassword is not allowed empty!!!");
                }
                if (sb.length() > 0){
                    JOptionPane.showMessageDialog(loginPanel, sb.toString(), "Invalidation", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (userId_txt.getText().equals("dat") && pass_txt.getText().equals("1")){
                    MainPageGUI window3 = new MainPageGUI();
                    window3.setVisible(true);
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(null,"Wrong Password or User Id!!!");
                }
            }

        });
        cancel_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                System.exit(0);
                int choice = JOptionPane.showConfirmDialog(loginPanel, "Do you want to exit?","Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }
        });
    }
}

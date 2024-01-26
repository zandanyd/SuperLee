//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package manueAndLogin;

import com.raven.swing.Button;
import com.raven.swing.MyPasswordField;
import com.raven.swing.MyTextField;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelLoginAndRegisterNew extends JLayeredPane {
    private JPanel login;
    private JPanel register;

    public PanelLoginAndRegisterNew() {
        this.initComponents();
        this.initRegister();
        this.initLogin();
        this.login.setVisible(false);
        this.register.setVisible(true);
    }

    private void initRegister() {
        this.register.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("ABOUT US");
        label.setFont(new Font("sansserif", 1, 150));
        label.setForeground(new Color(48, 149, 223 ));
        this.register.add(label);
        JLabel label2 = new JLabel("Dvir Zandany \n noam chohen \n ido dai \n tomer katsav");
        label.setFont(new Font("sansserif", 1, 15));
        label.setForeground(new Color(48, 149, 223 ));
        this.register.add(label2);

    }

    private void initLogin() {
        this.login.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Sign In");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(48, 149, 223 ));
        this.login.add(label);
        MyTextField txtEmail = new MyTextField();
        txtEmail.setPrefixIcon(new ImageIcon(this.getClass().getResource("/com/raven/icon/mail.png")));
        txtEmail.setHint("User Name");
        this.login.add(txtEmail, "w 60%");
        MyPasswordField txtPass = new MyPasswordField();
        txtPass.setPrefixIcon(new ImageIcon(this.getClass().getResource("/com/raven/icon/pass.png")));
        txtPass.setHint("Password");
        this.login.add(txtPass, "w 60%");
        Button cmd = new Button();
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(CurrentUserController.getInstance().setAll(txtEmail,txtPass)){
                    MainMenu mainMenu = new MainMenu();
                    mainMenu.setSize(1186, 621);
                    mainMenu.setLocationRelativeTo(null);
                    mainMenu.setVisible(true);
                }

            }
        });
        cmd.setBackground(new Color(48, 149, 223));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.setText("SIGN IN");
        this.login.add(cmd, "w 40%, h 40");
    }

    public void showRegister(boolean show) {
        if (show) {
            this.register.setVisible(true);
            this.login.setVisible(false);
        } else {
            this.register.setVisible(false);
            this.login.setVisible(true);
        }

    }

    private void initComponents() {
        this.login = new JPanel();
        this.register = new JPanel();
        this.setLayout(new CardLayout());

        this.login.setBackground(new Color(255, 255, 255));
        GroupLayout loginLayout = new GroupLayout(this.login);
        this.login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(loginLayout.createParallelGroup(Alignment.LEADING).addGap(0, 327, 32767));
        loginLayout.setVerticalGroup(loginLayout.createParallelGroup(Alignment.LEADING).addGap(0, 300, 32767));
        this.add(this.login, "card3");
        this.register.setBackground(new Color(255, 255, 255));
        GroupLayout registerLayout = new GroupLayout(this.register);
        this.register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(registerLayout.createParallelGroup(Alignment.LEADING).addGap(0, 327, 32767));
        registerLayout.setVerticalGroup(registerLayout.createParallelGroup(Alignment.LEADING).addGap(0, 300, 32767));
        this.add(this.register, "card2");
    }
}

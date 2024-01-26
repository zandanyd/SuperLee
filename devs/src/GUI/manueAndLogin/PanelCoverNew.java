package manueAndLogin;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.raven.swing.ButtonOutLine;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class PanelCoverNew extends JPanel {
    private final DecimalFormat df = new DecimalFormat("##0.###");
    private ActionListener event;
    private MigLayout layout;
    private JLabel title;
    private JLabel description;
    private JLabel description1;
    private ButtonOutLine button;
    private boolean isLogin;

    public PanelCoverNew() {
        this.initComponents();
        this.setOpaque(false);
        this.layout = new MigLayout("wrap, fill", "[center]", "push[]25[]10[]25[]push");
        this.setLayout(this.layout);
        this.init();
    }

    private void init() {
        this.title = new JLabel("Welcome Back!");
        this.title.setFont(new Font("sansserif", 1, 30));
        this.title.setForeground(new Color(245, 245, 245));
        this.add(this.title);
        this.description = new JLabel("To start using SuperLee system");
        this.description.setForeground(new Color(245, 245, 245));
        this.add(this.description);
        this.description1 = new JLabel("Press here");
        this.description1.setForeground(new Color(242, 242, 242));
        this.add(this.description1);
        this.button = new ButtonOutLine();
        this.button.setBackground(new Color(255, 255, 255));
        this.button.setForeground(new Color(255, 255, 255));
        this.button.setText("SIGN IN");
        this.button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                manueAndLogin.PanelCoverNew.this.event.actionPerformed(ae);
            }
        });
        this.add(this.button, "w 60%, h 40");
    }

    private void initComponents() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGap(0, 327, 32767));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGap(0, 300, 32767));
    }

    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D)grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint g = new GradientPaint(0, 0, Color.decode("#1CB5E0"), 0, getHeight(), Color.decode("#000046"));
        g2.setPaint(g);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.fillRect(getWidth() - 20, 0, getWidth(), getHeight());
        super.paintComponent(grphcs);
    }

    public void addEvent(ActionListener event) {
        this.event = event;
    }

    public void registerLeft(double v) {
        v = Double.valueOf(this.df.format(v));
        this.login(false);
        this.layout.setComponentConstraints(this.title, "pad 0 -" + v + "% 0 0");
        this.layout.setComponentConstraints(this.description, "pad 0 -" + v + "% 0 0");
        this.layout.setComponentConstraints(this.description1, "pad 0 -" + v + "% 0 0");
    }

    public void registerRight(double v) {
        v = Double.valueOf(this.df.format(v));
        this.login(false);
        this.layout.setComponentConstraints(this.title, "pad 0 -" + v + "% 0 0");
        this.layout.setComponentConstraints(this.description, "pad 0 -" + v + "% 0 0");
        this.layout.setComponentConstraints(this.description1, "pad 0 -" + v + "% 0 0");
    }

    public void loginLeft(double v) {
        v = Double.valueOf(this.df.format(v));
        this.login(true);
        this.layout.setComponentConstraints(this.title, "pad 0 " + v + "% 0 " + v + "%");
        this.layout.setComponentConstraints(this.description, "pad 0 " + v + "% 0 " + v + "%");
        this.layout.setComponentConstraints(this.description1, "pad 0 " + v + "% 0 " + v + "%");
    }

    public void loginRight(double v) {
        v = Double.valueOf(this.df.format(v));
        this.login(true);
        this.layout.setComponentConstraints(this.title, "pad 0 " + v + "% 0 " + v + "%");
        this.layout.setComponentConstraints(this.description, "pad 0 " + v + "% 0 " + v + "%");
        this.layout.setComponentConstraints(this.description1, "pad 0 " + v + "% 0 " + v + "%");
    }

    private void login(boolean login) {
        if (this.isLogin != login) {
            if (login) {
                this.title.setText("About Us");
                this.description.setText("If you want to know more About Us");
                this.description1.setText("Press here");
                this.button.setText("About Us");
            } else {
                this.title.setText("Welcome Back!");
                this.description.setText("Enter your personal details");
                this.description1.setText("and start journey with us");
                this.button.setText("SIGN IN");

            }

            this.isLogin = login;
        }

    }



}

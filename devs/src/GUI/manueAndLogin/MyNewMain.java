package manueAndLogin;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyNewMain extends JFrame {
    private MigLayout layout;
    private PanelCoverNew cover;
    private PanelLoginAndRegisterNew loginAndRegister;
    private boolean isLogin;
    private final double addSize = 30.0;
    private final double coverSize = 40.0;
    private final double loginSize = 60.0;
    private final DecimalFormat df = new DecimalFormat("##0.###");
    private JLayeredPane bg;

    public MyNewMain() {
        this.initComponents();
        this.init();
    }

    private void init() {
        this.layout = new MigLayout("fill, insets 0");
        this.cover = new PanelCoverNew();
        this.loginAndRegister = new PanelLoginAndRegisterNew();
        TimingTarget target = new TimingTargetAdapter() {
            public void timingEvent(float fraction) {
                double size = 40.0;
                if (fraction <= 0.5F) {
                    size += (double)fraction * 30.0;
                } else {
                    size += 30.0 - (double)fraction * 30.0;
                }

                double fractionCover;
                double fractionLogin;
                if (manueAndLogin.MyNewMain.this.isLogin) {
                    fractionCover = (double)(1.0F - fraction);
                    fractionLogin = (double)fraction;
                    if (fraction >= 0.5F) {
                        manueAndLogin.MyNewMain.this.cover.registerRight(fractionCover * 100.0);
                    } else {
                        manueAndLogin.MyNewMain.this.cover.loginRight(fractionLogin * 100.0);
                    }
                } else {
                    fractionCover = (double)fraction;
                    fractionLogin = (double)(1.0F - fraction);
                    if (fraction <= 0.5F) {
                        manueAndLogin.MyNewMain.this.cover.registerLeft((double)(fraction * 100.0F));
                    } else {
                        manueAndLogin.MyNewMain.this.cover.loginLeft((double)((1.0F - fraction) * 100.0F));
                    }
                }

                if (fraction >= 0.5F) {
                    manueAndLogin.MyNewMain.this.loginAndRegister.showRegister(manueAndLogin.MyNewMain.this.isLogin);
                }

                fractionCover = Double.valueOf(manueAndLogin.MyNewMain.this.df.format(fractionCover));
                fractionLogin = Double.valueOf(manueAndLogin.MyNewMain.this.df.format(fractionLogin));
                manueAndLogin.MyNewMain.this.layout.setComponentConstraints( manueAndLogin.MyNewMain.this.cover, "width " + size + "%, pos " + fractionCover + "al 0 n 100%");
                manueAndLogin.MyNewMain.this.layout.setComponentConstraints( manueAndLogin.MyNewMain.this.loginAndRegister, "width 60.0%, pos " + fractionLogin + "al 0 n 100%");
                manueAndLogin.MyNewMain.this.bg.revalidate();
            }

            public void end() {
                manueAndLogin.MyNewMain.this.isLogin = ! manueAndLogin.MyNewMain.this.isLogin;
            }
        };
        final Animator animator = new Animator(800, target);
        animator.setAcceleration(0.5F);
        animator.setDeceleration(0.5F);
        animator.setResolution(0);
        this.bg.setLayout(this.layout);
        this.bg.add(this.cover, "width 40.0%, pos 0al 0 n 100%");
        this.bg.add(this.loginAndRegister, "width 60.0%, pos 1al 0 n 100%");
        this.cover.addEvent(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!animator.isRunning()) {
                    animator.start();
                }

            }
        });
    }

    private void initComponents() {
        this.bg = new JLayeredPane();
        this.setDefaultCloseOperation(3);
        this.setUndecorated(true);
        this.bg.setBackground(new Color(255, 255, 255));
        this.bg.setOpaque(true);
        GroupLayout bgLayout = new GroupLayout(this.bg);
        this.bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(bgLayout.createParallelGroup(Alignment.LEADING).addGap(0, 933, 32767));
        bgLayout.setVerticalGroup(bgLayout.createParallelGroup(Alignment.LEADING).addGap(0, 537, 32767));
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.bg, Alignment.TRAILING));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.bg));
        this.pack();
        this.setLocationRelativeTo((Component)null);
    }

    public static void main(String[] args) {
        try {
            UIManager.LookAndFeelInfo[] var1 = UIManager.getInstalledLookAndFeels();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                UIManager.LookAndFeelInfo info = var1[var3];
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException var5) {
            Logger.getLogger(com.raven.main.Main.class.getName()).log(Level.SEVERE, (String)null, var5);
        } catch (InstantiationException var6) {
            Logger.getLogger(com.raven.main.Main.class.getName()).log(Level.SEVERE, (String)null, var6);
        } catch (IllegalAccessException var7) {
            Logger.getLogger(com.raven.main.Main.class.getName()).log(Level.SEVERE, (String)null, var7);
        } catch (UnsupportedLookAndFeelException var8) {
            Logger.getLogger(com.raven.main.Main.class.getName()).log(Level.SEVERE, (String)null, var8);
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                (new MyNewMain()).setVisible(true);
            }
        });
    }
}


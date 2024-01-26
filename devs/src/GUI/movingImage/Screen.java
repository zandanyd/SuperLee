package movingImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Screen extends JPanel implements ActionListener {
    Timer timer = new Timer(20, this);
    BufferedImage bufferedImage;
    float value = 1f;

    public Screen(){
        loadBufferedImage();
        timer.start();
    }

    public void loadBufferedImage() {
        bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("1.png")));
        } catch (IOException e){
            }
    }

    public  void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, value));

        g2d.drawImage(bufferedImage, 100, 20, null);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        value = value - 0.01f;

        if (value < 0){
            value = 0;
            timer.stop();
        }

        repaint();
    }
}

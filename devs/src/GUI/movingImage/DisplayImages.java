package movingImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

public class DisplayImages extends JFrame {
    private static final String IMAGE_DIRECTORY = "Images";
    private static final int TOTAL_IMAGES = 16;
    private static final int IMAGE_WIDTH = 200;  // Set the desired width of the images
    private static final int IMAGE_HEIGHT = 200; // Set the desired height of the images
    private static final int ROTATION_DELAY = 200; // Decreased delay for faster rotation

    private JLabel imageLabel;
    private ArrayList<ImageIcon> imageIcons;
    private int currentImageIndex = 0;

    public DisplayImages() {
        setTitle("Display Images");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(imageLabel, BorderLayout.CENTER);

        loadImages();
        displayNextImage();
        startImageRotation();
    }

    private void loadImages() {
        imageIcons = new ArrayList<>();
        for (int i = 1; i <= TOTAL_IMAGES; i++) {
            String imagePath = IMAGE_DIRECTORY + "/" + i + ".jpg";
            ImageIcon imageIcon = resizeImageIcon(imagePath, IMAGE_WIDTH, IMAGE_HEIGHT);
            imageIcons.add(imageIcon);
        }
    }

    private ImageIcon resizeImageIcon(String imagePath, int width, int height) {
        try {
            URL imageUrl = getClass().getClassLoader().getResource(imagePath);
            if (imageUrl != null) {
                BufferedImage image = ImageIO.read(imageUrl);
                Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(resizedImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void displayNextImage() {
        ImageIcon currentImageIcon = imageIcons.get(currentImageIndex);
        imageLabel.setIcon(currentImageIcon);

        currentImageIndex++;
        if (currentImageIndex >= TOTAL_IMAGES) {
            currentImageIndex = 0;
        }
    }

    private void startImageRotation() {
        Timer timer = new Timer(ROTATION_DELAY, e -> displayNextImage()); // Decreased delay for faster rotation
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DisplayImages().setVisible(true));
    }
}
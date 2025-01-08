package src.Modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class ImageUtil {

    public static ImageIcon getScaledImageIcon(String imageUrl, int width, int height) {
        try {
            ImageIcon originalIcon = new ImageIcon(new URL(imageUrl));
            Image image = originalIcon.getImage();
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            return new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)); // Placeholder image
        }
    }
}
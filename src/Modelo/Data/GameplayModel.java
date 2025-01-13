package src.Modelo.Data;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class GameplayModel {
    private String thumbnailUrl;
    private String videoUrl;

    public GameplayModel(String thumbnailUrl, String videoUrl) {
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getScaledThumbnail(int width, int height) {
        try {
            URL url = new URL(thumbnailUrl);
            ImageIcon icon = new ImageIcon(url);
            Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(image).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Thumbnail not available";
        }
    }
}
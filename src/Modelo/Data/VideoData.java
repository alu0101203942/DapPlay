package src.Modelo.Data;

import src.Modelo.ImageUtil;

import javax.swing.*;

public class VideoData {
    private final String videoUrl;
    private final String thumbnailUrl;

    public VideoData(String videoUrl, String thumbnailUrl) {
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public ImageIcon getScaledThumbnail(int width, int height) {
        return ImageUtil.getScaledImageIcon(thumbnailUrl, width, height);
    }
}
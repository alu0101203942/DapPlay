package src.Vista;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import com.lukaspradel.steamapi.data.json.playersummaries.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class GamePanelFactory implements PanelFactoryGame {
    private static final int MARGIN = 10;
    @Override
    public JPanel createPanel(Game game, ActionListener favoriteAction) {
        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        String imageUrl = "http://media.steampowered.com/steamcommunity/public/images/apps/"
                + game.getAppid() + "/" + game.getImgIconUrl() + ".jpg";
        try {
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);
            if (image != null) {
                Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaledImage);
                JLabel imageLabel = new JLabel(icon);
                imageLabel.setPreferredSize(new Dimension(75, 75));
                gamePanel.add(imageLabel, BorderLayout.WEST);
            } else {
                gamePanel.add(new JLabel("No Image"), BorderLayout.WEST);
            }
        } catch (IOException ex) {
            gamePanel.add(new JLabel("Failed to load image"), BorderLayout.WEST);
        }

        JPanel textPanel = new JPanel(new GridBagLayout());
        textPanel.setBackground(new Color(240, 240, 240));
        textPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel nameLabel = new JLabel("Game: " + game.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(Color.BLACK);
        textPanel.add(nameLabel, gbc);

        String playtimeFormatted = String.format("%.1f", game.getPlaytimeForever() / 60.0);
        JLabel playtimeLabel = new JLabel("Playtime (hours): " + playtimeFormatted);
        playtimeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        playtimeLabel.setForeground(Color.BLACK);
        gbc.gridy = 1;
        textPanel.add(playtimeLabel, gbc);

        JButton favoriteButton = new JButton("Add to Favorites");
        favoriteButton.addActionListener(favoriteAction);
        gbc.gridy = 2;
        textPanel.add(favoriteButton, gbc);

        gamePanel.add(textPanel, BorderLayout.CENTER);

        return gamePanel;
    }

}
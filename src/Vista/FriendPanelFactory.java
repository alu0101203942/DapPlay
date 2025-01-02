package src.Vista;

import com.lukaspradel.steamapi.data.json.playersummaries.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class FriendPanelFactory implements PanelFactoryFriend {
    private static final int MARGIN = 10;
    @Override
    public JPanel createPanel(Player friendList) {
        JPanel friendPanel = new JPanel(new BorderLayout());
        friendPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN)
        ));

        String imageUrl = friendList.getAvatarfull();
        try {
            URL url = new URL(imageUrl);
            Image image = ImageIO.read(url);
            if (image != null) {
                Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaledImage);
                JLabel imageLabel = new JLabel(icon);
                imageLabel.setPreferredSize(new Dimension(100, 100));
                friendPanel.add(imageLabel, BorderLayout.WEST);
            } else {
                friendPanel.add(new JLabel("No Image"), BorderLayout.WEST);
            }
        } catch (IOException ex) {
            friendPanel.add(new JLabel("Failed to load image"), BorderLayout.WEST);
        }

        JPanel textPanel = new JPanel(new GridBagLayout());
        textPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel nameLabel = new JLabel(friendList.getPersonaname());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Set the font size to 16
        nameLabel.setForeground(Color.BLACK); // Set the font color to black
        textPanel.add(nameLabel, gbc);

        friendPanel.add(textPanel, BorderLayout.CENTER);

        return friendPanel;
    }

}
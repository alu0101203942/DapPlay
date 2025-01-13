package src.Vista.PanelF;

import src.Modelo.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class AchievementPanelFactory implements PanelFactory {
    private JPanel achievementsPanel;

    public AchievementPanelFactory() {
        this.achievementsPanel = new JPanel();
    }

    public JPanel createPanel(List<Map<String, Object>> achievements, String title, Color titleColor) throws MalformedURLException {
        JPanel achievementsContainer = new JPanel();
        achievementsContainer.setLayout(new BorderLayout());
        achievementsContainer.setBackground(new Color(33, 33, 33));

        // Add title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(titleColor);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        achievementsContainer.add(titleLabel, BorderLayout.NORTH);

        // Create achievements list
        JPanel achievementsListPanel = new JPanel();
        achievementsListPanel.setLayout(new BoxLayout(achievementsListPanel, BoxLayout.Y_AXIS));
        achievementsListPanel.setBackground(new Color(33, 33, 33));

        for (Map<String, Object> achievement : achievements) {
            JPanel achievementPanel = new JPanel(new BorderLayout());
            achievementPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            achievementPanel.setBackground(new Color(44, 44, 44));

            int achieved = Integer.parseInt(achievement.get("achieved").toString());

            String iconUrl = achieved == 1
                    ? (String) achievement.get("icon")
                    : (String) achievement.get("iconGray");

            try {
                ImageIcon icon = ImageUtil.getScaledImageIcon(iconUrl, 50, 50);
                JLabel iconLabel = new JLabel(icon);
                achievementPanel.add(iconLabel, BorderLayout.WEST);
            } catch (Exception ex) {
                JLabel iconLabel = new JLabel("Icon not available");
                achievementPanel.add(iconLabel, BorderLayout.WEST);
            }

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(new Color(44, 44, 44));
            JLabel nameLabel = new JLabel((String) achievement.get("displayName"));
            nameLabel.setForeground(Color.WHITE);
            JLabel descLabel = new JLabel("<html>" + (String) achievement.get("description") + "</html>");
            descLabel.setForeground(Color.GRAY);
            infoPanel.add(nameLabel);
            infoPanel.add(descLabel);

            achievementPanel.add(infoPanel, BorderLayout.CENTER);
            achievementsListPanel.add(achievementPanel);
        }

        JScrollPane scrollPane = new JScrollPane(achievementsListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        achievementsContainer.add(scrollPane, BorderLayout.CENTER);

        return achievementsContainer;
    }

    public void displayNoAchievementsMessage(String gameName, String imageUrl, JPanel achievementsPanel) {
        try {
            // Crear el JLabel para el mensaje
            JLabel messageLabel = new JLabel("No se encuentran logros para " + gameName, SwingConstants.CENTER);
            messageLabel.setFont(new Font("Arial", Font.PLAIN, 20)); // Configurar la fuente
            messageLabel.setForeground(Color.RED); // Establecer el color del texto

            // Cargar la imagen desde la URL
            System.out.println("Loading image from URL: " + imageUrl); // Log the URL
            ImageIcon imageIcon = new ImageIcon(new URL(imageUrl));
            Image scaledImage = imageIcon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            // Configurar el JLabel con imagen
            messageLabel.setIcon(scaledIcon);
            messageLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            messageLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

            // Añadir el JLabel al panel de logros
            if (achievementsPanel != null) {
                achievementsPanel.removeAll();
                achievementsPanel.setLayout(new BorderLayout());
                achievementsPanel.add(messageLabel, BorderLayout.CENTER); // Centrar el contenido

                // Actualizar el panel
                achievementsPanel.revalidate();
                achievementsPanel.repaint();
            }
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("No se pudo cargar la imagen.", SwingConstants.CENTER);
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            errorLabel.setForeground(Color.RED);

            if (achievementsPanel != null) {
                achievementsPanel.removeAll();
                achievementsPanel.add(errorLabel, BorderLayout.CENTER); // Centrar el mensaje de error
                achievementsPanel.revalidate();
                achievementsPanel.repaint();
            }
            e.printStackTrace();
        }
    }

}
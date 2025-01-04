package src.Vista;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class AchievementPanelFactory implements PanelFactory {

    public JPanel createPanel(List<Map<String, Object>> achievements, String title, Color titleColor) throws MalformedURLException {
        JPanel achievementsContainer = new JPanel();
        achievementsContainer.setLayout(new BorderLayout());
        achievementsContainer.setBackground(new Color(33, 33, 33));

        // Agregar título
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(titleColor);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        achievementsContainer.add(titleLabel, BorderLayout.NORTH);

        // Crear lista de logros
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

            JLabel iconLabel = new JLabel(new ImageIcon(new URL(iconUrl)));
            achievementPanel.add(iconLabel, BorderLayout.WEST);

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
}

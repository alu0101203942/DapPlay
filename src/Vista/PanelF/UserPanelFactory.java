package src.Vista.PanelF;

import src.Modelo.Data.UserModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;

public class UserPanelFactory implements PanelFactoryUser {
    public JPanel createPanel(UserModel userModel) {
        JPanel userPanel = new JPanel(new GridBagLayout());
        userPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;

        JLabel avatarLabel = new JLabel();
        try {
            URL url = new URL(userModel.getAvatarUrl());
            Image image = ImageIO.read(url);
            ImageIcon avatarIcon = new ImageIcon(image.getScaledInstance(80, 80, Image.SCALE_SMOOTH));
            avatarLabel.setIcon(avatarIcon);
        } catch (Exception e) {
            avatarLabel.setText("No Avatar");
        }
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setPreferredSize(new Dimension(80, 80));
        gbc.gridy = 0;
        userPanel.add(avatarLabel, gbc);

        JLabel usernameLabel = new JLabel(userModel.getUsername(), SwingConstants.CENTER);
        JLabel gamesCountLabel = new JLabel("Juegos: " + userModel.getOwnedGamesCount(), SwingConstants.CENTER);
        JLabel profileStatusLabel = new JLabel("Estado del Perfil: " + userModel.getProfileStatus(), SwingConstants.CENTER);
        JLabel connectionStatusLabel = new JLabel("Conexión: " + userModel.getConnectionStatus(), SwingConstants.CENTER);

        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gamesCountLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        profileStatusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        connectionStatusLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        gbc.gridy = 1;
        userPanel.add(usernameLabel, gbc);
        gbc.gridy = 2;
        userPanel.add(gamesCountLabel, gbc);
        gbc.gridy = 3;
        userPanel.add(profileStatusLabel, gbc);
        gbc.gridy = 4;
        userPanel.add(connectionStatusLabel, gbc);

        JButton viewProfileButton = new JButton("Ver Perfil en Steam");
        viewProfileButton.addActionListener(e -> {
            try {
                String profileUrl = userModel.getProfileUrl();
                if (profileUrl == null || profileUrl.isEmpty()) {
                    JOptionPane.showMessageDialog(userPanel, "URL de perfil no válida o vacía.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(profileUrl));
                } else {
                    JOptionPane.showMessageDialog(userPanel, "No se puede abrir el navegador en este sistema.", "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(userPanel, "Error al abrir el perfil: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridy = 5;
        userPanel.add(viewProfileButton, gbc);

        return userPanel;
    }
}
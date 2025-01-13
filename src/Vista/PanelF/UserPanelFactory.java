package src.Vista.PanelF;

import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import src.Modelo.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class UserPanelFactory implements PanelFactoryUser {
    private static final int MARGIN = 10;
    ImageUtil imageUtil = new ImageUtil();

    @Override
    public JPanel createPanel(Player user, int games) {
        // Crear el panel principal para el usuario
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Avatar
        JLabel avatarLabel = new JLabel();
        try {
            URL url = new URL(user.getAvatarfull());
            ImageIcon avatarIcon = ImageUtil.getScaledImageIcon(url.toString(), 80, 80);
            avatarLabel.setIcon(avatarIcon);
        } catch (Exception e) {
            avatarLabel.setText("No Avatar");
        }
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setPreferredSize(new Dimension(80, 80));

        // Información del usuario
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));

        JLabel usernameLabel = new JLabel("Nombre: " + user.getPersonaname());
        JLabel gamesCountLabel = new JLabel("Juegos: " + games);
        JLabel profileStatusLabel = new JLabel("Estado del Perfil: " +
                (user.getCommunityvisibilitystate() == 3 ? "Público" : "Privado"));
        JLabel connectionStatusLabel = new JLabel("Conexión: " + mapConnectionStatus(user.getPersonastate().intValue()));

        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gamesCountLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        profileStatusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        connectionStatusLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        userInfoPanel.add(usernameLabel);
        userInfoPanel.add(gamesCountLabel);
        userInfoPanel.add(profileStatusLabel);
        userInfoPanel.add(connectionStatusLabel);

        // Botón para abrir perfil
        JButton viewProfileButton = new JButton("Ver Perfil en Steam");
        viewProfileButton.addActionListener(e -> {
            try {
                String profileUrl = user.getProfileurl();
                if (profileUrl == null || profileUrl.isEmpty()) {
                    JOptionPane.showMessageDialog(userPanel, "La URL del perfil no está disponible.");
                    return;
                }
                Desktop.getDesktop().browse(new URL(profileUrl).toURI());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(userPanel, "Error al abrir el perfil: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        userInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userInfoPanel.add(viewProfileButton);

        // Combinar avatar y texto en un solo panel
        userPanel.add(avatarLabel, BorderLayout.WEST);
        userPanel.add(userInfoPanel, BorderLayout.CENTER);

        return userPanel;
    }

//    public JPanel createPanel(Player user, int games) {
//        // Crear el panel principal para el usuario
//        JPanel userPanel = new JPanel(new BorderLayout());
//        userPanel.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(Color.GRAY, 1),
//                BorderFactory.createEmptyBorder(10, 10, 10, 10)
//        ));
//
//        // Avatar
//        JLabel avatarLabel = new JLabel();
//        try {
//            URL url = new URL(user.getAvatarfull());
//            ImageIcon avatarIcon = ImageUtil.getScaledImageIcon(url.toString(), 80, 80);
//            avatarLabel.setIcon(avatarIcon);
//        } catch (Exception e) {
//            avatarLabel.setText("No Avatar");
//        }
//        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        avatarLabel.setPreferredSize(new Dimension(80, 80));
//
//        // Información del usuario
//        JPanel userInfoPanel = new JPanel();
//        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
//
//        JLabel usernameLabel = new JLabel("Nombre: " + user.getPersonaname());
//        JLabel gamesCountLabel = new JLabel("Juegos: " + games);
//        JLabel profileStatusLabel = new JLabel("Estado del Perfil: " +
//                (user.getCommunityvisibilitystate() == 3 ? "Público" : "Privado"));
//        JLabel connectionStatusLabel = new JLabel("Conexión: " + mapConnectionStatus(user.getPersonastate().intValue()));
//
//        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
//        gamesCountLabel.setFont(new Font("Arial", Font.PLAIN, 12));
//        profileStatusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
//        connectionStatusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
//
//        userInfoPanel.add(usernameLabel);
//        userInfoPanel.add(gamesCountLabel);
//        userInfoPanel.add(profileStatusLabel);
//        userInfoPanel.add(connectionStatusLabel);
//
//        // Botón para abrir perfil
//        JButton viewProfileButton = new JButton("Ver Perfil en Steam");
////        viewProfileButton.addActionListener(e -> {
////            try {
////                String profileUrl = user.getProfileurl();
////                System.out.println("URL del perfil: " + profileUrl);
////                if (profileUrl == null || profileUrl.isEmpty()) {
////                    JOptionPane.showMessageDialog(userPanel, "La URL del perfil no está disponible.");
////                    return;
////                }
////                Desktop.getDesktop().browse(new URL(profileUrl).toURI());
////            } catch (Exception ex) {
////                JOptionPane.showMessageDialog(userPanel, "Error al abrir el perfil: " + ex.getMessage());
////                ex.printStackTrace();
////            }
////        });
//        viewProfileButton.addActionListener(e -> {
//            JOptionPane.showMessageDialog(userPanel, "El botón funciona, pero hay un problema al abrir el perfil.");
//        });
//        userInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
//        userInfoPanel.add(viewProfileButton);
//
//        // Combinar avatar y texto en un solo panel
//        userPanel.add(avatarLabel, BorderLayout.WEST);
//        userPanel.add(userInfoPanel, BorderLayout.CENTER);
//
//        return userPanel;
//    }

    private String mapConnectionStatus(int status) {
        return switch (status) {
            case 0 -> "Offline";
            case 1 -> "Online";
            case 2 -> "Busy";
            case 3 -> "Away";
            case 4 -> "Snooze";
            case 5 -> "Looking to Trade";
            case 6 -> "Looking to Play";
            default -> "Unknown";
        };
    }



}

package src.Vista.PanelF;

import com.lukaspradel.steamapi.data.json.playersummaries.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class UserPanelFactory implements PanelFactoryUser {
    private static final int MARGIN = 10;

    @Override
//    public JPanel createPanel(Player user, int games) {
//        // Crear el panel principal
//        JPanel userPanel = new JPanel(new BorderLayout());
//        userPanel.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(Color.GRAY, 1),
//                BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN)
//        ));
//
//        // Obtener la imagen del avatar
//        String imageUrl = user.getAvatarfull();
//        try {
//            URL url = new URL(imageUrl);
//            Image image = ImageIO.read(url);
//            if (image != null) {
//                // Escalar la imagen para que se ajuste al tamaño deseado
//                Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
//                ImageIcon icon = new ImageIcon(scaledImage);
//                JLabel imageLabel = new JLabel(icon);
//                imageLabel.setPreferredSize(new Dimension(100, 100));
//                userPanel.add(imageLabel, BorderLayout.WEST);
//            } else {
//                userPanel.add(new JLabel("No Image"), BorderLayout.WEST);
//            }
//        } catch (IOException ex) {
//            userPanel.add(new JLabel("Failed to load image"), BorderLayout.WEST);
//        }
//
//        // Crear el panel de texto
//        JPanel textPanel = new JPanel(new GridBagLayout());
//        textPanel.setBackground(new Color(240, 240, 240));
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.anchor = GridBagConstraints.CENTER; // Centrar elementos
//        gbc.insets = new Insets(5, 5, 5, 5); // Márgenes entre elementos
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//
//        // Nombre del usuario
//        JLabel nameLabel = new JLabel(user.getPersonaname());
//        nameLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Ajustar tamaño y estilo de fuente
//        nameLabel.setForeground(Color.BLACK);
//        textPanel.add(nameLabel, gbc);
//
//        // Número de juegos
//        gbc.gridy = 1; // Segunda fila
//        JLabel gamesCountLabel = new JLabel("Juegos: " + games);
//        gamesCountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
//        gamesCountLabel.setForeground(Color.DARK_GRAY);
//        textPanel.add(gamesCountLabel, gbc);
//
//        // Agregar el panel de texto al centro
//        userPanel.add(textPanel, BorderLayout.CENTER);
//
//        return userPanel;
//    }
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
            Image image = ImageIO.read(url);
            ImageIcon avatarIcon = new ImageIcon(image.getScaledInstance(80, 80, Image.SCALE_SMOOTH));
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
                Desktop.getDesktop().browse(new URL(user.getProfileurl()).toURI());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(userPanel, "Error al abrir el perfil: " + ex.getMessage());
            }
        });
        userInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userInfoPanel.add(viewProfileButton);

        // Combinar avatar y texto en un solo panel
        userPanel.add(avatarLabel, BorderLayout.WEST);
        userPanel.add(userInfoPanel, BorderLayout.CENTER);

        return userPanel;
    }

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

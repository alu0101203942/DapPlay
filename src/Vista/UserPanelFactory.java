package src.Vista;

import com.lukaspradel.steamapi.data.json.playersummaries.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class UserPanelFactory implements PanelFactoryUser {
    private static final int MARGIN = 10;

    @Override
    public JPanel createPanel(Player user, int games) {
        // Crear el panel principal
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN)
        ));

        // Obtener la imagen del avatar
        String imageUrl = user.getAvatarfull();
        try {
            URL url = new URL(imageUrl);
            Image image = ImageIO.read(url);
            if (image != null) {
                // Escalar la imagen para que se ajuste al tamaño deseado
                Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaledImage);
                JLabel imageLabel = new JLabel(icon);
                imageLabel.setPreferredSize(new Dimension(100, 100));
                userPanel.add(imageLabel, BorderLayout.WEST);
            } else {
                userPanel.add(new JLabel("No Image"), BorderLayout.WEST);
            }
        } catch (IOException ex) {
            userPanel.add(new JLabel("Failed to load image"), BorderLayout.WEST);
        }

        // Crear el panel de texto
        JPanel textPanel = new JPanel(new GridBagLayout());
        textPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER; // Centrar elementos
        gbc.insets = new Insets(5, 5, 5, 5); // Márgenes entre elementos
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Nombre del usuario
        JLabel nameLabel = new JLabel(user.getPersonaname());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Ajustar tamaño y estilo de fuente
        nameLabel.setForeground(Color.BLACK);
        textPanel.add(nameLabel, gbc);

        // Número de juegos
        gbc.gridy = 1; // Segunda fila
        JLabel gamesCountLabel = new JLabel("Juegos: " + games);
        gamesCountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gamesCountLabel.setForeground(Color.DARK_GRAY);
        textPanel.add(gamesCountLabel, gbc);

        // Agregar el panel de texto al centro
        userPanel.add(textPanel, BorderLayout.CENTER);

        return userPanel;
    }
}

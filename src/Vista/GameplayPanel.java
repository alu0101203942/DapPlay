package src.Vista;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameplayPanel implements PanelFactoryGame {

    @Override
    public JPanel createPanel(Game game, ActionListener removeAction) {
        // Crear el panel principal
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Agregar el nombre del juego en la parte superior
        JLabel gameNameLabel = new JLabel(game.getName());
        gameNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(gameNameLabel, BorderLayout.NORTH);

        // Crear un sub-panel para la miniatura y los botones
        JPanel contentPanel = new JPanel(new BorderLayout());
        panel.add(contentPanel, BorderLayout.CENTER);

        // Agregar una miniatura del gameplay (imagen de muestra)
        JLabel thumbnailLabel = new JLabel("Miniatura no disponible"); // Placeholder
        thumbnailLabel.setHorizontalAlignment(SwingConstants.CENTER);
        thumbnailLabel.setPreferredSize(new Dimension(200, 100));
        contentPanel.add(thumbnailLabel, BorderLayout.CENTER);

        // Botones en la parte inferior
        JPanel buttonPanel = new JPanel();
        JButton removeButton = new JButton("Eliminar");
        removeButton.addActionListener(removeAction);

        JButton playButton = new JButton("Reproducir Gameplay");
        playButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(panel, "Aquí se buscaría y reproduciría el gameplay para: " + game.getName());
            // Aquí puedes implementar la lógica para reproducir el gameplay
        });

        buttonPanel.add(playButton);
        buttonPanel.add(removeButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
}

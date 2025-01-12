package src.Vista.PanelF;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import src.Controlador.GameplayController;
import src.Modelo.API.YoutubeApiService;
import src.Modelo.Data.VideoData;
import src.Vista.ViewManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

public class GameplayPanel implements PanelFactoryGame {
    private YoutubeApiService youtubeApiService;
    //private ViewManager viewManager;

    public GameplayPanel( YoutubeApiService youtubeApiService) {
        //this.viewManager = viewManager;
        this.youtubeApiService = youtubeApiService;
    }

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

        // Crear un sub-panel para la miniatura y los enlaces
        JPanel contentPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botones en la parte inferior
        JPanel buttonPanel = new JPanel();
        JButton removeButton = new JButton("Eliminar");
        removeButton.addActionListener(removeAction);

        JButton loadGameplaysButton = new JButton("Cargar Gameplays");

        loadGameplaysButton.addActionListener(e -> {
            // Pasa las instancias correctas de ViewManager y YoutubeApiService al constructor de GameplayController
            GameplayController gameplayController = new GameplayController(youtubeApiService);
            List<VideoData> videos = gameplayController.fetchGameplays(game.getName());

            // Mostrar los videos en el panel
            contentPanel.removeAll();
            for (VideoData video : videos) {
                JPanel videoPanel = new JPanel(new BorderLayout());

                // Miniatura
                try {
                    ImageIcon thumbnailIcon = new ImageIcon(new URL(video.getThumbnailUrl()));
                    JLabel thumbnailLabel = new JLabel(thumbnailIcon);
                    videoPanel.add(thumbnailLabel, BorderLayout.WEST);
                } catch (Exception ex) {
                    JLabel thumbnailLabel = new JLabel("Miniatura no disponible");
                    videoPanel.add(thumbnailLabel, BorderLayout.WEST);
                }

                // Enlace
                JButton videoButton = new JButton("Abrir en navegador");
                videoButton.addActionListener(event -> {
                    try {
                        Desktop.getDesktop().browse(new URL(video.getVideoUrl()).toURI());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, "Error al abrir el enlace: " + ex.getMessage());
                    }
                });
                videoPanel.add(videoButton, BorderLayout.CENTER);

                contentPanel.add(videoPanel);
            }
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        buttonPanel.add(loadGameplaysButton);
        buttonPanel.add(removeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
}
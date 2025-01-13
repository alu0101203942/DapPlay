package src.Vista.PanelF;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import src.Controlador.GameplayController;
import src.Modelo.Data.GameplayModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

public class GameplayPanel implements PanelFactoryGame {
    private final GameplayController gameplayController;

    public GameplayPanel(GameplayController gameplayController) {
        this.gameplayController = gameplayController;
    }

//    @Override
//    public JPanel createPanel(Game game, ActionListener removeAction) {
//        // Crear el panel principal
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(Color.GRAY, 1),
//                BorderFactory.createEmptyBorder(10, 10, 10, 10)
//        ));
//
//        // Agregar el nombre del juego en la parte superior
//        JLabel gameNameLabel = new JLabel(game.getName());
//        gameNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
//        panel.add(gameNameLabel, BorderLayout.NORTH);
//
//        // Crear un sub-panel para la miniatura y los enlaces
//        JPanel contentPanel = new JPanel();
//        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
//        JScrollPane scrollPane = new JScrollPane(contentPanel);
//        panel.add(scrollPane, BorderLayout.CENTER);
//
//        // Botones en la parte inferior
//        JPanel buttonPanel = new JPanel();
//        JButton removeButton = new JButton("Eliminar");
//        removeButton.addActionListener(removeAction);
//
//        JButton loadGameplaysButton = new JButton("Cargar Gameplays");
//
//        loadGameplaysButton.addActionListener(e -> {
//            List<GameplayModel> gameplayModels = gameplayController.fetchGameplays(game.getName());
//            updateContentPanel(contentPanel, gameplayModels);
//        });
//
//        buttonPanel.add(loadGameplaysButton);
//        buttonPanel.add(removeButton);
//        panel.add(buttonPanel, BorderLayout.SOUTH);
//
//        return panel;
//    }
//
//    private void updateContentPanel(JPanel contentPanel, List<GameplayModel> gameplayModels) {
//        contentPanel.removeAll();
//
//        if (gameplayModels.isEmpty()) {
//            JLabel noResultsLabel = new JLabel("No se encontraron gameplays para este juego.");
//            noResultsLabel.setHorizontalAlignment(SwingConstants.CENTER);
//            contentPanel.add(noResultsLabel);
//        } else {
//            for (GameplayModel model : gameplayModels) {
//                JPanel videoPanel = new JPanel(new BorderLayout());
//                videoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//
//                // Miniatura
//                try {
//                    ImageIcon thumbnailIcon = new ImageIcon(new URL(model.getThumbnailUrl()));
//                    JLabel thumbnailLabel = new JLabel(thumbnailIcon);
//                    videoPanel.add(thumbnailLabel, BorderLayout.WEST);
//                } catch (Exception ex) {
//                    JLabel thumbnailLabel = new JLabel("Miniatura no disponible");
//                    videoPanel.add(thumbnailLabel, BorderLayout.WEST);
//                }
//
//                // Botón para abrir el video
//                JButton videoButton = new JButton("Abrir en navegador");
//                videoButton.addActionListener(event -> {
//                    try {
//                        Desktop.getDesktop().browse(new URL(model.getVideoUrl()).toURI());
//                    } catch (Exception ex) {
//                        JOptionPane.showMessageDialog(contentPanel, "Error al abrir el enlace: " + ex.getMessage());
//                    }
//                });
//                videoPanel.add(videoButton, BorderLayout.CENTER);
//
//                contentPanel.add(videoPanel);
//            }
//        }
//
//        contentPanel.revalidate();
//        contentPanel.repaint();
//    }
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
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botones en la parte inferior
        JPanel buttonPanel = new JPanel();
        JButton removeButton = new JButton("Eliminar");
        removeButton.addActionListener(removeAction);

        JButton loadGameplaysButton = new JButton("Cargar Gameplays");

        loadGameplaysButton.addActionListener(e -> {
            List<GameplayModel> gameplayModels = gameplayController.fetchGameplays(game.getName());
            displayGameplayLinksWithThumbnails(gameplayModels, contentPanel);
        });

        buttonPanel.add(loadGameplaysButton);
        buttonPanel.add(removeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    public void displayGameplayLinksWithThumbnails(List<GameplayModel> videoDataList, JPanel gameplayPanel) {
        gameplayPanel.removeAll(); // Limpiar contenido anterior

        // Crear un panel interno con BoxLayout para alinear elementos verticalmente
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        for (GameplayModel video : videoDataList) {
            JPanel videoPanel = new JPanel(new BorderLayout());
            videoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Espaciado entre elementos

            // Miniatura
            JLabel thumbnailLabel = new JLabel(video.getScaledThumbnail(100, 100));
            videoPanel.add(thumbnailLabel, BorderLayout.WEST);

            // Botón de enlace
            JButton videoButton = new JButton("Open in browser");
            videoButton.addActionListener(event -> {
                try {
                    Desktop.getDesktop().browse(new URL(video.getVideoUrl()).toURI());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(gameplayPanel, "Error opening link: " + ex.getMessage());
                }
            });
            videoPanel.add(videoButton, BorderLayout.CENTER);

            // Agregar el panel del video al panel de contenido
            contentPanel.add(videoPanel);
        }

        // Agregar el panel de contenido a un JScrollPane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Reemplazar el contenido del gameplayPanel con el JScrollPane
        gameplayPanel.setLayout(new BorderLayout());
        gameplayPanel.add(scrollPane, BorderLayout.CENTER);

        gameplayPanel.revalidate(); // Refrescar el panel
        gameplayPanel.repaint();
    }


}

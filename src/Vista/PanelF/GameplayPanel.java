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

    @Override
    public JPanel createPanel(Game game, ActionListener removeAction) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JLabel gameNameLabel = new JLabel(game.getName());
        gameNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(gameNameLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

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
        gameplayPanel.removeAll();
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        for (GameplayModel video : videoDataList) {
            JPanel videoPanel = new JPanel(new BorderLayout());
            videoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            JLabel thumbnailLabel = new JLabel();
            ImageIcon thumbnail = video.getScaledThumbnail(100, 100);
            if (thumbnail != null) {
                thumbnailLabel.setIcon(thumbnail);
            } else {
                thumbnailLabel.setText("Thumbnail not available");
            }
            videoPanel.add(thumbnailLabel, BorderLayout.WEST);

            JButton videoButton = new JButton("Open in browser");
            videoButton.addActionListener(event -> {
                try {
                    Desktop.getDesktop().browse(new URL(video.getVideoUrl()).toURI());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(gameplayPanel, "Error opening link: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            videoPanel.add(videoButton, BorderLayout.CENTER);

            contentPanel.add(videoPanel);
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        gameplayPanel.setLayout(new BorderLayout());
        gameplayPanel.add(scrollPane, BorderLayout.CENTER);
        gameplayPanel.revalidate();
        gameplayPanel.repaint();
    }
}

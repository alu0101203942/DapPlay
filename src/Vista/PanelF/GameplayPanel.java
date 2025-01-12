package src.Vista.PanelF;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import src.Controlador.GameplayController;
import src.Modelo.API.YoutubeApiService;
import src.Modelo.Data.FavoritesObserver;
import src.Modelo.Data.VideoData;
import src.Vista.ViewManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

public class GameplayPanel implements PanelFactoryGame, FavoritesObserver {
    private YoutubeApiService youtubeApiService;
    private ViewManager viewManager;
    private JPanel contentPanel;
    private String gameName;

    public GameplayPanel(ViewManager viewManager, YoutubeApiService youtubeApiService) {
        this.viewManager = viewManager;
        this.youtubeApiService = youtubeApiService;
    }

    @Override
    public JPanel createPanel(Game game, ActionListener removeAction) {
        this.gameName = game.getName(); // Guardar el nombre del juego

        // Crear el panel principal
        JPanel panel = new JPanel(new BorderLayout());
        contentPanel = new JPanel(new GridLayout(0, 1)); // Inicializar contentPanel
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Configurar botón para cargar gameplays
        JButton loadGameplaysButton = new JButton("Cargar Gameplays");
        loadGameplaysButton.addActionListener(e -> loadGameplays());
        panel.add(loadGameplaysButton, BorderLayout.SOUTH);

        return panel;
    }

    private void loadGameplays() {
        // Lógica para cargar videos
        GameplayController gameplayController = new GameplayController(viewManager, youtubeApiService);
        List<VideoData> videos = gameplayController.fetchGameplays(gameName);

        contentPanel.removeAll();
        for (VideoData video : videos) {
            JLabel label = new JLabel(video.getVideoUrl());
            contentPanel.add(label);
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    @Override
    public void onFavoritesUpdated(List<Game> favoriteGames) {
        // Si este juego está en los favoritos, recargar gameplays
        for (Game favorite : favoriteGames) {
            if (favorite.getName().equals(gameName)) {
                loadGameplays();
            }
        }
    }
}

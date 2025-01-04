package src.Controlador;

import src.Modelo.API.YoutubeApiService;
import src.Vista.DashboardView;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import src.Vista.ViewManager;

import javax.swing.*;

public class GameplayController {
    private final ViewManager viewManager;
    private final YoutubeApiService youtubeApiService;

    public GameplayController(ViewManager viewManager, YoutubeApiService youtubeApiService) {
        this.viewManager = viewManager;
        this.youtubeApiService = youtubeApiService;

        if (youtubeApiService == null) {
            System.out.println("Error: youtubeApiService es null en GameplayController");
        }
    }


    public void viewGameplay(Game game) {
        try {
            String jsonResponse = youtubeApiService.searchVideosByGame(game);
            String videoId = youtubeApiService.extractVideoId(jsonResponse);

            if (videoId != null) {
                String videoUrl = "https://www.youtube.com/embed/" + videoId;
                viewManager.playGameplay(videoUrl);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron gameplays para este juego.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar gameplay: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

package src.Controlador;

import src.Modelo.API.YoutubeApiService;
import src.Modelo.Data.GameplayModel;

import java.util.List;

public class GameplayController {
    private final YoutubeApiService youtubeApiService;

    public GameplayController(YoutubeApiService youtubeApiService) {
        this.youtubeApiService = youtubeApiService;
    }

    public List<GameplayModel> fetchGameplays(String gameName) {
        try {
            return youtubeApiService.searchLatestVideosByGame(gameName);
        } catch (Exception e) {
            System.err.println("Error fetching gameplays: " + e.getMessage());
            return List.of();
        }
    }
}
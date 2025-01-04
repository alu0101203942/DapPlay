package src.Controlador;

import src.Modelo.API.YoutubeApiService;
import src.Vista.ViewManager;


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
}

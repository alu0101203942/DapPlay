package src.Controlador;

import src.Modelo.API.YoutubeApiService;
import src.Modelo.Data.VideoData;
import src.Vista.ViewManager;

import java.util.ArrayList;
import java.util.List;

public class GameplayController {
    private YoutubeApiService youtubeApiService;
    private ViewManager viewManager;

    public GameplayController(ViewManager viewManager, YoutubeApiService youtubeApiService) {
        this.viewManager = viewManager;
        this.youtubeApiService = youtubeApiService;
    }

    public List<VideoData> fetchGameplays(String gameName) {
        List<VideoData> videoDataList = new ArrayList<>();
        try {
            List<VideoData> videos = youtubeApiService.searchLatestVideosByGame(gameName);
            videoDataList.addAll(videos);
        } catch (Exception e) {
            System.err.println("Error al buscar gameplays: " + e.getMessage());
        }
        return videoDataList;
    }

}

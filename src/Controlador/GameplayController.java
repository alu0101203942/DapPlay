package src.Controlador;

import src.Modelo.API.YoutubeApiService;
import src.Modelo.Data.VideoData;
import src.Vista.ViewManager;

import java.util.ArrayList;
import java.util.List;

public class GameplayController {
    private final YoutubeApiService youtubeApiService;

    public GameplayController(YoutubeApiService youtubeApiService) {
        this.youtubeApiService = youtubeApiService;
    }

    public List<VideoData> fetchGameplays(String gameName) {
        try {
            return youtubeApiService.searchLatestVideosByGame(gameName);
        } catch (Exception e) {
            System.err.println("Error al buscar gameplays: " + e.getMessage());
            return List.of();
        }
    }
//  public void viewGameplay(Game game) {
//        try {
//            List<VideoData> videos = youtubeApiService.searchLatestVideosByGame(game.getName());
//
//            if (!videos.isEmpty()) {
//                viewManager.displayGameplayLinksWithThumbnails(videos, dashboardView.gameplayPanel);
//            } else {
//                JOptionPane.showMessageDialog(dashboardView.frame, "No gameplays found for this game.", "Information", JOptionPane.INFORMATION_MESSAGE);
//            }
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(dashboardView.frame, "Error fetching gameplays: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
}

//public class GameplayController {
//    private YoutubeApiService youtubeApiService;
//    private ViewManager viewManager;
//
//    public GameplayController(ViewManager viewManager, YoutubeApiService youtubeApiService) {
//        this.viewManager = viewManager;
//        this.youtubeApiService = youtubeApiService;
//    }
//
//    public List<VideoData> fetchGameplays(String gameName) {
//        List<VideoData> videoDataList = new ArrayList<>();
//        try {
//            List<VideoData> videos = youtubeApiService.searchLatestVideosByGame(gameName);
//            videoDataList.addAll(videos);
//        } catch (Exception e) {
//            System.err.println("Error al buscar gameplays: " + e.getMessage());
//        }
//        return videoDataList;
//    }
//
//}

package src;


import src.Controlador.StartController;
import src.Modelo.API.SteamApiService;
import src.Modelo.API.YoutubeApiService;
import src.Vista.MainViews.StartView;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting application...");
        SteamApiService service = SteamApiService.getInstance("06166564FA99EDCBCEDAFFF71732218B");
        YoutubeApiService youtubeApiService = YoutubeApiService.getInstance("AIzaSyBHO-ZRnVGVumSC6XxQg1n9fEQiohF0QqI");
        StartView startView = new StartView();
        new StartController(service, startView, youtubeApiService);
        startView.show();
    }
}
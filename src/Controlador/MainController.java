package src.Controlador;

import src.Modelo.SteamApiService;
import src.Vista.MainView;


public class MainController {
    private final SteamApiService steamApiService;
    private final MainView mainView;

    public MainController(SteamApiService steamApiService, MainView mainView) {
        this.steamApiService = steamApiService;
        this.mainView = mainView;
    }
}

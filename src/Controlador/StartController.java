package src.Controlador;

import src.Modelo.API.YoutubeApiService;
import src.Modelo.Data.FavoritesManager;
import src.Modelo.Data.UserModel;
import src.Modelo.Sort.SortByPlaytime;
import src.Modelo.Sort.SortStrategy;
import src.Modelo.API.SteamApiService;
import src.Vista.MainViews.DashboardView;
import src.Vista.MainViews.StartView;

public class StartController {
    private final SteamApiService steamApiService;
    private final StartView startView;
    private final YoutubeApiService youtubeApiService;

    public StartController(SteamApiService service, StartView startView, YoutubeApiService youtubeApiService) {
        this.steamApiService = service;
        this.startView = startView;
        this.youtubeApiService = youtubeApiService;

        startView.nextButton.addActionListener(e -> showDashboard());
    }

    private void showDashboard() {
        String username = startView.usernameField.getText();
        if (username.isEmpty()) {
            startView.showError("Por favor, ingrese un nombre de usuario.");
            return;
        }
        startView.hide();

        DashboardView dashboardView = new DashboardView();
        FavoritesManager favoritesManager = new FavoritesManager();
        SortStrategy sortStrategy = new SortByPlaytime();
        UserModel userModel = new UserModel(steamApiService);
        DashboardController dashboardController = new DashboardController(
                steamApiService, favoritesManager, dashboardView, sortStrategy, username, youtubeApiService, userModel
        );
        dashboardView.openNewDashboardButton.addActionListener(e -> openNewDashboard());
        dashboardView.show();
    }

    private void openNewDashboard() {
        StartView newStartView = new StartView();
        new StartController(steamApiService, newStartView, youtubeApiService);
        newStartView.show();
    }
}
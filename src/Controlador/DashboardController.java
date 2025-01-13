package src.Controlador;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import src.Modelo.API.YoutubeApiService;
import src.Modelo.Data.*;
import src.Modelo.Sort.SortStrategy;
import src.Modelo.API.SteamApiService;
import src.Vista.MainViews.DashboardView;
import src.Vista.PanelF.GameplayPanel;
import src.Vista.ViewManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardController {
    private final SteamApiService steamApiService;
    private final FavoritesManager favoritesManager;
    private final SortStrategy sortStrategy;
    private final String username;
    private final ViewManager viewManager;
    private final YoutubeApiService youtubeApiService;
    private final UserModel user;
    private DashboardView dashboardView;
    private AchievementsController achievementsController;
    private GameplayController gameplayController;
    private UserController userController;

    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;
    private List<Game> games = new ArrayList<>();

    public DashboardController(SteamApiService service, FavoritesManager favoritesManager, DashboardView view, SortStrategy sortStrategy, String username, YoutubeApiService youtubeApiService, UserModel user) {
        this.steamApiService = service;
        this.favoritesManager = favoritesManager;
        this.sortStrategy = sortStrategy;
        this.username = username;
        this.youtubeApiService = youtubeApiService;
        this.user = user;
        this.dashboardView = view; // Initialize dashboardView

        // Create GameplayController
        this.gameplayController = new GameplayController(youtubeApiService); // Initialize class-level variable
        achievementsController = new AchievementsController(steamApiService);
        new ChartController(dashboardView, favoritesManager);
        userController = new UserController(view, user);

        // Create ViewManager with DashboardController and GameplayController
        this.viewManager = new ViewManager(view, this, achievementsController, youtubeApiService);

        fetchAndDisplayUserInfo();
        fetchGames();

        // Setup listeners
        setupListeners(view);
    }

    private void updateChart() {
        String selectedType = (String) dashboardView.chartTypeComboBox.getSelectedItem();
        //viewManager.updateChart(selectedType, favoritesManager.getFavoriteGames());
    }

    public void fetchAndDisplayUserInfo() {
        try {
            user.loadUserData(username);
            userController.displayUserInfo();
        } catch (Exception e) {
            viewManager.showError("Error al cargar información del usuario: " + e.getMessage());
        }
    }


    private void fetchGames() {
        try {
            String steamId64;
            if (user.isSteamId64(username)) {
                steamId64 = username;
            } else {
                steamId64 = steamApiService.getSteamIdFromUsername(username);
            }
            games = steamApiService.getOwnedGames(steamId64);
            currentPage = 0;
            displayPage();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error fetching games: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void fetchAchievements(String steamId64, Game selectedGame) {
        achievementsController.fetchAchievements(steamId64, dashboardView, selectedGame, viewManager);
    }
    private void setupListeners(DashboardView view) {
        view.nextButton.addActionListener(e -> nextPage());
        view.prevButton.addActionListener(e -> prevPage());
        view.chartTypeComboBox.addActionListener(e -> updateChart());
    }

    private void nextPage() {
        if ((currentPage + 1) * PAGE_SIZE < games.size()) {
            currentPage++;
            viewManager.displayGames(games, currentPage, PAGE_SIZE, favoritesManager);
        }
    }

    private void prevPage() {
        if (currentPage > 0) {
            currentPage--;
            viewManager.displayGames(games, currentPage, PAGE_SIZE, favoritesManager);
        }
    }

    public String getUsername() {
        return username;
    }

    private void displayPage() {
        sortStrategy.sort(games);
        viewManager.displayGames(games, currentPage, PAGE_SIZE, favoritesManager);
    }

    public void viewGameplay(Game game) {
        try {
            // Llamar al servicio de YouTube y obtener gameplays
            List<GameplayModel> gameplays = youtubeApiService.searchLatestVideosByGame(game.getName());
            if (!gameplays.isEmpty()) {
                // Actualizar la vista con los gameplays
                viewManager.updateGameplayPanel(gameplays);
            } else {
                viewManager.showError("No se encontraron gameplays para este juego.");
            }
        } catch (Exception e) {
            viewManager.showError("Error al cargar gameplays: " + e.getMessage());
        }
    }

}
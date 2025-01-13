package src.Controlador;

import com.lukaspradel.steamapi.data.json.friendslist.Friend;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import src.Modelo.API.YoutubeApiService;
import src.Modelo.Data.*;
import src.Modelo.Sort.*;
import src.Modelo.API.SteamApiService;
import src.Vista.MainViews.DashboardView;
import src.Vista.ViewManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {
    private final SteamApiService steamApiService;
    private final FavoritesManager favoritesManager;
    private  SortStrategy sortStrategy;
    private final String username;
    private final ViewManager viewManager;
    private final YoutubeApiService youtubeApiService;
    private final UserModel user;
    private DashboardView dashboardView;
    private AchievementsController achievementsController;
    private GameplayController gameplayController;
    private UserController userController;
    private ChartController chartController;

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
        this.dashboardView = view;

        // Create GameplayController
        this.gameplayController = new GameplayController(youtubeApiService); // Initialize class-level variable
        achievementsController = new AchievementsController(steamApiService);
        new ChartController(dashboardView, favoritesManager);
        userController = new UserController(view, user);
        chartController = new ChartController(view, favoritesManager);

        // Create ViewManager with DashboardController and GameplayController
        this.viewManager = new ViewManager(view, this, achievementsController, youtubeApiService);
        // Crear ViewManager con DashboardController y GameplayController
        favoritesManager.addObserver(updatedGames -> viewManager.updateFavorites(updatedGames, favoritesManager));

        fetchAndDisplayUserInfo();
        fetchGames();
        fetchFriends();

        // Setup listeners
        setupListeners(view);
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
    private void fetchFriends() {
        try {
            String steamId64;
            if (user.isSteamId64(username)) {
                steamId64 = username;
            } else {
                steamId64 = steamApiService.getSteamIdFromUsername(username);
            }
            List<Friend> friends = steamApiService.getFriends(steamId64);
            List<Player> players = steamApiService.getPlayerSummaries(friends.toString());
            viewManager.displayFriends(players);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardView.frame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void setupListeners(DashboardView view) {
        view.nextButton.addActionListener(e -> nextPage());
        view.prevButton.addActionListener(e -> prevPage());
        view.chartTypeComboBox.addActionListener(e -> chartController.updateChart());
        view.sortComboBox.addActionListener(e -> updateSortStrategy());
    }

    private void updateSortStrategy() {
        String selectedStrategy = (String) dashboardView.sortComboBox.getSelectedItem();
        if ("Sort by Name".equals(selectedStrategy)) {
            sortStrategy = new SortByName();
        } else if ("Sort by Playtime".equals(selectedStrategy)) {
            sortStrategy = new SortByPlaytime();
        }
        displayPage();
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
            List<GameplayModel> gameplays = youtubeApiService.searchLatestVideosByGame(game.getName());
            if (!gameplays.isEmpty()) {
                viewManager.updateGameplayPanel(gameplays);
            } else {
                viewManager.showError("No se encontraron gameplays para este juego.");
            }
        } catch (Exception e) {
            viewManager.showError("Error al cargar gameplays: " + e.getMessage());
        }
    }

}
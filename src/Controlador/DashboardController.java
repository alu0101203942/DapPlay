package src.Controlador;

import com.lukaspradel.steamapi.data.json.friendslist.Friend;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import src.Modelo.API.YoutubeApiService;
import src.Modelo.Data.*;
import src.Modelo.Sort.SortStrategy;
import src.Modelo.API.SteamApiService;
import src.Vista.MainViews.DashboardView;
import src.Vista.ViewManager;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {
    private final SteamApiService steamApiService;
    private final FavoritesManager favoritesManager;
    private final SortStrategy sortStrategy;
    private final String username;
    private final ViewManager viewManager;
    private final YoutubeApiService youtubeApiService;
    private final UserModel user;
    private  DashboardView dashboardView;
    private AchievementsController achievementsController;


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

        // Crear GameplayController
        GameplayController gameplayController = new GameplayController(youtubeApiService);
        achievementsController = new AchievementsController(steamApiService);
        // Crear ViewManager con DashboardController y GameplayController
        this.viewManager = new ViewManager(view, this, achievementsController);
        favoritesManager.addObserver(updatedGames -> viewManager.updateFavorites(updatedGames, favoritesManager));

        fetchAndDisplayUserInfo();
        fetchGames();
        fetchFriends();


        // Configurar listeners
        setupListeners(view);
    }

    private void updateChart() {
        String selectedType = (String) dashboardView.chartTypeComboBox.getSelectedItem();
        viewManager.updateChart(selectedType, favoritesManager.getFavoriteGames());
    }

    private void fetchAndDisplayUserInfo() {
        UserController userController = new UserController(user, viewManager);
        userController.fetchAndDisplayUserInfo(username);
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

}
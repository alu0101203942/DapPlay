package src.Controlador;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import com.lukaspradel.steamapi.data.json.friendslist.Friend;
import src.Modelo.FavoritesManager;
import src.Modelo.SortByName;
import src.Modelo.SortByPlaytime;
import src.Modelo.SortStrategy;
import src.Modelo.API.SteamApiService;
import src.Vista.DashboardView;
import src.Vista.UserPanelFactory;
import src.Vista.ViewManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardController {
    private final SteamApiService steamApiService;
    private final FavoritesManager favoritesManager;
    private SortStrategy sortStrategy;
    private final String username;
    private final ViewManager viewManager;
    private final DashboardView dashboardView;
    private List<Game> games;

    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;

    public DashboardController(SteamApiService service, FavoritesManager favoritesManager, DashboardView view, SortStrategy sortStrategy, String username) {
        this.steamApiService = service;
        this.favoritesManager = favoritesManager;
        this.sortStrategy = sortStrategy;
        this.username = username;
        this.viewManager = new ViewManager(view);
        this.dashboardView = view;

        favoritesManager.addObserver(updatedGames -> viewManager.updateFavorites(updatedGames, favoritesManager));
        fetchAndDisplayUserInfo();
        fetchGames();
        fetchFriends();
        setupListeners(view);
    }

    private boolean isSteamId64(String input) {
        return input.matches("\\d{17}");
    }

    private void fetchGames() {
        try {
            String steamId64;
            if (isSteamId64(username)) {
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

    private void fetchAndDisplayUserInfo() {
        try {
            String steamId64 = isSteamId64(username) ? username : steamApiService.getSteamIdFromUsername(username);
            List<Player> userInfoList = steamApiService.getPlayerSummaries(steamId64);

            if (!userInfoList.isEmpty()) {
                Player userInfo = userInfoList.get(0);
                int gamesCount = steamApiService.getOwnedProductsCount(steamId64);

                UserPanelFactory userPanelFactory = new UserPanelFactory();
                JPanel userPanel = userPanelFactory.createPanel(userInfo, gamesCount);

                dashboardView.userPanel.removeAll();
                dashboardView.userPanel.add(userPanel, BorderLayout.CENTER);
                dashboardView.userPanel.revalidate();
                dashboardView.userPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(dashboardView.frame, "No se encontró información del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardView.frame, "Error al cargar información del usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void fetchFriends() {
        try {
            String steamId64;
            if (isSteamId64(username)) {
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


    private void displayPage() {
        sortStrategy.sort(games);
        viewManager.displayGames(games, currentPage, PAGE_SIZE, favoritesManager);
    }


    private void setupListeners(DashboardView view) {
        view.nextButton.addActionListener(e -> nextPage());
        view.prevButton.addActionListener(e -> prevPage());
        view.chartTypeComboBox.addActionListener(e -> updateChart());
        view.sortComboBox.addActionListener(e -> updateSort());
    }

    private void nextPage() {
        if ((currentPage + 1) * PAGE_SIZE < games.size()) {
            currentPage++;
            displayPage();
        }
    }

    private void prevPage() {
        if (currentPage > 0) {
            currentPage--;
            displayPage();
        }
    }

    private void updateChart() {
        String selectedType = (String) dashboardView.chartTypeComboBox.getSelectedItem();
        viewManager.updateChart(selectedType, favoritesManager.getFavoriteGames());
    }

    private void updateSort() {
        String selectedType = (String) dashboardView.sortComboBox.getSelectedItem();
        switch (selectedType) {
            case "Sort by Playtime":
                sortStrategy = new SortByPlaytime();
                break;
            case "Sort by Name":
                sortStrategy = new SortByName();
                break;
            case null:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + selectedType);
        }
        displayPage();
    }
}

package src.Controlador;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import com.lukaspradel.steamapi.data.json.playerachievements.Achievement;
import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import com.lukaspradel.steamapi.data.json.friendslist.Friend;
import src.Modelo.FavoritesManager;
import src.Modelo.SortByName;
import src.Modelo.SortByPlaytime;
import src.Modelo.SortStrategy;
import src.Modelo.API.SteamApiService;
import src.Vista.DashboardView;
import src.Vista.ViewManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        this.viewManager = new ViewManager(view, this);
        this.dashboardView = view;

        favoritesManager.addObserver(updatedGames -> viewManager.updateFavorites(updatedGames, favoritesManager));

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

    public void fetchAchievements(Game selectedGame) {
        try {
            String steamId64 = isSteamId64(username) ? username : steamApiService.getSteamIdFromUsername(username);
            List<Map<String, Object>> rawAchievements = SteamApiService.fetchAchievements(
                    steamId64,
                    String.valueOf(selectedGame.getAppid()),
                    steamApiService.getApiKey()
            );
            List<Map<String, String>> achievementDetails = steamApiService.fetchAchievementDetails(String.valueOf(selectedGame.getAppid()), steamApiService.getApiKey());
            for (Map<String, Object> rawAchievement : rawAchievements) {
                System.out.println("Logro del usuario: " + rawAchievement.get("apiname"));
            }

            for (Map<String, String> detail : achievementDetails) {
                System.out.println("Detalle del logro: " + detail.get("name"));
            }
            List<Map<String, Object>> unlockedAchievements = new ArrayList<>();
            List<Map<String, Object>> lockedAchievements = new ArrayList<>();

            for (Map<String, Object> rawAchievement : rawAchievements) {
                String apiname = (String) rawAchievement.get("apiname");
                int achieved = (int) rawAchievement.get("achieved");

                if (apiname == null) {
                    System.out.println("Logro con 'apiname' nulo encontrado, ignorando...");
                    continue;
                }

                Map<String, String> details = achievementDetails.stream()
                        .filter(d -> apiname.equalsIgnoreCase(d.get("name")))
                        .findFirst()
                        .orElse(null);

                if (details != null) {
                    Map<String, Object> achievement = new HashMap<>();
                    achievement.put("apiname", apiname);
                    achievement.put("achieved", achieved);
                    achievement.put("displayName", details.get("displayName"));
                    achievement.put("description", details.get("description"));
                    achievement.put("icon", details.get("icon"));
                    achievement.put("iconGray", details.get("icongray"));

                    if (achieved == 1) {
                        unlockedAchievements.add(achievement);
                    } else {
                        lockedAchievements.add(achievement);
                    }
                } else {
                    System.out.println("No se encontraron detalles para el logro: " + apiname);
                }
            }
            viewManager.displayAchievements(unlockedAchievements, lockedAchievements);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardView.frame,
                    "Error loading achievements for " + selectedGame.getName() + ": " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
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

        view.gamesPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int index = evt.getY() / 200;
                if (index >= 0 && index < games.size()) {
                    Game selectedGame = games.get(index);
                    fetchAchievements(selectedGame);
                }
            }
        });

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

package src.Controlador;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import com.lukaspradel.steamapi.data.json.friendslist.Friend;
import src.Modelo.API.YoutubeApiService;
import src.Modelo.FavoritesManager;
import src.Modelo.Sort.SortByName;
import src.Modelo.Sort.SortByPlaytime;
import src.Modelo.Sort.SortStrategy;
import src.Modelo.API.SteamApiService;
import src.Modelo.VideoData;
import src.Vista.DashboardView;
import src.Vista.UserPanelFactory;
import src.Vista.ViewManager;

import javax.swing.*;
import java.awt.*;
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
    private YoutubeApiService youtubeApiService;

    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;

    public DashboardController(SteamApiService service, FavoritesManager favoritesManager, DashboardView view, SortStrategy sortStrategy, String username, YoutubeApiService youtubeApiService) {
        this.steamApiService = service;
        this.favoritesManager = favoritesManager;
        this.sortStrategy = sortStrategy;
        this.username = username;
        this.dashboardView = view;
        this.youtubeApiService = youtubeApiService;

        // Crear ViewManager primero
        this.viewManager = new ViewManager(view, this);

        // Crear GameplayController usando ViewManager
        GameplayController gameplayController = new GameplayController(viewManager, youtubeApiService);

        // Pasar GameplayController a ViewManager para completar la relación
        this.viewManager.setGameplayController(gameplayController);

        // Registrar los observadores
        favoritesManager.addObserver(updatedGames -> viewManager.updateFavorites(updatedGames, favoritesManager));

        // Inicializar la interfaz
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

    // En DashboardController.java
    public void viewGameplay(Game game) {
        try {
            List<VideoData> videos = youtubeApiService.searchLatestVideosByGame(game.getName());

            if (!videos.isEmpty()) {
                viewManager.displayGameplayLinksWithThumbnails(videos, dashboardView.gameplayPanel);
            } else {
                JOptionPane.showMessageDialog(dashboardView.frame, "No gameplays found for this game.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dashboardView.frame, "Error fetching gameplays: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String extractVideoId(String jsonResponse) {
        // Implement the logic to extract video ID from the JSON response
        // This is a placeholder implementation
        return "extractedVideoId";
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

                    // Mostrar logros del juego
                    fetchAchievements(selectedGame);

                    // Ver gameplay del juego
                    viewGameplay(selectedGame);
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

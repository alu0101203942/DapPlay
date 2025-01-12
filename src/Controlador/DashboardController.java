package src.Controlador;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import com.lukaspradel.steamapi.data.json.friendslist.Friend;
import src.Modelo.API.YoutubeApiService;
import src.Modelo.Data.FavoritesManager;
import src.Modelo.Sort.SortByName;
import src.Modelo.Sort.SortByPlaytime;
import src.Modelo.Sort.SortStrategy;
import src.Modelo.API.SteamApiService;
import src.Modelo.Data.VideoData;
import src.Vista.MainViews.DashboardView;
import src.Vista.PanelF.UserPanelFactory;
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
    private final AchievementsController achievementsController;

    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;
    private List<Game> games = new ArrayList<>();



    public DashboardController(SteamApiService service, FavoritesManager favoritesManager, DashboardView view, SortStrategy sortStrategy, String username, YoutubeApiService youtubeApiService, UserModel user) {
        this.steamApiService = service;
        this.favoritesManager = favoritesManager;
        this.sortStrategy = sortStrategy;
        this.username = username;
        this.dashboardView = view;
        this.youtubeApiService = youtubeApiService;

        achievementsController = new AchievementsController(steamApiService);
        // Crear ViewManager primero
        this.viewManager = new ViewManager(view, this, achievementsController);
        this.user = user;

        // Crear GameplayController
        GameplayController gameplayController = new GameplayController(youtubeApiService);

        // Crear ViewManager con DashboardController y GameplayController
        this.viewManager = new ViewManager(view, this, gameplayController);

        // Registrar los observadores
        favoritesManager.addObserver(updatedGames -> viewManager.updateFavorites(updatedGames, favoritesManager));

        // Inicializar la interfaz
        fetchAndDisplayUserInfo();
        fetchGames();
        fetchFriends();
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

    public String getUsername() {
        return username;
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

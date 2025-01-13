package src.Vista;

import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import src.Controlador.AchievementsController;
import src.Controlador.GameplayController;
import src.Controlador.DashboardController;
import src.Modelo.API.YoutubeApiService;
import src.Modelo.Data.FavoritesManager;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import src.Modelo.Data.GameplayModel;
import src.Modelo.Data.UserModel;
import src.Vista.MainViews.DashboardView;
import src.Vista.PanelF.*;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

public class ViewManager {
    private final DashboardView dashboardView;
    private final DashboardController dashboardController;
    private GameplayController gameplayController;
    private final AchievementsController achievementsController;

    public ViewManager(DashboardView view, DashboardController dashboardController, AchievementsController achievementsController, YoutubeApiService youtubeApiService) {
        this.dashboardView = view;
        this.dashboardController = dashboardController;
        this.gameplayController = new GameplayController(youtubeApiService);
        this.achievementsController = achievementsController;
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(dashboardView.frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void displayUserInfo(UserModel user) {
        UserPanelFactory userPanelFactory = new UserPanelFactory();
        JPanel userPanel = userPanelFactory.createPanel(user);
        dashboardView.userPanel.removeAll();
        dashboardView.userPanel.add(userPanel, BorderLayout.CENTER);
        dashboardView.userPanel.revalidate();
        dashboardView.userPanel.repaint();
    }

    public void displayAchievements(List<Map<String, Object>> unlockedAchievements, List<Map<String, Object>> lockedAchievements) throws MalformedURLException {
        dashboardView.achievementsPanel.removeAll();
        dashboardView.achievementsPanel.setLayout(new GridLayout(1, 2)); // Dividir en dos columnas
        AchievementPanelFactory achievementPanelFactory = new AchievementPanelFactory();
        JPanel unlockedContainer = achievementPanelFactory.createPanel(unlockedAchievements, "Unlocked Achievements", Color.GREEN);
        dashboardView.achievementsPanel.add(unlockedContainer);
        JPanel lockedContainer = achievementPanelFactory.createPanel(lockedAchievements, "Locked Achievements", Color.RED);
        dashboardView.achievementsPanel.add(lockedContainer);
        dashboardView.achievementsPanel.revalidate();
        dashboardView.achievementsPanel.repaint();
    }

    public void updateGameplayPanel(Game game) {
        List<GameplayModel> gameplays = gameplayController.fetchGameplays(game.getName());
        JPanel gameplayContent;

        if (gameplays.isEmpty()) {
            gameplayContent = new JPanel();
            JLabel noDataLabel = new JLabel("No se encontraron gameplays para este juego.");
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gameplayContent.add(noDataLabel);
        } else {
            GameplayPanel gameplayPanelFactory = new GameplayPanel(gameplayController);
            gameplayContent = gameplayPanelFactory.createPanel(game, e -> {
                dashboardView.gameplayPanel.removeAll();
                dashboardView.gameplayPanel.revalidate();
                dashboardView.gameplayPanel.repaint();
            });

        }
        dashboardView.gameplayPanel.removeAll();
        dashboardView.gameplayPanel.add(gameplayContent, BorderLayout.CENTER);
        dashboardView.gameplayPanel.revalidate();
        dashboardView.gameplayPanel.repaint();
    }

    public void displayGames(List<Game> games, int currentPage, int pageSize, FavoritesManager favoritesManager) {
        dashboardView.gamesPanel.removeAll();
        dashboardView.gamesPanel.setLayout(new BorderLayout());

        JPanel gamesListPanel = new JPanel();
        gamesListPanel.setLayout(new BoxLayout(gamesListPanel, BoxLayout.Y_AXIS));
        GamePanelFactory gamePanelFactory = new GamePanelFactory();

        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, games.size());
        for (int i = start; i < end; i++) {
            Game game = games.get(i);
            JPanel gamePanel = gamePanelFactory.createPanel(game, e -> favoritesManager.addFavorite(game));
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Organizar botones horizontalmente
            JButton achievementsButton = new JButton("View Achievements");
            achievementsButton.addActionListener(e -> {
                String steamId64 = dashboardController.getUsername(); // Obtener el Steam ID del usuario
                dashboardController.fetchAchievements(steamId64, game);
            });
            buttonsPanel.add(achievementsButton);
            JButton gameplayButton = new JButton("View Gameplay");
            gameplayButton.addActionListener(e -> dashboardController.viewGameplay(game));
            buttonsPanel.add(gameplayButton);
            gamePanel.add(buttonsPanel, BorderLayout.SOUTH);
            gamesListPanel.add(gamePanel);
        }

        JScrollPane scrollPane = new JScrollPane(gamesListPanel);
        scrollPane.setPreferredSize(new Dimension(dashboardView.gamesPanel.getWidth(), 6 * 200));
        dashboardView.gamesPanel.add(scrollPane, BorderLayout.CENTER);
        dashboardView.gamesPanel.revalidate();
        dashboardView.gamesPanel.repaint();
    }

    public void displayFriends(List<Player> friends) {
        dashboardView.friendsPanel.removeAll();
        dashboardView.friendsPanel.setLayout(new BorderLayout());

        JPanel friendsListPanel = new JPanel();
        friendsListPanel.setLayout(new BoxLayout(friendsListPanel, BoxLayout.Y_AXIS));
        FriendPanelFactory friendPanelFactory = new FriendPanelFactory();

        for (Player friend : friends) {
            JPanel friendPanel = friendPanelFactory.createPanel(friend);
            friendsListPanel.add(friendPanel);
        }

        JScrollPane scrollPane = new JScrollPane(friendsListPanel);
        scrollPane.setPreferredSize(new Dimension(dashboardView.friendsPanel.getWidth(), 4 * 100));
        dashboardView.friendsPanel.add(scrollPane, BorderLayout.CENTER);
        dashboardView.friendsPanel.revalidate();
        dashboardView.friendsPanel.repaint();
    }

    public void updateFavorites(List<Game> updatedGames, FavoritesManager favoritesManager) {
        dashboardView.favoritesPanel.removeAll();
        FavoritePanelFactory favoritePanelFactory = new FavoritePanelFactory();
        List<Game> favoriteGames = favoritesManager.getFavoriteGames();
        for (Game game : favoriteGames) {
            JPanel favoritePanel = favoritePanelFactory.createPanel(game, e -> favoritesManager.removeFavorite(game));
            dashboardView.favoritesPanel.add(favoritePanel);
        }
        dashboardView.favoritesPanel.revalidate();
        dashboardView.favoritesPanel.repaint();
    }
}
package src.Vista;


import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import src.Controlador.DashboardController;
import src.Modelo.FavoritesManager;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ViewManager {
    private final DashboardView dashboardView;
    private final DashboardController dashboardController;

    public ViewManager(DashboardView view, DashboardController dashboardController) {
        this.dashboardView = view;
        this.dashboardController = dashboardController; // Inicializar referencia
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

            // Crear panel para el juego
            JPanel gamePanel = gamePanelFactory.createPanel(game, e -> favoritesManager.addFavorite(game));

            // Agregar botón para ver logros
            JButton achievementsButton = new JButton("View Achievements");
            achievementsButton.addActionListener(e -> dashboardController.fetchAchievements(game));
            gamePanel.add(achievementsButton, BorderLayout.SOUTH);

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

    public void updateFavorites(List<Game> updatedGames, FavoritesManager favoritesManager) {
        dashboardView.favoritesPanel.removeAll();
        FavoritePanelFactory favoritePanelFactory = new FavoritePanelFactory();

        List<Game> favoriteGames = favoritesManager.getFavoriteGames();

        for (Game game : favoriteGames) {
            JPanel favoritePanel = favoritePanelFactory.createPanel(game, e -> favoritesManager.removeFavorite(game)); // Añadir lógica para eliminar
            dashboardView.favoritesPanel.add(favoritePanel);
        }
        updateChart(Objects.requireNonNull(dashboardView.chartTypeComboBox.getSelectedItem()).toString(), favoriteGames);
        dashboardView.favoritesPanel.revalidate();
        dashboardView.favoritesPanel.repaint();
    }

    public void updateChart(String chartType, List<Game> favoriteGames) {
        dashboardView.statsPanel.removeAll();
        dashboardView.statsPanel.add(dashboardView.chartTypeComboBox, BorderLayout.NORTH);

        ChartPanelFactory chartPanelFactory = new ChartPanelFactory();
        JPanel chartPanel = chartPanelFactory.createChart(chartType, favoriteGames);

        dashboardView.statsPanel.add(chartPanel, BorderLayout.CENTER);
        dashboardView.statsPanel.revalidate();
        dashboardView.statsPanel.repaint();
    }
}

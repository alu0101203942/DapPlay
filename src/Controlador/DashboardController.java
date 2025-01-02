// src/Controlador/DashboardController.java
package src.Controlador;

import src.Modelo.*;
import src.Vista.*;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import com.lukaspradel.steamapi.data.json.friendslist.Friend;

import java.awt.*;
import java.util.List;
import javax.swing.*;

public class DashboardController implements FavoritesObserver {
    private final SteamApiService steamApiService;
    private final DashboardView dashboardView;
    private final String username;
    private final SortStrategy sortStrategy;
    private List<Game> games;
    private List<Friend> friends;
    private List<Player> players;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;
    private final FavoritesManager favoritesManager;

    public DashboardController(SteamApiService service, FavoritesManager favoritesManager, DashboardView view, SortStrategy sortStrategy, String username) {
        this.steamApiService = service;
        this.favoritesManager = favoritesManager;
        this.dashboardView = view;
        this.sortStrategy = sortStrategy;
        this.username = username;

        favoritesManager.addObserver(this);

        fetchGames();
        fetchFriends();
        updateChart();

        dashboardView.nextButton.addActionListener(e -> nextPage());
        dashboardView.prevButton.addActionListener(e -> prevPage());
        dashboardView.chartTypeComboBox.addActionListener(e -> updateChartType());
    }

    private void fetchGames() {
        try {
            String steamId64 = steamApiService.getSteamIdFromUsername(username);
            games = steamApiService.getOwnedGames(steamId64);
            currentPage = 0;
            displayPage();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dashboardView.frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayPage() {
        dashboardView.gamesPanel.removeAll();
        dashboardView.gamesPanel.setLayout(new BorderLayout());
        sortStrategy.sort(games);

        JPanel gamesListPanel = new JPanel();
        gamesListPanel.setLayout(new BoxLayout(gamesListPanel, BoxLayout.Y_AXIS));
        GamePanelFactory gamePanelFactory = new GamePanelFactory();

        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, games.size());
        for (int i = start; i < end; i++) {
            Game game = games.get(i);
            JPanel gamePanel = gamePanelFactory.createPanel(game, e -> favoritesManager.addFavorite(game));
            gamesListPanel.add(gamePanel);
        }

        JScrollPane scrollPane = new JScrollPane(gamesListPanel);
        scrollPane.setPreferredSize(new Dimension(dashboardView.gamesPanel.getWidth(), 6 * 200)); // Adjust height as needed
        dashboardView.gamesPanel.add(scrollPane, BorderLayout.CENTER);

        dashboardView.gamesPanel.revalidate();
        dashboardView.gamesPanel.repaint();
    }

    @Override
    public void onFavoritesUpdated(List<Game> favoriteGames) {
        dashboardView.favoritesPanel.removeAll();
        PanelFactoryGame favoritePanelFactory = new FavoritePanelFactory();

        for (Game game : favoriteGames) {
            JPanel favoritePanel = favoritePanelFactory.createPanel(game, e -> favoritesManager.removeFavorite(game));
            dashboardView.favoritesPanel.add(favoritePanel);
        }

        dashboardView.favoritesPanel.revalidate();
        dashboardView.favoritesPanel.repaint();
        updateChart();
    }

    private void updateChart() {
        dashboardView.statsPanel.removeAll();
        dashboardView.statsPanel.add(dashboardView.chartTypeComboBox, BorderLayout.NORTH);

        String selectedType = (String) dashboardView.chartTypeComboBox.getSelectedItem();
        ChartPanelFactory chartPanelfactory = new ChartPanelFactory();
        JPanel chartPanel = chartPanelfactory.createChart(selectedType, favoritesManager.getFavoriteGames());

        dashboardView.statsPanel.add(chartPanel, BorderLayout.CENTER);
        dashboardView.statsPanel.revalidate();
        dashboardView.statsPanel.repaint();
    }

    private void updateChartType() {
        updateChart();
    }

    private void nextPage() {
        if ((currentPage + 1) * PAGE_SIZE < games.size()) {
            currentPage++;
            displayPage();
        } else {
            JOptionPane.showMessageDialog(dashboardView.frame, "No more games to display.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void prevPage() {
        if (currentPage > 0) {
            currentPage--;
            displayPage();
        } else {
            JOptionPane.showMessageDialog(dashboardView.frame, "You are on the first page.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void fetchFriends() {
        try {
            String steamId64 = steamApiService.getSteamIdFromUsername(username);
            friends = steamApiService.getFriends(steamId64);
            players = steamApiService.getPlayerSummaries(friends.toString());
            displayFriends(players);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardView.frame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayFriends(List<Player> friends) {
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
        scrollPane.setPreferredSize(new Dimension(dashboardView.friendsPanel.getWidth(), 4 * 100)); // Assuming each friend panel is 100px high
        dashboardView.friendsPanel.add(scrollPane, BorderLayout.CENTER);

        dashboardView.friendsPanel.revalidate();
        dashboardView.friendsPanel.repaint();
    }
}
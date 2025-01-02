package src.Controlador;

import src.Modelo.*;
import src.Modelo.SteamApiService;
import src.Vista.DashboardView;
import src.Vista.PanelFactory;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;

import java.awt.*;
import com.lukaspradel.steamapi.data.json.friendslist.Friend;
import com.lukaspradel.steamapi.data.json.playersummaries.*;
import java.util.List;
import javax.swing.*;


public class DashboardController implements FavoritesObserver {
    private final SteamApiService steamApiService;
    private final DashboardView dashboardView;
    private final String username;
    private List<Game> games;
    private List<Friend> friends;
    private List<Player> players;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;
    private final FavoritesManager favoritesManager;
    private SortStrategy sortStrategy;

    public DashboardController(SteamApiService service, FavoritesManager favoritesManager, DashboardView view, SortStrategy sortStrategy, String username) {
        this.steamApiService = service;
        this.favoritesManager = favoritesManager;
        this.dashboardView = view;
        this.username = username;
        this.sortStrategy = sortStrategy;

        favoritesManager.addObserver(this);

        fetchGames();
        fetchFriends();
        updateChart();

        dashboardView.nextButton.addActionListener(e -> nextPage());
        dashboardView.prevButton.addActionListener(e -> prevPage());
        dashboardView.sortComboBox.addActionListener(e -> updateSortStrategy());
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

        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, games.size());
        for (int i = start; i < end; i++) {
            Game game = games.get(i);
            JPanel gamePanel = PanelFactory.createGamePanel(game, e -> favoritesManager.addFavorite(game));
            gamesListPanel.add(gamePanel);
        }

        JScrollPane scrollPane = new JScrollPane(gamesListPanel);
        scrollPane.setPreferredSize(new Dimension(dashboardView.gamesPanel.getWidth(), 6 * 200)); // Adjust height as needed
        dashboardView.gamesPanel.add(scrollPane, BorderLayout.CENTER);

        dashboardView.gamesPanel.revalidate();
        dashboardView.gamesPanel.repaint();
    }

    private void updateChart() {
        dashboardView.statsPanel.removeAll();
        dashboardView.statsPanel.add(dashboardView.chartTypeComboBox, BorderLayout.NORTH);

        String selectedType = (String) dashboardView.chartTypeComboBox.getSelectedItem();
        JPanel chartPanel = PanelFactory.createChart(selectedType, favoritesManager.getFavoriteGames());

        dashboardView.statsPanel.add(chartPanel, BorderLayout.CENTER);
        dashboardView.statsPanel.revalidate();
        dashboardView.statsPanel.repaint();
    }

    private void updateChartType() {
        updateChart();
    }


    @Override
    public void onFavoritesUpdated(List<Game> favoriteGames) {
        dashboardView.favoritesPanel.removeAll();
        for (Game game : favoriteGames) {
            JPanel favoritePanel = PanelFactory.createFavoritePanel(game, e -> favoritesManager.removeFavorite(game));
            dashboardView.favoritesPanel.add(favoritePanel);
        }

        dashboardView.favoritesPanel.revalidate();
        dashboardView.favoritesPanel.repaint();
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

        for (Player friend : friends) {
            JPanel friendPanel = PanelFactory.createFriendPanel(friend);
            friendsListPanel.add(friendPanel);
        }

        JScrollPane scrollPane = new JScrollPane(friendsListPanel);
        scrollPane.setPreferredSize(new Dimension(dashboardView.friendsPanel.getWidth(), 4 * 100)); // Assuming each friend panel is 100px high
        dashboardView.friendsPanel.add(scrollPane, BorderLayout.CENTER);

        dashboardView.friendsPanel.revalidate();
        dashboardView.friendsPanel.repaint();
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
}

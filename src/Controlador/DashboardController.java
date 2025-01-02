package src.Controlador;

import src.Modelo.FavoritesObserver;
import src.Modelo.FavoritesManager;
import src.Modelo.SteamApiService;
import src.Vista.DashboardView;
import src.Vista.PanelFactory;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;


public class DashboardController implements FavoritesObserver {
    private final SteamApiService steamApiService;
    private final DashboardView dashboardView;
    private final String username;
    private List<Game> games;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;
    private final FavoritesManager favoritesManager;

    public DashboardController(SteamApiService service, FavoritesManager favoritesManager, DashboardView view, String username) {
        this.steamApiService = service;
        this.favoritesManager = favoritesManager;
        this.dashboardView = view;
        this.username = username;

        favoritesManager.addObserver(this);

        fetchGames();
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

        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, games.size());

        for (int i = start; i < end; i++) {
            Game game = games.get(i);
            JPanel gamePanel = PanelFactory.createGamePanel(game, e -> favoritesManager.addFavorite(game));
            dashboardView.gamesPanel.add(gamePanel);
        }

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


}

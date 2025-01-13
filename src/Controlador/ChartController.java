package src.Controlador;

import src.Modelo.Data.FavoritesManager;
import src.Vista.MainViews.DashboardView;
import src.Vista.PanelF.ChartPanelFactory;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChartController {
    private final DashboardView dashboardView;
    private final FavoritesManager favoritesManager;
    private final ChartPanelFactory chartPanelFactory;
    private boolean isUpdatingFromFavorites = false;

    public ChartController(DashboardView dashboardView, FavoritesManager favoritesManager) {
        this.dashboardView = dashboardView;
        this.favoritesManager = favoritesManager;
        this.chartPanelFactory = new ChartPanelFactory();
        favoritesManager.addObserver(this::onFavoritesChanged);
        setupListeners();
    }

    private void onFavoritesChanged(List<Game> updatedFavorites) {
        isUpdatingFromFavorites = true;
        System.out.println("Favoritos actualizados: " + updatedFavorites.size());
        updateChart();
        isUpdatingFromFavorites = false;
    }

    private void setupListeners() {
        dashboardView.chartTypeComboBox.addActionListener(e -> {
            if (!isUpdatingFromFavorites) {
                updateChart();
            }
        });
    }

    public void updateChart() {
        String selectedChartType = (String) dashboardView.chartTypeComboBox.getSelectedItem();
        List<Game> favoriteGames = favoritesManager.getFavoriteGames();
        dashboardView.statsPanel.removeAll();
        if (favoriteGames.isEmpty()) {
            JLabel noDataLabel = new JLabel("No hay juegos favoritos para mostrar.", SwingConstants.CENTER);
            noDataLabel.setFont(new Font("Arial", Font.BOLD, 16));
            dashboardView.statsPanel.add(noDataLabel, BorderLayout.CENTER);
            dashboardView.statsPanel.revalidate();
            dashboardView.statsPanel.repaint();
            return;
        }

        try {
            JPanel chartPanel = chartPanelFactory.createChart(selectedChartType, favoriteGames);
            if (chartPanel == null) {
                throw new IllegalStateException("El gráfico generado es nulo.");
            }
            dashboardView.statsPanel.add(dashboardView.chartTypeComboBox, BorderLayout.NORTH); // Mantener el JComboBox fijo
            dashboardView.statsPanel.add(chartPanel, BorderLayout.CENTER);
            dashboardView.statsPanel.revalidate();
            dashboardView.statsPanel.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dashboardView.frame, "Error al generar el gráfico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

}
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

    public ChartController(DashboardView dashboardView, FavoritesManager favoritesManager) {
        this.dashboardView = dashboardView;
        this.favoritesManager = favoritesManager;
        this.chartPanelFactory = new ChartPanelFactory();

        setupListeners();
    }

    // Configurar los listeners para el JComboBox (tipos de gráficos)
    private void setupListeners() {
        dashboardView.chartTypeComboBox.addActionListener(e -> updateChart());
    }

    // Actualizar el gráfico basado en el tipo seleccionado
    public void updateChart() {
        String selectedChartType = (String) dashboardView.chartTypeComboBox.getSelectedItem();
        List<Game> favoriteGames = favoritesManager.getFavoriteGames();

        if (favoriteGames.isEmpty()) {
            JOptionPane.showMessageDialog(dashboardView.frame, "No hay juegos favoritos para mostrar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear el gráfico utilizando la fábrica
        JPanel chartPanel = chartPanelFactory.createChart(selectedChartType, favoriteGames);

        // Actualizar el panel de estadísticas en la vista
        dashboardView.statsPanel.removeAll();
        dashboardView.statsPanel.add(chartPanel, BorderLayout.CENTER);
        dashboardView.statsPanel.revalidate();
        dashboardView.statsPanel.repaint();
    }
}

package src.Controlador;

import src.Modelo.API.YoutubeApiService;
import src.Modelo.Data.FavoritesManager;
import src.Modelo.Sort.SortByPlaytime;
import src.Modelo.Sort.SortStrategy;
import src.Modelo.API.SteamApiService;
import src.Vista.MainViews.DashboardView;
import src.Vista.MainViews.StartView;

import javax.swing.*;

public class StartController {
    private final SteamApiService steamApiService;
    private final StartView startView;
    private final YoutubeApiService youtubeApiService;

    public StartController(SteamApiService service, StartView startView, YoutubeApiService youtubeApiService) {
        this.steamApiService = service;
        this.startView = startView;
        this.youtubeApiService = youtubeApiService;

        startView.nextButton.addActionListener(e -> showDashboard());
    }

    private void showDashboard() {
        String username = startView.usernameField.getText();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(startView.frame, "Por favor, ingrese un nombre de usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ocultar la pantalla de inicio
        startView.frame.setVisible(false);

        // Crear y mostrar el cuadro de mando
        DashboardView dashboardView = new DashboardView();
        FavoritesManager favoritesManager = new FavoritesManager();
        SortStrategy sortStrategy = new SortByPlaytime();
        new DashboardController(steamApiService, favoritesManager, dashboardView, sortStrategy, username, youtubeApiService);
        dashboardView.show();

        // Agregar opción para abrir otro dashboard
        dashboardView.openNewDashboardButton.addActionListener(e -> openNewDashboard());
    }

    private void openNewDashboard() {
        // Mostrar la pantalla de inicio nuevamente para otro usuario
        StartView newStartView = new StartView();
        new StartController(steamApiService, newStartView, youtubeApiService);
        newStartView.show();
    }
}
package src.Controlador;

import src.Modelo.FavoritesManager;
import src.Modelo.SortByPlaytime;
import src.Modelo.SortStrategy;
import src.Modelo.SteamApiService;
import src.Vista.DashboardView;
import src.Vista.StartView;

import javax.swing.*;

public class StartController {
    private final SteamApiService steamApiService;
    private final StartView startView;

    public StartController(SteamApiService service, StartView startView) {
        this.steamApiService = service;
        this.startView = startView;

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
        new DashboardController(steamApiService, favoritesManager, dashboardView, sortStrategy, username);
        dashboardView.show();

        // Agregar opción para abrir otro dashboard
        dashboardView.openNewDashboardButton.addActionListener(e -> openNewDashboard());
    }

    private void openNewDashboard() {
        // Mostrar la pantalla de inicio nuevamente para otro usuario
        StartView newStartView = new StartView();
        new StartController(steamApiService, newStartView);
        newStartView.show();
    }
}
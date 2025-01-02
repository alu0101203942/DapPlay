package src.Controlador;

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
        new DashboardController(steamApiService, dashboardView, username);
        dashboardView.show();
    }
}
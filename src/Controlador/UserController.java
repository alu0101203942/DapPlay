package src.Controlador;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import src.Modelo.Data.UserModel;
import src.Vista.MainViews.*;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class UserController {
    private final DashboardView dashboardView;
    private final UserModel userModel;

    public UserController(DashboardView dashboardView, UserModel userModel) {
        this.dashboardView = dashboardView;
        this.userModel = userModel;
    }
    public void displayUserInfo() {
        try {
            // Configurar datos de usuario en la vista
            dashboardView.usernameLabel.setText("Nombre: " + userModel.getUsername());
            dashboardView.gamesCountLabel.setText("Juegos: " + userModel.getOwnedGamesCount());
            dashboardView.profileStatusLabel.setText("Estado del Perfil: " + userModel.getProfileStatus());
            dashboardView.connectionStatusLabel.setText("Conexión: " + userModel.getConnectionStatus());

            // Cargar y escalar el avatar
            URL avatarUrl = new URL(userModel.getAvatarUrl());
            ImageIcon avatarIcon = new ImageIcon(avatarUrl);
            Image scaledImage = avatarIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            dashboardView.avatarLabel.setIcon(new ImageIcon(scaledImage));

        } catch (Exception ex) {
            dashboardView.avatarLabel.setText("Avatar no disponible");
            System.err.println("Error al cargar la información del usuario: " + ex.getMessage());
        }
    }




}
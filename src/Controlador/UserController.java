package src.Controlador;

import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import src.Modelo.API.SteamApiService;
import src.Vista.ViewManager;
import src.Modelo.Data.UserModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class UserController {
    private final UserModel userModel;
    private final ViewManager viewManager;

    public UserController(UserModel userModel, ViewManager viewManager) {
        this.userModel = userModel;
        this.viewManager = viewManager;
    }

    public void fetchAndDisplayUserInfo(String username) {
        try {
            // Obtener datos del modelo
            Player user = userModel.getUserInfo(username);
            int gameCount = userModel.getOwnedGamesCount(username);

            // Preparar datos para la vista
            String avatarUrl = user.getAvatarfull();
            ImageIcon avatarIcon = fetchAvatarIcon(avatarUrl);
            String profileStatus = user.getCommunityvisibilitystate() == 3 ? "Público" : "Privado";
            String connectionStatus = mapConnectionStatus(user.getPersonastate().intValue());

            // Pasar datos a la vista
            viewManager.displayUserInfo(avatarIcon, user.getPersonaname(), gameCount, profileStatus, connectionStatus, user.getProfileurl());
        } catch (Exception e) {
            viewManager.showError("Error al cargar información del usuario: " + e.getMessage());
        }
    }

    private ImageIcon fetchAvatarIcon(String avatarUrl) {
        try {
            URL url = new URL(avatarUrl);
            Image image = ImageIO.read(url);
            Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            return new ImageIcon(); // Devuelve un icono vacío si falla
        }
    }

    private String mapConnectionStatus(int state) {
        return switch (state) {
            case 0 -> "Offline";
            case 1 -> "Online";
            case 2 -> "Busy";
            case 3 -> "Away";
            case 4 -> "Snooze";
            case 5 -> "Looking to Trade";
            case 6 -> "Looking to Play";
            default -> "Unknown";
        };
    }
}


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
            Player user = userModel.getUserInfo(username);
            int gameCount = userModel.getOwnedGamesCount(username);

            // Pasar datos directamente como objeto Player y el número de juegos
            viewManager.displayUserInfo(user, gameCount);
        } catch (Exception e) {
            viewManager.showError("Error al cargar información del usuario: " + e.getMessage());
        }
    }


}


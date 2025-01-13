package src.Modelo.Data;

import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import src.Modelo.API.SteamApiService;

import java.util.List;

public class UserModel {
    private final SteamApiService steamApiService;
    private Player currentUser;
    private int ownedGamesCount;
    private boolean userInfoLoaded;

    public UserModel(SteamApiService steamApiService) {
        this.steamApiService = steamApiService;
    }

    public String getAvatarUrl() {
        if (!userInfoLoaded) {
            throw new IllegalStateException("No se ha cargado la información del usuario.");
        }
        return currentUser.getAvatarfull();
    }

    public void loadUserData(String username) throws Exception {
        String steamId64 = isSteamId64(username) ? username : steamApiService.getSteamIdFromUsername(username);
        List<Player> userInfoList = steamApiService.getPlayerSummaries(steamId64);

        if (!userInfoList.isEmpty()) {
            currentUser = userInfoList.get(0);
            ownedGamesCount = steamApiService.getOwnedProductsCount(steamId64);
            userInfoLoaded = true;
        } else {
            throw new Exception("No se encontró información del usuario.");
        }
    }

    public String getUsername() {
        if (!userInfoLoaded) {
            throw new IllegalStateException("No se ha cargado la información del usuario.");
        }
        return currentUser.getPersonaname();
    }

    public String getProfileStatus() {
        if (currentUser == null) {
            throw new IllegalStateException("No se ha cargado la información del usuario.");
        }
        return currentUser.getCommunityvisibilitystate() == 3 ? "Público" : "Privado";
    }

    public String getConnectionStatus() {
        if (currentUser == null) {
            throw new IllegalStateException("No se ha cargado la información del usuario.");
        }
        return mapConnectionStatus(currentUser.getPersonastate().intValue());
    }

    public String getProfileUrl() {
        if (currentUser == null) {
            throw new IllegalStateException("No se ha cargado la información del usuario.");
        }
        return currentUser.getProfileurl();
    }

    public int getOwnedGamesCount() {
        return ownedGamesCount;
    }

    public boolean isSteamId64(String input) {
        return input.matches("\\d{17}");
    }

    public void loadUserInfo() {
        userInfoLoaded = true;
    }

    private String mapConnectionStatus(int status) {
        return switch (status) {
            case 0 -> "Offline";
            case 1 -> "Online";
            case 2 -> "Busy";
            case 3 -> "Away";
            case 4 -> "Snooze";
            case 5 -> "Looking to Trade";
            case 6 -> "Looking to Play";
            default -> "Desconocido";
        };
    }
}

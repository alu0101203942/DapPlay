package src.Modelo.Data;

import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import src.Modelo.API.SteamApiService;

import java.util.List;

public class UserModel {
    private final SteamApiService steamApiService;

    public UserModel(SteamApiService steamApiService) {
        this.steamApiService = steamApiService;
    }

    public Player getUserInfo(String username) throws Exception {
        String steamId64 = isSteamId64(username) ? username : steamApiService.getSteamIdFromUsername(username);
        List<Player> userInfoList = steamApiService.getPlayerSummaries(steamId64);

        if (!userInfoList.isEmpty()) {
            return userInfoList.get(0);
        } else {
            throw new Exception("No se encontró información del usuario.");
        }
    }

    public int getOwnedGamesCount(String username) throws Exception {
        String steamId64 = isSteamId64(username) ? username : steamApiService.getSteamIdFromUsername(username);
        return steamApiService.getOwnedProductsCount(steamId64);
    }

    private boolean isSteamId64(String input) {
        return input.matches("\\d{17}");
    }
}

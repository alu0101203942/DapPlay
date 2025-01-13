package src.Controlador;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import src.Modelo.API.SteamApiService;
import src.Vista.MainViews.DashboardView;
import src.Vista.PanelF.AchievementPanelFactory;
import src.Vista.ViewManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AchievementsController {
    private final SteamApiService steamApiService;

    public AchievementsController(SteamApiService steamApiService) {
        this.steamApiService = steamApiService;
    }

    private boolean isSteamId64(String input) {
        return input.matches("\\d{17}");
    }

    public void fetchAchievements(String steamId64, DashboardView dashboardView, Game selectedGame, ViewManager viewManager) {
        try {
            if (!isSteamId64(steamId64)) {
                steamId64 = steamApiService.getSteamIdFromUsername(steamId64);
            }
            List<Map<String, Object>> rawAchievements = SteamApiService.fetchAchievements(
                    steamId64,
                    String.valueOf(selectedGame.getAppid()),
                    steamApiService.getApiKey()
            );
            List<Map<String, String>> achievementDetails = steamApiService.fetchAchievementDetails(String.valueOf(selectedGame.getAppid()), steamApiService.getApiKey());
            List<Map<String, Object>> unlockedAchievements = new ArrayList<>();
            List<Map<String, Object>> lockedAchievements = new ArrayList<>();

            for (Map<String, Object> rawAchievement : rawAchievements) {
                String apiname = (String) rawAchievement.get("apiname");
                int achieved = (int) rawAchievement.get("achieved");

                if (apiname == null) {
                    System.out.println("Logro con 'apiname' nulo encontrado, ignorando...");
                    continue;
                }

                Map<String, String> details = achievementDetails.stream()
                        .filter(d -> apiname.equalsIgnoreCase(d.get("name")))
                        .findFirst()
                        .orElse(null);

                if (details != null) {
                    Map<String, Object> achievement = new HashMap<>();
                    achievement.put("apiname", apiname);
                    achievement.put("achieved", achieved);
                    achievement.put("displayName", details.get("displayName"));
                    achievement.put("description", details.get("description"));
                    achievement.put("icon", details.get("icon"));
                    achievement.put("iconGray", details.get("icongray"));

                    if (achieved == 1) {
                        unlockedAchievements.add(achievement);
                    } else {
                        lockedAchievements.add(achievement);
                    }
                } else {
                    System.out.println("No se encontraron detalles para el logro: " + apiname);
                }
            }
            viewManager.displayAchievements(unlockedAchievements, lockedAchievements);

        } catch (Exception e) {
            handleNoAchievements(selectedGame.getName(), dashboardView);
        }
    }

    public void handleNoAchievements(String gameName, DashboardView dashboardView) {
        String imageUrl = "https://raw.githubusercontent.com/alu0101203942/DapPlay/refs/heads/main/error.jpg"; // Enlace de la imagen
        JPanel achievementsPanel = dashboardView.getAchievementsPanel();
        AchievementPanelFactory factory = new AchievementPanelFactory();
        factory.displayNoAchievementsMessage(gameName, imageUrl, achievementsPanel);
    }

    public Map<String, Integer> getUnlockedAchievements(String steamId64, List<Game> games) {
        Map<String, Integer> unlockedAchievementsCount = new HashMap<>();
        for (Game game : games) {
            try {
                List<Map<String, Object>> rawAchievements = SteamApiService.fetchAchievements(
                        steamId64,
                        String.valueOf(game.getAppid()),
                        steamApiService.getApiKey()
                );
                int unlockedCount = 0;
                for (Map<String, Object> rawAchievement : rawAchievements) {
                    int achieved = (int) rawAchievement.get("achieved");
                    if (achieved == 1) {
                        unlockedCount++;
                    }
                }
                unlockedAchievementsCount.put(game.getName(), unlockedCount);
            } catch (Exception e) {
                System.out.println("Error al obtener logros para " + game.getName() + ": " + e.getMessage());
            }
        }
        return unlockedAchievementsCount;
    }
}
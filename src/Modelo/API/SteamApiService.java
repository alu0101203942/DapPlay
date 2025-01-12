package src.Modelo.API;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.friendslist.Friend;
import com.lukaspradel.steamapi.data.json.friendslist.GetFriendList;
import com.lukaspradel.steamapi.data.json.playersummaries.*;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetFriendListRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONArray;
import org.json.JSONObject;

public class SteamApiService {
    private static SteamApiService instance;
    private final String apiKey;

    private SteamApiService(String apiKey) {
        this.apiKey = apiKey;
    }

    public static SteamApiService getInstance(String apiKey) {
        if (instance == null) {
            instance = new SteamApiService(apiKey);
        }
        return instance;
    }

    public String getSteamIdFromUsername(String username) throws SteamApiException {
        try {
            String url = "http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=" + apiKey + "&vanityurl=" + username;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            JSONObject json = new JSONObject(content.toString());
            if (json.getJSONObject("response").getInt("success") == 1) {
                return json.getJSONObject("response").getString("steamid");
            } else {
                throw new SteamApiException("User not found");
            }
        } catch (Exception e) {
            throw new SteamApiException("Failed to resolve username to SteamID", e);
        }
    }

    public List<Game> getOwnedGames(String steamId64) throws SteamApiException {
        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder(apiKey).build();
        var request = SteamWebApiRequestFactory.createGetOwnedGamesRequest(steamId64, true, true, Collections.emptyList());
        GetOwnedGames ownedGames = client.processRequest(request);

        if (ownedGames != null && ownedGames.getResponse() != null) {
            return ownedGames.getResponse().getGames();
        } else {
            return Collections.emptyList();
        }
    }

    public int getOwnedProductsCount(String steamId64) throws SteamApiException {
        String url = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v1/?key=" + apiKey + "&steamid=" + steamId64 + "&include_played_free_games=true&include_free_sub=true&skip_unvetted_apps=false";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                System.out.println(inputLine);
            }
            in.close();
            conn.disconnect();

            JSONObject json = new JSONObject(content.toString());
            if (json.has("response") && json.getJSONObject("response").has("game_count")) {
                return json.getJSONObject("response").getInt("game_count");
            } else {
                throw new SteamApiException("Failed to get owned games count");
            }
        } catch (Exception e) {
            throw new SteamApiException("Failed to get owned games count", e);
        }
    }



    public List<Friend> getFriends(String steamId64) throws SteamApiException {
        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder(apiKey).build();

        var request = SteamWebApiRequestFactory.createGetFriendListRequest(
                steamId64,
                GetFriendListRequest.Relationship.FRIEND
        );

        GetFriendList friendList = client.processRequest(request);

        if (friendList != null && friendList.getFriendslist() != null) {
            return friendList.getFriendslist().getFriends();
        } else {
            return Collections.emptyList();
        }
    }

    public List<Player> getPlayerSummaries(String steamId64) throws SteamApiException {
        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder(apiKey).build();
        var request = SteamWebApiRequestFactory.createGetPlayerSummariesRequest(Collections.singletonList(steamId64));
        GetPlayerSummaries playerSummaries = client.processRequest(request);

        if (playerSummaries != null && playerSummaries.getResponse() != null) {
            return playerSummaries.getResponse().getPlayers();
        } else {
            return Collections.emptyList();
        }
    }

    public static List<Map<String, Object>> fetchAchievements(String steamId, String appId, String apiKey) throws IOException {
        String urlString = "https://api.steampowered.com/ISteamUserStats/GetPlayerAchievements/v0001/?appid=" + appId + "&key=" + apiKey + "&steamid=" + steamId;
        System.out.println("Fetching achievements from URL: " + urlString);

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Steam API responded with error code: " + responseCode);
        }

        Scanner scanner = new Scanner(connection.getInputStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();


        JSONObject jsonResponse = new JSONObject(response.toString());
        if (jsonResponse.has("playerstats") && jsonResponse.getJSONObject("playerstats").has("achievements")) {
            JSONArray achievementsArray = jsonResponse
                    .getJSONObject("playerstats")
                    .getJSONArray("achievements");

            List<Map<String, Object>> achievements = new ArrayList<>();
            for (int i = 0; i < achievementsArray.length(); i++) {
                JSONObject achievement = achievementsArray.getJSONObject(i);
                Map<String, Object> achievementData = new HashMap<>();

                achievementData.put("apiname", achievement.optString("apiname", "unknown"));

                // Forzar el valor como Integer
                int achievedValue = achievement.optInt("achieved", 0);
                achievementData.put("achieved", achievedValue);
                achievements.add(achievementData);
            }

            return achievements;
        } else if (jsonResponse.has("playerstats") && jsonResponse.getJSONObject("playerstats").has("error")) {
            String error = jsonResponse.getJSONObject("playerstats").getString("error");
            throw new IOException("Error from Steam API: " + error);
        } else {
            throw new IOException("No achievements found or invalid response from Steam API.");
        }
    }

    public List<Map<String, String>> fetchAchievementDetails(String appId, String apiKey) throws IOException {
        String urlString = "https://api.steampowered.com/ISteamUserStats/GetSchemaForGame/v2/?key=" + apiKey + "&appid=" + appId;

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Leer la respuesta
        Scanner scanner = new Scanner(connection.getInputStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        // Parsear la respuesta
        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONObject game = jsonResponse.getJSONObject("game");
        JSONObject availableGameStats = game.getJSONObject("availableGameStats");
        JSONArray achievementsArray = availableGameStats.getJSONArray("achievements");

        List<Map<String, String>> achievementDetails = new ArrayList<>();
        for (int i = 0; i < achievementsArray.length(); i++) {
            JSONObject achievement = achievementsArray.getJSONObject(i);

            Map<String, String> details = new HashMap<>();
            details.put("name", achievement.optString("name", "unknown"));
            details.put("displayName", achievement.optString("displayName", "Unknown Achievement"));
            details.put("description", achievement.optString("description", "No description available."));
            details.put("icon", achievement.optString("icon", ""));
            details.put("icongray", achievement.optString("icongray", ""));
            achievementDetails.add(details);
        }

        return achievementDetails;
    }

    public String getApiKey() {
        return apiKey;
    }
}
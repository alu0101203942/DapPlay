package src;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class SteamApiService {
    private final String apiKey;

    public SteamApiService(String apiKey) {
        this.apiKey = apiKey;
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
}
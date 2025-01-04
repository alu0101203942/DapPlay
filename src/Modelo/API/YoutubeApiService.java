package src.Modelo.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import org.json.JSONArray;
import org.json.JSONObject;

public class YoutubeApiService {
    private static YoutubeApiService instance;
    private final String apiKey;

    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3/search";

    private YoutubeApiService(String apiKey) {
        this.apiKey = apiKey;
    }

    public static YoutubeApiService getInstance(String apiKey) {
        if (instance == null) {
            instance = new YoutubeApiService(apiKey);
        }
        return instance;
    }

    // Método para buscar videos
    public String searchVideosByGame(Game game) throws Exception {
        String query = game.getName() + " gameplay"; // Buscamos videos de gameplay
        String urlString = BASE_URL + "?part=snippet&q=" + query + "&type=video&maxResults=1&key=" + apiKey;
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
    public String extractVideoId(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray items = jsonObject.getJSONArray("items");
        if (items.length() > 0) {
            JSONObject video = items.getJSONObject(0);
            return video.getJSONObject("id").getString("videoId");
        }
        return null; // Si no hay videos
    }
}

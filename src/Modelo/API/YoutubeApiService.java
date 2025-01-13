package src.Modelo.API;

import org.json.JSONArray;
import org.json.JSONObject;
import src.Modelo.Data.GameplayModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class YoutubeApiService {
    private static YoutubeApiService instance;
    private String apiKey;

    private YoutubeApiService(String apiKey) {
        this.apiKey = apiKey;
    }

    public static YoutubeApiService getInstance(String apiKey) {
        if (instance == null) {
            instance = new YoutubeApiService(apiKey);
        }
        return instance;
    }

    public String getApiKey() {
        return apiKey;
    }

    public List<GameplayModel> searchLatestVideosByGame(String gameName) throws Exception {
        // Construye la URL con el parámetro "order=date" para obtener los videos más recientes
        String urlString = String.format(
                "https://www.googleapis.com/youtube/v3/search?part=snippet&q=%s+gameplay&type=video&maxResults=5&order=date&key=%s",
                gameName.replace(" ", "%20"), apiKey);
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        System.out.println("Sending GET request to: " + urlString);

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Procesa la respuesta JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray items = jsonResponse.getJSONArray("items");
            List<GameplayModel> videoDataList = new ArrayList<>();

            for (int i = 0; i < items.length(); i++) {
                JSONObject video = items.getJSONObject(i);
                String videoId = video.getJSONObject("id").getString("videoId");
                String videoUrl = "https://www.youtube.com/watch?v=" + videoId;
                String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";

                videoDataList.add(new GameplayModel(thumbnailUrl, videoUrl));
            }

            if (videoDataList.isEmpty()) {
                throw new Exception("No se encontraron videos de gameplay relevantes para este juego.");
            }

            return videoDataList;
        } else {
            throw new Exception("Error: Código de respuesta " + responseCode);
        }
    }
}

package src.Modelo.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

    public String searchVideosByGame(String gameName) throws Exception {
        String urlString = String.format(
                "https://www.googleapis.com/youtube/v3/search?part=snippet&q=%s+gameplay&type=video&maxResults=1&key=%s",
                gameName.replace(" ", "%20"), apiKey);
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new Exception("Server returned HTTP response code: " + responseCode + " for URL: " + urlString);
        }
    }
}
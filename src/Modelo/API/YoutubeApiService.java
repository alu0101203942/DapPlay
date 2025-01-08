package src.Modelo.API;

import org.json.JSONArray;
import org.json.JSONObject;
import src.Modelo.VideoData;

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

    public List<VideoData> searchLatestVideosByGame(String gameName) throws Exception {
        String urlString = String.format(
                "https://www.googleapis.com/youtube/v3/search?part=snippet&q=%s+gameplay&type=video&maxResults=5&key=%s",
                gameName.replace(" ", "%20"), apiKey);
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray items = jsonResponse.getJSONArray("items");
            List<VideoData> videoDataList = new ArrayList<>();

            for (int i = 0; i < items.length(); i++) {
                JSONObject video = items.getJSONObject(i);
                String videoId = video.getJSONObject("id").getString("videoId");
                String videoUrl = "https://www.youtube.com/watch?v=" + videoId;
                String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
                videoDataList.add(new VideoData(videoUrl, thumbnailUrl));
            }
            return videoDataList;
        } else {
            throw new Exception("Error: Código de respuesta " + responseCode);
        }
    }


}
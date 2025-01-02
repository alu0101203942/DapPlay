package src.Modelo;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;

import java.util.Comparator;
import java.util.List;

public class SortByPlaytime implements SortStrategy {
    @Override
    public void sort(List<Game> games) {
        games.sort(Comparator.comparingLong(Game::getPlaytimeForever).reversed());
    }
}
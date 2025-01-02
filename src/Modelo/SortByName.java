package src.Modelo;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;

import java.util.Comparator;
import java.util.List;

public class SortByName implements SortStrategy {
    @Override
    public void sort(List<Game> games) {
        games.sort(Comparator.comparing(Game::getName));
    }
}
package src.Modelo;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;

import java.util.List;

public interface SortStrategy {
    void sort(List<Game> games);
}
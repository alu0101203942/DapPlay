package src.Modelo;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;

import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {
    private final List<Game> favoriteGames = new ArrayList<>();
    private final List<FavoritesObserver> observers = new ArrayList<>();

    public void addFavorite(Game game) {
        if (!favoriteGames.contains(game)) {
            favoriteGames.add(game);
            notifyObservers();
        }
    }

    public void removeFavorite(Game game) {
        if (favoriteGames.remove(game)) {
            notifyObservers();
        }
    }

    public void addObserver(FavoritesObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (FavoritesObserver observer : observers) {
            observer.onFavoritesUpdated(favoriteGames);
        }
    }
}

package src.Modelo.Data;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    public List<Game> getFavoriteGames() {
        return new ArrayList<>(favoriteGames);
    }

    public void startAutoUpdate() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> notifyObservers(), 0, 10, TimeUnit.MINUTES);
    }
}

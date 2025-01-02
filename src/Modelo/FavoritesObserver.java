package src.Modelo;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;

import java.util.List;

public interface FavoritesObserver {
    void onFavoritesUpdated(List<Game> favoriteGames);
}